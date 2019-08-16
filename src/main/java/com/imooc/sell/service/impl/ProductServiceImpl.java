package com.imooc.sell.service.impl;

import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.dto.CartDTO;
import com.imooc.sell.enums.ProductStatus;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.repository.ProductInfoRepository;
import com.imooc.sell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).orElse(null);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatus.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTO) {
        for (CartDTO cart : cartDTO) {
            ProductInfo productInfo = repository.findById(cart.getProductId()).orElse(null);
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock()+ cart.getProductQuantity();
            productInfo.setProductStock(result);
        }
    }

    @Override
    @Transactional
    /**
     * redis锁机制，防止超库存
     */
    public void decreaseStock(List<CartDTO> cartDTO) {
        for (CartDTO cartDTO1 : cartDTO) {
            ProductInfo productInfo = repository.findById(cartDTO1.getProductId()).orElse(null);
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.getProductStock() -cartDTO1.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setProductStock(result);
        }
    }

    /**
     * 商品上架
     * @param productId
     * @return
     */
    @Override
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = repository.findById(productId).orElse(null);
        if (productInfo==null) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatus() == ProductStatus.UP) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //更新
        productInfo.setProductStatus(ProductStatus.UP.getCode());
        return repository.save(productInfo);
    }

    /**
     * 商品下架
     * @param productId
     * @return
     */
    @Override
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = repository.findById(productId).orElse(null);
        if (productInfo==null) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatus() == ProductStatus.DOWN) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //更新
        productInfo.setProductStatus(ProductStatus.DOWN.getCode());
        return repository.save(productInfo);
    }
}
