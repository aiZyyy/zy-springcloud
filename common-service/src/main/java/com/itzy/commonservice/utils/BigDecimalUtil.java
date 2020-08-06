package com.itzy.commonservice.utils;

import java.math.BigDecimal;

/**
 * @Author: ZY
 * @Date: 2019/8/2 15:36
 * @Version 1.0
 */
public class BigDecimalUtil {
    private BigDecimalUtil(){
    }

    /**
     * 相加
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(double v1, double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    /**
     * 相减
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /**
     * 相乘
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    /**
     * 相除
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal div(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);//四舍五入,保留2位小数

        //除不尽的情况
    }
}
