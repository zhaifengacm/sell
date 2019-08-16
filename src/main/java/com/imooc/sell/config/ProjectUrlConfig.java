package com.imooc.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Data
@ConfigurationProperties(prefix = "peojectUrl")
@Component
public class ProjectUrlConfig {

    /**
     * 微信公众账号授权URL
     */
    public String wxMpAuthorize;
    /**
     * 微信开放平台授权URL
     */
    public String wxOpenAuthorize;
    /**
     * 点餐系统
     */
    public String sell;

}
