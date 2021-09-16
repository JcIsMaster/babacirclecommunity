package com.example.babacirclecommunity.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author Administrator
 */
public class ConstantUtil {


	/**
	 * 小程序appid  开发者自己拥有的
	 */
	public static  final String appid="wx6f3fbf1454d85747";


	/**
	 * 小程序秘钥
	 */
	public static final String secret="3d39711670edf5003814761764f0c350";

	/**
	 *商户ID
	 */
	public static final String MCHID="1607486962";

	/**
	 * 商户号秘钥
	 */
	public static final String PATERNERKEY="UmZKqJqkhIO1KY9IBBrVJhqKDlVsGaFo";

	/**
	 * 支付成功后的服务器回调url
	 */
	public static final String notifyUrl = "https://www.gofatoo.com/springbootmybatis/OrdersController/weChatNotify";

	/**
	 * 交易类型
	 */
	public static final String TRADETYPE = "JSAPI";

	/**
	 * 微信统一下单接口地址
	 */
	public static final String payUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 签名方式
	 */
	public static final String SIGNTYPE = "MD5";
	    
		
	    /**
	     * 生成token
	     * @return
	     */
	    public static String getToken(){
	    	 String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx6f3fbf1454d85747"+"&secret=3d39711670edf5003814761764f0c350";
	         String wxJson = new RestTemplate().getForEntity(url, String.class).getBody();
	         JSONObject jsonObject = JSON.parseObject(wxJson);
	         Object accessTokenObj = jsonObject.get("access_token");
	         String token=accessTokenObj.toString().split(",")[0].split(":")[0];
	         return token;
	    }

	/**
	 * 获取access_token
	 * @param appSecret
	 * @param appId
	 */
	public Map<String, String> getAccessToken(String appId,String appSecret){
		Map<String,String> result = new HashedMap();
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx6f3fbf1454d85747"+"&secret=3d39711670edf5003814761764f0c350";
		String wxJson = new RestTemplate().getForEntity(url, String.class).getBody();
		JSONObject parseObject = JSON.parseObject(wxJson);
		System.out.println("accessTokenOut========="+parseObject.toJSONString());

		String errcode = parseObject.getString("errcode");
		String accessToken = parseObject.getString("access_token");
		String expiresIn = parseObject.getString("expires_in");
		Integer restTime = Integer.parseInt(expiresIn);
		//TODO 7200秒之内刷新token
		if (restTime == 0) {
			getAccessToken(appid,secret);
		}
		result.put("accessToken",accessToken);

		//获取access_token码失败
		if (StringUtils.isNotBlank(errcode)) {
			throw new ApplicationException(CodeType.SERVICE_ERROR,"获取token失败");
		}
		return result;
	}




	    
	    
	    /**
	     * 识别文本内容
	     * @param content 文本内容
	     * @return
	     * @throws ParseException 
	     */
	    public static String identifyText(String content,String accessToken) throws ParseException{
	    	
	    	 String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;
	         //创建客户端
	         HttpClient httpclient = HttpClients.createDefault();
	         //创建一个post请求
	         HttpPost request = new HttpPost(url);
	         //设置响应头
	         request.setHeader("Content-Type", "application/json;charset=UTF-8");
	         //通过fastJson设置json数据
	         JSONObject postData = new JSONObject();
	         //设置要检测的内容
	         postData.put("content", content);
	         String jsonString = postData.toString();
	         request.setEntity(new StringEntity(jsonString,"utf-8"));
	         //由客户端执行(发送)请求
	         String returnedValue=null;
	         try {
	             HttpResponse response = httpclient.execute(request);
	             // 从响应模型中获取响应实体
	             HttpEntity entity = response.getEntity();
	             //得到响应结果
	             String result = EntityUtils.toString(entity,"utf-8");
	             //将响应结果变成json
	             JSONObject resultJsonObject = JSONObject.parseObject(result, JSONObject.class);

	             //根据key得到值
	             returnedValue =String.valueOf(resultJsonObject.get("errcode")) ;

	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	    	return returnedValue;
	    }
	    
	    
	    /**
	     * 识别图片内容
	     * @param multipartFile 图片文件
	     * @param accessToken token
	     * @return
	     */
		@SuppressWarnings("unused")
		public static String checkImg(MultipartFile multipartFile, String accessToken)throws Exception {
	    	String valueOf=null;
	        try {

	            CloseableHttpClient httpclient = HttpClients.createDefault();

	            CloseableHttpResponse response = null;

	            HttpPost request = new HttpPost("https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken);
	            request.addHeader("Content-Type", "application/octet-stream");

	            InputStream inputStream = multipartFile.getInputStream();

	            byte[] byt = new byte[inputStream.available()];
	            inputStream.read(byt);
	            request.setEntity(new ByteArrayEntity(byt, ContentType.create("image/jpg")));

	            response = httpclient.execute(request);
	            HttpEntity httpEntity = response.getEntity();
	            // 转成string
	            String result = EntityUtils.toString(httpEntity, "UTF-8");
	            
	            JSONObject jso = JSONObject.parseObject(result);
	            //通过key拿到返回单的值
	            valueOf = String.valueOf(jso.get("errcode"));
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.err.println("----------------调用腾讯内容过滤系统出错------------------");
	        }
			return valueOf;
	    }
	    
		
		public static String getSubstring(String index,String src){
			//截取字符串中的最后一个点的下标
			int indexOf = src.lastIndexOf(".");
			
			//在拿个0到截取出来的下标  在截取出来值
		    String str=  src.substring(0,indexOf);
		    
		    //得到后缀名
		    String substring = src.substring(indexOf);
		    //得到最后拼接的地址
		    String srca=str+index+substring;
		    
		    return srca;
		}
		
		public static String getImg(String[] modes){
			System.out.println(modes.length+"==长度");
			String  srcq="";
			
			TreeMap<Integer, String> stTre=new TreeMap<>();
		    for (int j = 0; j < modes.length; j++) {
				System.out.println("数字=="+j);
		    int index=modes[j].lastIndexOf(".");
		    String x= modes[j].substring(index-1,index);
				stTre.put(Integer.parseInt(x), modes[j]);
				
			}
		    
		    Iterator it = stTre.entrySet().iterator();
		    while (it.hasNext()) {
		     Map.Entry entry = (Map.Entry) it.next();
		     Object key = entry.getKey();
		     String value = entry.getValue().toString();
		     
		     
		     int lastIndexOf = value.lastIndexOf(".");
		     StringBuilder sb = new StringBuilder(value);
			 sb.replace(lastIndexOf-1,lastIndexOf,"");
			 System.out.println("====="+sb);
		     srcq+=sb+",";
				System.out.println("-----------"+srcq);
		    }
		    return srcq;
		}
		
		//得到a和b的百分比  然后转整数
		public static int getInteger(Integer a,Integer b){
			  Double p=0.0;
			  if(a==0){
			   p=0.0;
			   }
			  else{
			   p=a*1.0/b;
			   }
			      int i=0;
			      DecimalFormat df = new DecimalFormat("#.00");  //拼接小数点后两位
			      String c= df.format(p);
			      i=(int) (Double.parseDouble(c)*100);
			   return i;
		}
		
		//得到a和b的百分比  然后转带有百分符号的整数
		public static String getString(Integer a,Integer b ){
			  Double p=0.0;
			  if(a==0){
			   p=0.0;
			   }
			  else{
			   p=a*1.0/b;
			   }
			      int i=0;
			      DecimalFormat df = new DecimalFormat("#.00");  //拼接小数点后两位
			      NumberFormat nf = NumberFormat.getPercentInstance();
			      String ps = nf.format(p);
			   return ps;
		}
	    
	
	    public static String stampToDate(String s){
	        String res;
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        long lt = new Long(s);
	        Date date = new Date(lt);
	        res = simpleDateFormat.format(date);
	        return res;
	    }
	    
	  
	   
	
}
