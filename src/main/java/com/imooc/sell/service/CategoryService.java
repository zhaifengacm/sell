package com.imooc.sell.service;

import com.imooc.sell.dataobject.ProductCategory;

import java.util.List;

public interface CategoryService {
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryList);

    ProductCategory save(ProductCategory productCategory);
}
