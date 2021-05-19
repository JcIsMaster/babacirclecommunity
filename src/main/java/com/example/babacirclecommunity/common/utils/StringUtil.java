package com.example.babacirclecommunity.common.utils;

public class StringUtil {
	
	
	public static  String getlinkNo(String str) {
	    String linkNo = "";
	    // 用字符数组的方式随机
	    String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    char[] m = model.toCharArray();
	    for (int j = 0; j < 6; j++) {
	        char c = m[(int) (Math.random() * 36)];
	        // 保证六位随机数之间没有重复的
	      
	        linkNo = linkNo + c;
	        if (linkNo.equals(str)) {
	        	StringUtil.getlinkNo(str);  
	           
	        }
	    }
	    return linkNo;
	}

	


}
