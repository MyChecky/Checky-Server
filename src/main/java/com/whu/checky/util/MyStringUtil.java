package com.whu.checky.util;

import java.util.UUID;

public class MyStringUtil {

    public static boolean isEmpty(String a) {
        if(a==null || a.length()==0)
            return true;
        else
            return false;
    }

    public static String getUUID( ) throws Exception {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase();
    }
}
