package com.imooc.sell.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.sell.util.serializer.Date2LongSerializer;
import lombok.Data;

import java.util.Date;
import java.util.Random;

@Data
public class CategoryForm {
    /*类目ID*/
    private Integer categoryId;

    /*类目名字*/
    private String categoryName;

    /*类目编号*/
    private Integer categoryType;

}

