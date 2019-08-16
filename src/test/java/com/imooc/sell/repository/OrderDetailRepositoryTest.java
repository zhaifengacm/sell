package com.imooc.sell.repository;

import com.imooc.sell.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;
    @Test
    public void saveTest() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("1234567810");
        orderDetail.setOrderId("111111112");
        orderDetail.setProductIcon("htttp:xxx.com");
        orderDetail.setProductId("111112");
        orderDetail.setProductName("皮蛋粥");
        orderDetail.setProductPrice(new BigDecimal(12.3));
        orderDetail.setProductQuantity(12);
        OrderDetail result = repository.save(orderDetail);

        Assert.assertNotNull(result);

    }
    @Test
    public void findByOrderIdTest() {
        List<OrderDetail> result = repository.findByOrderId("111111111");
        Assert.assertNotEquals(0,result.size());

    }
}