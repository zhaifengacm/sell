package com.imooc.sell.controller;

import com.imooc.sell.config.ProjectUrlConfig;
import com.imooc.sell.constant.CookieConstant;
import com.imooc.sell.constant.RedisConstant;
import com.imooc.sell.dataobject.SellerInfo;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.service.SellerService;
import com.imooc.sell.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * 买家用户
 */
@Controller
@RequestMapping("/seller")
public class SellerUserController {
    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;



    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                              HttpServletResponse response,
                              Map<String,Object> map) {
        //匹配数据库openid
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if (sellerInfo == null) {
            map.put("msg",ResultEnum.LOGIN_FAIL);
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("/common/error");
        }
        //设置token到redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.explore;
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),openid,expire);
        //设置token到Cookie
        CookieUtil.set(response,CookieConstant.TOKEN,token,CookieConstant.EXPIRE);

        return new ModelAndView("redirect:"+projectUrlConfig.getSell()+"/sell/seller/order/list");
    }

    @GetMapping("logout")
    public ModelAndView logout(HttpServletRequest request,
                       HttpServletResponse response,
                         Map<String,Object> map) {
        //从cookie里查询
        Cookie cookie = CookieUtil.get(request,CookieConstant.TOKEN);
        if (cookie!=null) {
            //清除redis
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
            //清除cookie
            CookieUtil.set(response, CookieConstant.TOKEN,null,0);
        }
        map.put("msg",ResultEnum.LOGOUT_SUCCESS);
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success",map);
    }
}
