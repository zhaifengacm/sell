package com.imooc.sell.dataobject.mapper;

import org.apache.ibatis.annotations.Insert;

import java.util.Map;

public interface ProductCategoryMapper {

    @Insert("insert into product_category(category_name, category_type) values(#{category_type,jdbcType=VARCHAR}," +
            "#{category_type,jdbcType=INTEGER})")
    int insertByMapper(Map<String,Object> map);
}
