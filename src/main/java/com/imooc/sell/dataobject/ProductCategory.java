package com.imooc.sell.dataobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.sell.util.serializer.Date2LongSerializer;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * product_category表
 */
@Entity
@DynamicUpdate
@Data
public class ProductCategory {
    /*类目Id。*/
    @Id
    @GeneratedValue
    private Integer categoryId;

    /*类目名字*/
    private String categoryName;

    /*类目编号*/
    private Integer categoryType;

    /*类别创建时间*/
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /*类别更新时间*/
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    public ProductCategory() {
    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
