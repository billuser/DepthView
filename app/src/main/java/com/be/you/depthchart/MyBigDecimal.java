package com.be.you.depthchart;


import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MyBigDecimal {
    public static String getValue(String data){
        String value = new BigDecimal(data).toPlainString();
        return subZeroAndDot(value);
    }

    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static String twoDecimal(double data){
        DecimalFormat df = new DecimalFormat("#.##");
        return  df.format(data);
    }

    public static String twoDecimal(float data){
        DecimalFormat df = new DecimalFormat("#.##");
        return  df.format(data);
    }
}
