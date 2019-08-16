package com.imooc.sell.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrderForm {
    /*买家姓名*/
    @NotEmpty(message = "姓名必须填写")
    private String name;

    /*买家手机号*/
    @NotEmpty(message = "手机号必须填写")
    private String phone;

    /*买家地址*/
    @NotEmpty(message = "地址必须填写")
    private String address;

    /*买家openid*/
    @NotEmpty(message = "openid必填")
    private String openid;

    /*购物车*/
    @NotEmpty(message = "购物车不能为空")
    private String items;



}
