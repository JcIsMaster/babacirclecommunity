package com.example.babacirclecommunity.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
* @author MQ
* @version 创建时间：2020年11月30日 上午11:06:12
* @ClassName 类名称:
* @Description 类描述: 得到当前时间和过去时间相隔多少天
*/
public class TimeUtil {




	/**
	 * 得到相隔的天数
	 * @param time 过去时间
	 * @return
	 */
	public static int getNumberDaysApart(String time){
		long temp = (long)Integer.parseInt(time)*1000;
		Timestamp ts = new Timestamp(temp);
		String tsStr = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
		     //方法一
		     tsStr = dateFormat.format(ts);
		    } catch (Exception e) {
		         e.printStackTrace();
		}

		//设置转换的日期格式
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   String format = sdf.format(new Date());

	    //开始时间
		Date startDate=null;
		Date endDate =null;
		try {
			startDate = sdf.parse(tsStr);
			//结束时间
			endDate = sdf.parse(format);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//得到相差的天数 betweenDate
		long betweenDate = (endDate.getTime() - startDate.getTime())/(60*60*24*1000);
		//打印控制台相差的天数
		return (int) betweenDate;
	}


	/**
	 * 得到相隔的分钟
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static long getMinutesApart(String time) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long NTime =(long)Integer.parseInt(time)*1000;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 	   	String format = sdf.format(new Date());
        //从对象中拿到时间
        long OTime = df.parse(format).getTime();
        long diff=(OTime-NTime)/1000/60;
		return diff;
	}

	public static boolean getThisTime(long time) {
		Date date1 = new Date();
		String time1 = new SimpleDateFormat("yyyy-MM-dd").format(date1);
		String format = longToDate(time, "yyyy-MM-dd");
        return time1.equals(format);
	}



	public static String longToDate(long times, String formatDate) {
		// "yyyy-MM-dd HH:mm:ss"
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		Long time = new Long(times*1000);
		String d = format.format(time);
		return d;
	}

	
	 
	 

}
