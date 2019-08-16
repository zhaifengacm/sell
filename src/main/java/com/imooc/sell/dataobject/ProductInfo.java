package com.imooc.sell.dataobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.sell.enums.ProductStatus;
import com.imooc.sell.util.EnumUtil;
import com.imooc.sell.util.serializer.Date2LongSerializer;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@DynamicUpdate
@Data
public class ProductInfo {
    /*商品ID*/
    @Id
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

    /*商品状态-0正常-1下架*/
    private Integer productStatus = ProductStatus.UP.getCode();

    /*类目编号*/
    private Integer categoryType;

    /*商品创建时间*/
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /*商品更新时间*/
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    public ProductStatus getProductStatus() {
        return EnumUtil.getByCode(productStatus,ProductStatus.class);
    }
}
