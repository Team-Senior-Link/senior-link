package com.cteam.seniorlink.utils;

import java.text.DecimalFormat;

public class NumberUtils {
    public static String formatNumber(double number, String pattern){
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }
}
