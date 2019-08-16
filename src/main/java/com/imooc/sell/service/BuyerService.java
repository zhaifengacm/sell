package com.imooc.sell.service;

import com.imooc.sell.dto.OrderDTO;

/**
 * 买家
 */
public interface BuyerService {
    //查询一个订单
    OrderDTO findOrderOne(String openId,String orderId);

    //取消订单
    OrderDTO cancelOrder(String openId,String orderId);


}
