package com.sx.enjoy.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegularUtil {

    //手机号判断 true为通过验证
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        if (str == null) {
            return false;
        }
        if (str.length() != 11) {
            return false;
        }
        String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
