package com.imooc.sell.service.impl;

import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.service.PayService;
import com.imooc.sell.util.JsonUtil;
import com.imooc.sell.util.MathUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PayServiceImpl implements PayService {
    private static final String ORDER_NAME = "微信点餐订单";
    @Autowired
    private BestPayService bestPayService;

    @Autowired
    private OrderService orderService;

    /**
     * 发起微信支付
     * @param orderDTO
     * @return
     */
    @Override
    public PayResponse create(OrderDTO orderDTO) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        log.info("【微信支付】发起支付请求request={}",JsonUtil.toJson(payRequest));
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("[微信支付] 发起支付response={}",JsonUtil.toJson(payResponse));

        return payResponse;

    }

    /**
     * 接收微信异步通知
     * 1.验证签名
     * 2.支付状态
     * 3.支付的金额校验
     * 4.支付的人是否一致
     * @param notifyData
     */
    @Override
    public PayResponse notify(String notifyData) {
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付异步通知】,payResponse={}",payResponse);
        //查询订单
        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());
        //判断订单是否存在
        if (orderDTO == null) {
            log.error("【微信支付异步通知】,订单不存在orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //判断金额是否一致(0.10与0.1不一样)
        if (!MathUtil.equal(payResponse.getOrderAmount(),orderDTO.getOrderAmount().doubleValue())) {
            log.error("【微信支付异步通知】,订单金额不一致orderDTO={},payResponse={}",
                    orderDTO.getOrderAmount(), payResponse.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_ERR);
        }
        //修改订单的支付状态
        orderService.paid(orderDTO);
        return payResponse;
    }

    /**
     * 微信退款
     * @param orderDTO
     */
    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("[微信退款],refundRequest={}",JsonUtil.toJson(refundRequest));
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("[微信退款],refundResponse={}",JsonUtil.toJson(refundResponse));

        return refundResponse;

    }
}
