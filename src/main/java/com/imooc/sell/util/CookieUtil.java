package com.imooc.sell.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {
    /**
     * 设置Cookie
     * @param response
     * @param name
     * @param val
     * @param maxAge
     */
    public static void set(HttpServletResponse response,
                           String name,
                           String val,
                           int maxAge) {
        Cookie cookie = new Cookie(name,val);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);

    }

    /**
     * 获取cookie
     * @param request
     * @param name
     * @return
     */
    public static Cookie get(HttpServletRequest request,
                           String name) {
        Map<String, Cookie> map = new HashMap<>();
        map = readCookieMap(request);
        if (map.containsKey(name)) {
            return map.get(name);
        }
        return null;
    }

    /**
     * 将cookie封装称map
     * @param request
     * @return
     */
    public static Map<String,Cookie> readCookieMap(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Map<String, Cookie> map = new HashMap<>();
        if (cookies!=null) {
            for (Cookie cookie : cookies) {
                map.put(cookie.getName(),cookie);
            }
        }
        return map;
    }

}
