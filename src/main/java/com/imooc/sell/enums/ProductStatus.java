package com.imooc.sell.enums;

import lombok.Getter;

/**
 * 商品状态
 */
@Getter
public enum ProductStatus implements CodeEnum {
    UP(0,"在架"),
    DOWN(1,"下架")
    ;
    private Integer code;
    private String Msg;

    ProductStatus(Integer code, String msg) {
        this.code = code;
        Msg = msg;
    }
}
