package com.imooc.sell.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class OrderDetail {

    /*订单详情ID*/
    @Id
    private String detailId;

    /*订单编号*/
    private String orderId;

    /*产品编号*/
    private String productId;

    /*产品名称*/
    private String productName;

    /*产品价格*/
    private BigDecimal productPrice;

    /*产品数量*/
    private Integer productQuantity;

    /*产品小图片*/
    private String productIcon;

}
