package com.example.babacirclecommunity.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * ffmpeg工具类
 * @author MQ
 */
public class FfmpegUtil {
	
	/**
	 * 得到视频封面
	 * @param videoUrl 视屏地址
	 * @return
	 * @throws IOException
	 */
	public static String getVideoCover(String videoUrl) throws IOException{
		   List<String> commend = new ArrayList<String>();
		   //服务器地址
		   //C:\Users\Administrator\Desktop\ffmpeg-N-99816-g3da35b7cc7-win64-lgpl\bin\\ffmpeg.exe
		   //本地地址
		   //D:\\ffmpeg\\ffmpeg-N-99816-g3da35b7cc7-win64-lgpl\\bin\\ffmpeg.exe

		   String a=System.currentTimeMillis()/1000+""+StringUtil.getlinkNo("a");
		   String address="";
		 
		   commend.add("C:\\Users\\Administrator\\Desktop\\ffmpeg-N-99816-g3da35b7cc7-win64-lgpl\\bin\\ffmpeg.exe");
		   commend.add("-i");
		   commend.add(videoUrl);  //视屏地址
		   commend.add("-r");
		   commend.add("24");
		   commend.add("-ss");
		   commend.add("00:00:02");
		   commend.add("-t");
		   commend.add("00:00:03");
		   commend.add("-vf");
		   commend.add("scale=295:400");
		   commend.add("E:\\file\\img\\"+a+".png"); //截取的图片放的位置
		   
		   ProcessBuilder builder = new ProcessBuilder();
		   builder.command(commend);
		   Process process = builder.start();
		   
	
		   address= "https://www.gofatoo.com/img/"+a+".png";
	         
		   try {
	            //获取进程的标准输入流
	            final InputStream is1 = process.getInputStream();
	            //获取进城的错误流
	            final InputStream is2 = process.getErrorStream();
	            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
	            new Thread() {
	                public void run() {
	                    BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
	                    try {
	                        String line1 = null;
	                        while ((line1 = br1.readLine()) != null) {
	                            if (line1 != null){}
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    finally{
	                        try {
	                            is1.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }.start();

	            new Thread() {
	                public void  run() {
	                    BufferedReader br2 = new  BufferedReader(new  InputStreamReader(is2));
	                    try {
	                        String line2 = null ;
	                        while ((line2 = br2.readLine()) !=  null ) {
	                            if (line2 != null){}
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    finally{
	                        try {
	                            is2.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }.start();

	            process.waitFor();// 等待进程执行结束
			
	        }catch (Exception ex){
	            ex.printStackTrace();
	            try{
	                process.getErrorStream().close();
	                process.getInputStream().close();
	                process.getOutputStream().close();
	            }
	            catch(Exception ee){}
	        }
		   
		return address;
	
	

 
}
	
	
	
	/**
	 * 
	 * @param imgUrl 图片地址
	 * @return
	 * @throws IOException
	 * 
	 */
	public static void getCompressImg(String imgUrl,String uploadUrl) throws IOException{
		   List<String> commend = new ArrayList<String>();
		   //服务器地址
		   //C:\\Users\\Administrator\\Desktop\\ffmpeg-N-99816-g3da35b7cc7-win64-lgpl\\bin\\ffmpeg.exe
		   //本地地址
		   //D:\\ffmpeg\\ffmpeg-N-99816-g3da35b7cc7-win64-lgpl\\bin\\ffmpeg.exe
		   
		   //String a=System.currentTimeMillis()/1000+""+StringUtil.getlinkNo("a");
		   String address="";
		   System.out.println(uploadUrl);
		  
		  // ffmpeg -i in.png -i tmp.png -lavfi "[0][1:v] paletteuse" -pix_fmt pal8 -y out.png
		   commend.add("C:\\Users\\Administrator\\Desktop\\ffmpeg-N-99816-g3da35b7cc7-win64-lgpl\\bin\\ffmpeg.exe"); 
		   commend.add("-i");
		   commend.add(imgUrl);  //图片地址
		   commend.add("-i");
		   commend.add(getCompressImgColor(imgUrl));
		   commend.add("-lavfi");
		   commend.add("[0][1:v] paletteuse");
		   commend.add("-pix_fmt");
		   commend.add("pal8");
		   commend.add("-y");
		   commend.add(uploadUrl);  //保存图片路劲
		   ProcessBuilder builder = new ProcessBuilder();
		   builder.command(commend);
		   Process process = builder.start();
	
		   
		   try {
	            //获取进程的标准输入流
	            final InputStream is1 = process.getInputStream();
	            //获取进城的错误流
	            final InputStream is2 = process.getErrorStream();
	            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
	            new Thread() {
	                public void run() {
	                    BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
	                    try {
	                        String line1 = null;
	                        while ((line1 = br1.readLine()) != null) {
	                            if (line1 != null){}
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    finally{
	                        try {
	                            is1.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }.start();

	            new Thread() {
	                public void  run() {
	                    BufferedReader br2 = new  BufferedReader(new  InputStreamReader(is2));
	                    try {
	                        String line2 = null ;
	                        while ((line2 = br2.readLine()) !=  null ) {
	                            if (line2 != null){}
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    finally{
	                        try {
	                            is2.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }.start();

	            process.waitFor();// 等待进程执行结束
	 
	        }catch (Exception ex){
	            ex.printStackTrace();
	            try{
	                process.getErrorStream().close();
	                process.getInputStream().close();
	                process.getOutputStream().close();
	            }
	            catch(Exception ee){}
	        }

		  
}
	
	public static String getCompressImgColor(String imgUrl) throws IOException{
		   List<String> commend = new ArrayList<String>();
		   String address="E:\\file\\color\\"+System.currentTimeMillis()/1000+".jpg";
		   
		   commend.add("C:\\Users\\Administrator\\Desktop\\ffmpeg-N-99816-g3da35b7cc7-win64-lgpl\\bin\\ffmpeg.exe"); 
		   commend.add("-i");
		   commend.add(imgUrl);  //图片地址
		   commend.add("-vf");
		   commend.add("palettegen=max_colors=256:stats_mode=single");
		   commend.add("-y");
		   commend.add(address);  //保存图片路劲
		   ProcessBuilder builder = new ProcessBuilder();
		   builder.command(commend);
		   Process process = builder.start();
	
		   
		   try {
	            //获取进程的标准输入流
	            final InputStream is1 = process.getInputStream();
	            //获取进城的错误流
	            final InputStream is2 = process.getErrorStream();
	            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
	            new Thread() {
	                public void run() {
	                    BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
	                    try {
	                        String line1 = null;
	                        while ((line1 = br1.readLine()) != null) {
	                            if (line1 != null){}
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    finally{
	                        try {
	                            is1.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }.start();

	            new Thread() {
	                public void  run() {
	                    BufferedReader br2 = new  BufferedReader(new  InputStreamReader(is2));
	                    try {
	                        String line2 = null ;
	                        while ((line2 = br2.readLine()) !=  null ) {
	                            if (line2 != null){}
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    finally{
	                        try {
	                            is2.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }.start();

	            process.waitFor();// 等待进程执行结束
	 
	        }catch (Exception ex){
	            ex.printStackTrace();
	            try{
	                process.getErrorStream().close();
	                process.getInputStream().close();
	                process.getOutputStream().close();
	            }
	            catch(Exception ee){}
	        }
		return address;

		  
}
	
	}


