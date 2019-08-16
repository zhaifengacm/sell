package com.imooc.sell.util;

public class MathUtil {
    private static final Double Money_Range=0.01;
    /**
     * 比较2个金额是否相同
     * @param v1
     * @param v2
     * @return
     */
    public static boolean equal(double v1,double v2) {
        double result = Math.abs(v1-v2);
        if (result < Money_Range) {
            return true;
        }
        return false;
    }
}
