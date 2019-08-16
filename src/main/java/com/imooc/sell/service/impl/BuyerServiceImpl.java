package com.imooc.sell.service.impl;

import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.service.BuyerService;
import com.imooc.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    private OrderService orderService;
    @Override
    public OrderDTO findOrderOne(String openId, String orderId) {
        return checkOrderOwner(openId, orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openId, String orderId) {
        OrderDTO orderDTO = checkOrderOwner(openId,orderId);
        if (orderDTO == null) {
            log.error("[取消订单],查不到该订单,orderId={}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancel(orderDTO);
    }

    private OrderDTO checkOrderOwner(String openId, String orderId) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO==null) {
            return null;
        }
        //判断是否是自己的订单
        if (orderDTO.getBuyerOpenid().equalsIgnoreCase(openId)) {
            log.error("【查询订单】，订单Openid不一致，Openid={},OrderDTO={}",openId,orderDTO.getBuyerOpenid());
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
