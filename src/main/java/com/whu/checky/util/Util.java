package com.whu.checky.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {
    private static final Logger log = LoggerFactory.getLogger(Util.class);

    public static boolean judgeDate(String ymd){
        try {
            Calendar serverCalendar = Calendar.getInstance();
            serverCalendar.setTime(new Date());

            Date userDate = MyConstants.DATE_FORMAT.parse(ymd);
            Calendar userCalendar = Calendar.getInstance();
            userCalendar.setTime(userDate);

            if (userCalendar.get(Calendar.YEAR) == serverCalendar.get(Calendar.YEAR) &&
                    userCalendar.get(Calendar.MONTH) == serverCalendar.get(Calendar.MONTH) &&
                    userCalendar.get(Calendar.DAY_OF_MONTH) == serverCalendar.get(Calendar.DAY_OF_MONTH))
                return true;
            else {
                return false;
            }
        } catch (ParseException ex) {
            log.error("Parse date error!\t"+ ex.getMessage());
            return false;
        }
    }
}
