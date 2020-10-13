package com.whu.checky.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    //获取下一个月的日期
    public static String nextMonth(){
        Date date = new Date();
        int year=Integer.parseInt(new SimpleDateFormat("yyyy").format(date));//取到年份值
        int month=Integer.parseInt(new SimpleDateFormat("MM").format(date))+1;//取到鱼粉值
        int day=Integer.parseInt(new SimpleDateFormat("dd").format(date));//取到天值
        if(month==0){
            year-=1;month=12;
        }
        else if(day>28){
            if(month==2){
                if(year%400==0||(year %4==0&&year%100!=0)){
                    day=29;
                }else day=28;
            }else if((month==4||month==6||month==9||month==11)&&day==31)
            {
                day=30;
            }
        }
        String y = year+"";String m ="";String d ="";
        if(month<10) m = "0"+month;
        else m=month+"";
        if(day<10) d = "0"+day;
        else d = day+"";
        System.out.println(y+"-"+m+"-"+d);
        return y+"-"+m+"-"+d;
    }
    //判断输入时间是否在七天前
    public static boolean checkIsBeforeWeek(String time)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //过去七天
        c.setTime(new Date());
        c.add(Calendar.DATE, - 7);
        Date d = c.getTime();
        String day = format.format(d);
        long DateWeekBefor = Long.valueOf(day.replaceAll("[-\\s:]",""));
        long Longtime = Long.valueOf(time.replaceAll("[-\\s:]",""));
        return DateWeekBefor>Longtime;
    }
    //将check_fre转换为一周应该打卡的次数
    public static int transferCheckFre(String checkFre)
    {
        return checkFre.length()-checkFre.replace("1","").length();
    }
    //判断是否在一年以内
    public static boolean checkIsInOneYear(String time)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        String year = format.format(y);
        String[] ymd = time.split("-");
        return Integer.parseInt(ymd[0])>=Integer.parseInt(year);
    }


}
