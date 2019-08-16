package com.imooc.sell.repository;

import com.imooc.sell.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOneTest() {
        ProductCategory productCategory = productCategoryRepository.findById(1).orElse(null);
        System.out.println(productCategory);
    }

    @Test
    @Transactional
    public void saveTest() {
        ProductCategory productCategory = new ProductCategory("男生最爱",3);
        ProductCategory result = productCategoryRepository.save(productCategory);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByCaAndCategoryTypeInTest() {
        List<Integer> list = Arrays.asList(2,3);
        List<ProductCategory> result = productCategoryRepository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,result.size());

    }

}