package com.imooc.sell.service.impl;

import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {
    private final String BUYEROPENID = "110110";
    private final String ORDERID = "1563348136105176631";
    @Autowired
    private OrderServiceImpl orderService;
    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("翟峰");
        orderDTO.setBuyerAddress("imooc");
        orderDTO.setBuyerPhone("12323422341");
        orderDTO.setBuyerOpenid(BUYEROPENID);
        /*购物车*/
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setProductId("12345678");
        orderDetail1.setProductQuantity(1);
        orderDetailList.add(orderDetail1);
        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setProductId("123457");
        orderDetail2.setProductQuantity(2);
        orderDetailList.add(orderDetail2);
        orderDTO.setOrderDetailList(orderDetailList);
        OrderDTO result = orderService.create(orderDTO);
        log.info("[创建订单]result={}",result);
        Assert.assertNotNull(orderDTO);
    }

    @Test
    public void findList() {
        PageRequest pageRequest = PageRequest.of(0,2);
        Page<OrderDTO> orderDTOPage = orderService.findList(pageRequest);
        log.info("查询订单列表result={}",orderDTOPage);
        Assert.assertNotEquals(0,orderDTOPage.getTotalElements());
    }

    @Test
    public void findOne() {
        OrderDTO result = orderService.findOne(ORDERID);
        log.info("查询订单result={}",result);
        Assert.assertEquals(ORDERID,result.getOrderId());

    }

    @Test
    public void queryOrderList() {
        PageRequest pageRequest = PageRequest.of(0,2);
        Page<OrderDTO> orderDTOPage = orderService.queryOrderList(BUYEROPENID,pageRequest);
        log.info("查询订单列表result={}",orderDTOPage);
        Assert.assertNotEquals(0,orderDTOPage.getTotalElements());
    }

    @Test
    public void cancel() {
        OrderDTO result = orderService.findOne(ORDERID);
        OrderDTO orderDTO = orderService.cancel(result);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),orderDTO.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTO result = orderService.findOne(ORDERID);
        OrderDTO orderDTO = orderService.finish(result);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(),orderDTO.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO result = orderService.findOne(ORDERID);
        OrderDTO orderDTO = orderService.paid(result);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(),orderDTO.getPayStatus());
    }
}