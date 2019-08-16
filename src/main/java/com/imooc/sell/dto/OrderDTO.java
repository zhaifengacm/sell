package com.imooc.sell.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.dataobject.OrderMaster;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.util.EnumUtil;
import com.imooc.sell.util.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    /*订单ID*/
    private String orderId;

    /*买家名称*/
    private String buyerName;

    /*买家电话*/
    private String buyerPhone;

    /*买家地址*/
    private String buyerAddress;

    /*买家微信OpenId*/
    private String buyerOpenid;

    /*订单总金额*/
    private BigDecimal orderAmount;

    /*订单状态 默认为新订单*/
    private Integer orderStatus;


    /*支付状态，默认为等待支付*/
    private Integer payStatus;

    /*订单创建时间*/
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /*订单更新时间*/
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    /*订单详情列表*/
    private List<OrderDetail> orderDetailList;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus,OrderStatusEnum.class);
    }
    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        return EnumUtil.getByCode(payStatus,PayStatusEnum.class);
    }
}
