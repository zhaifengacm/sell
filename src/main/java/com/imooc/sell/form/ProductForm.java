package com.imooc.sell.form;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductForm {

    /*商品ID*/
    private String productId;

    /*商品名称*/
    private String productName;

    /*商品价格*/
    private BigDecimal productPrice;

    /*商品库存*/
    private Integer productStock;

    /*商品描述*/
    private String productDescription;

    /*商品小图*/
    private String productIcon;


    /*类目编号*/
    private Integer categoryType;

    /*商品创建时间*/
    private Date createTime;

    /*商品更新时间*/
    private Date updateTime;

}
