package com.imooc.sell.service;

import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductInfo findOne(String productId);

    /*查询所有在架的商品*/
    List<ProductInfo> findUpAll();

    /*管理端查询所有商品*/
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    /*加库存*/
    void increaseStock(List<CartDTO> cartDTO);

    /*减库存*/
    void decreaseStock(List<CartDTO> cartDTO);

    /*上架*/
    ProductInfo onSale(String productId);

    /*下架*/
    ProductInfo offSale(String productId);

}
