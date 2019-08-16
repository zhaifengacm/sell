package com.imooc.sell.dataobject;


import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@DynamicUpdate
public class OrderMaster {

    /*订单ID*/
    @Id
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
    private Integer orderStatus = OrderStatusEnum.New.getCode();

    /*支付状态，默认为等待支付*/
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    /*订单创建时间*/
    private Date createTime;

    /*订单更新时间*/
    private Date updateTime;

}
