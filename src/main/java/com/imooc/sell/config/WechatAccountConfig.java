package com.imooc.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {
    /**
     * 公众平台
     */
    private String myAppId;
    private String myAppSecret;

    /**
     * 开放平台
     */
    private String openAppId;
    private String openAppSecret;

    /**
     * 商户号
     */
    private String mchId;
    /**
     * 商户密钥
     */
    private String mchKey;
    /**
     * 商户证书路径
     */
    private String keyPath;
    /**
     * 微信异步通知地址
     */
    private String notifyUrl;

    /**
     * 微信模版Id
     */
    private Map<String,String> templateId;
}
