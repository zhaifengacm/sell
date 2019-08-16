package com.imooc.sell.repository;

import com.imooc.sell.dataobject.SellerInfo;
import com.imooc.sell.util.KeyUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerInfoRepositoryTest {
    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Test
    public void findByOpenid() {
        SellerInfo sellerInfo = sellerInfoRepository.findByOpenid("abc");
        Assert.assertEquals("abc",sellerInfo.getOpenid());
    }

    @Test
    public void save() {
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setSellerId(KeyUtil.genUniqueKey());
        sellerInfo.setUsername("admin");
        sellerInfo.setPassword("admin");
        sellerInfo.setOpenid("abc");
        SellerInfo result = sellerInfoRepository.save(sellerInfo);
        Assert.assertNotNull(result);


    }
}