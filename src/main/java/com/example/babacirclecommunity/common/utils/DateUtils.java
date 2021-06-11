package com.example.babacirclecommunity.common.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 工具类
 *
 * @Author MQ
 * @Date 2019/8/23
 * @Version 1.0
 */
public class DateUtils {
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String SIMPLE_DATE_FORMAT = "yyyyMMddHHmmss";

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

    private static final DateTimeFormatter simpleDateFormatter = DateTimeFormatter.ofPattern(SIMPLE_DATE_FORMAT);

    public static String formatDateTime(LocalDate date) {
        return dateTimeFormatter.format(date);
    }

    public static String formatDateTime(LocalDateTime date) {
        return dateTimeFormatter.format(date);
    }

    public static String formatSimpleDateTime(LocalDateTime date) {
        return simpleDateFormatter.format(date);
    }

    public static LocalDateTime parseDateTime(String time){ return LocalDateTime.parse(time, dateTimeFormatter); }

    public static String formatDate(LocalDate date) {
        return dateFormatter.format(date);
    }

    public static String formatDate(LocalDateTime date) {
        return dateFormatter.format(date);
    }

    public static LocalDate parseDate(String date){ return LocalDate.parse(date,dateFormatter); }

    /**
     * 得到时间
     * @param hqtime 过去时间戳
     * @return
     */
    public static String getTime(String hqtime){
        BigDecimal add = new BigDecimal(System.currentTimeMillis()/1000).subtract(new BigDecimal(Integer.parseInt(hqtime)));
        Long s =  add.longValue();
        if(s<60) {
            return s+"秒前";
        }else {
            long minute = s/60;
            if(minute<60) {
                return minute+"分钟前";
            }else {
                long hour = minute/60;
                if(hour<24) {
                    return hour+"时前";
                }else {
                    long day = hour/24;
                    if(day<30) {
                        return day+"天前";
                    }else {
                        long month = day/30;
                        if(month<12) {
                            return month+"月前";
                        }else {
                            long year = month/12;
                            return year+"年前";
                        }
                    }

                }
            }
        }
    }

}
