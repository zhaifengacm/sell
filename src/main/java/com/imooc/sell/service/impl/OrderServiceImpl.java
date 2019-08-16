package com.imooc.sell.service.impl;

import com.imooc.sell.converter.OrderMaster2OrderDTO;
import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.dataobject.OrderMaster;
import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.dto.CartDTO;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.repository.OrderDetailRepository;
import com.imooc.sell.repository.OrderMasterRepository;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.service.PayService;
import com.imooc.sell.service.ProductService;
import com.imooc.sell.service.PushMessage;
import com.imooc.sell.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private PayService payService;

    @Autowired
    private PushMessage pushMessage;

    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        //加锁

        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        //1.查询商品数量、价格
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null)  {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2.计算订单总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            //3.订单入库
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);
        }
        //4.写入orderMaster和orderDetail数据库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(0);
        orderMaster.setPayStatus(0);
        orderMasterRepository.save(orderMaster);

        //5.扣库存
        List<CartDTO> cartDTOList =
        orderDTO.getOrderDetailList().stream().map(e->
                new CartDTO(e.getProductId(),e.getProductQuantity())
        ).collect(Collectors.toList());

        productService.decreaseStock(cartDTOList);

        //发送WebSocket消息
        webSocket.sendMessage("有新的订单");

        //解锁

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {

        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (orderDetailList == null) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> queryOrderList(String buyerOpenId, Pageable pageable) {
        Page<OrderMaster> page = orderMasterRepository.findByBuyerOpenid(buyerOpenId,pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTO.convert2List(page.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList,pageable,page.getTotalElements());
        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        //1.判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.New.getCode())) {
            log.error("[取消订单]订单状态不正确.orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //2.修改订单状态为取消
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[取消订单]更新失败.orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_ERROR);
        }
        //3.返还库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("[取消订单]订单中无商品详情，orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e->
                new CartDTO(e.getProductId(),e.getProductQuantity())
        ).collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        //4.如果已经支付，则退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            payService.refund(orderDTO);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //1.判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.New.getCode())) {
            log.error("[完结订单]订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //2.修改状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateOrderMaster = orderMasterRepository.save(orderMaster);
        if (updateOrderMaster == null) {
            log.error("[完结订单]更新失败.orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_ERROR);
        }
        //3.订单完结，推送消息
        pushMessage.orderStatus(orderDTO);

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //1.判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.New.getCode())) {
            log.error("[完成支付订单]订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //2.判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【完成支付订单】订单支付状态不正确，orderId={},payStatus={}",orderDTO.getOrderId(),orderDTO.getPayStatus());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //2.修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateOrderMaster = orderMasterRepository.save(orderMaster);
        if (updateOrderMaster == null) {
            log.error("[完成支付订单]更新失败.orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_ERROR);
        }
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> page = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTO.convert2List(page.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOList,pageable,page.getTotalElements());
        return orderDTOPage;

    }
}
