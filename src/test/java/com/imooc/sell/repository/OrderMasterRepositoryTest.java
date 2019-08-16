package com.imooc.sell.repository;

import com.imooc.sell.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    private final String OPENID = "110110";
    @Test
    public void saveTest() {
        OrderMaster orderMaster =new OrderMaster();
        orderMaster.setOrderId("1234567");
        orderMaster.setBuyerName("source");
        orderMaster.setBuyerPhone("123456789123");
        orderMaster.setBuyerAddress("imooc");
        orderMaster.setBuyerOpenid("110110");
        orderMaster.setOrderAmount(new BigDecimal(2.5));
        OrderMaster result = orderMasterRepository.save(orderMaster);
        Assert.assertNotNull(result);
    }
    @Test
    public void findByBuyerOpenidTest() {
        PageRequest pageRequest = PageRequest.of(1,3);
        Page<OrderMaster> result = orderMasterRepository.findByBuyerOpenid(OPENID,pageRequest);
        //System.out.println(result.getTotalElements()+","+result.getTotalPages());
        Assert.assertNotEquals(0,result.getTotalElements());
    }
}