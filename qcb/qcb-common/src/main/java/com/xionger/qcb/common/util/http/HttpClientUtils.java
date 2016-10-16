package com.xionger.qcb.common.util.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.string.StringUtil;




public class HttpClientUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);
	
		private static ConnectionSocketFactory plainsf =null;
		private static LayeredConnectionSocketFactory sslsf = null;
		private static Registry<ConnectionSocketFactory> registry = null;
		private static PoolingHttpClientConnectionManager cm =null;
	     
		private static HttpRequestRetryHandler httpRequestRetryHandler=null;
		
		private static RequestConfig requestConfig=null;
		
		private static CloseableHttpClient httpClient=null;
	     
	     static {  
	    	   plainsf = PlainConnectionSocketFactory.getSocketFactory();
		       sslsf = SSLConnectionSocketFactory.getSocketFactory();
		       registry = RegistryBuilder.<ConnectionSocketFactory>create()
		                .register("http", plainsf)
		                .register("https", sslsf)
		                .build();
		        cm = new PoolingHttpClientConnectionManager(registry);
		        // 最大连接数
		        cm.setMaxTotal(Constants.HTTP_MAX_TOTAL);
		        // 每个路由基础的连接
		        cm.setDefaultMaxPerRoute(Constants.HTTP_DEFAULT_MAX_PERROUTE);
		        //请求重试处理
		         httpRequestRetryHandler = new HttpRequestRetryHandler() {
		            public boolean retryRequest(IOException exception,int executionCount, HttpContext context) {
		                if (executionCount >=Constants.HTTP_EXECUTION_COUNT) {// 如果已经重试了5次，就放弃                    
		                    return false;
		                }
		                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试                    
		                    return true;
		                }
		                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常                    
		                    return false;
		                }                
		                if (exception instanceof InterruptedIOException) {// 超时                    
		                    return false;
		                }
		                if (exception instanceof UnknownHostException) {// 目标服务器不可达                    
		                    return false;
		                }
		                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝                    
		                    return false;
		                }
		                if (exception instanceof SSLException) {// ssl握手异常                    
		                    return false;
		                }
		                 
		                HttpClientContext clientContext = HttpClientContext.adapt(context);
		                HttpRequest request = clientContext.getRequest();
		                // 如果请求是幂等的，就再次尝试
		                if (!(request instanceof HttpEntityEnclosingRequest)) {                    
		                    return true;
		                }
		                return false;
		            }
		        }; 
		        
		       requestConfig = RequestConfig.custom()
		                .setConnectionRequestTimeout(Constants.HTTP_TIME_OUT)
		                .setConnectTimeout(Constants.HTTP_TIME_OUT)
		                .setSocketTimeout(Constants.HTTP_TIME_OUT)
		                .build();
		       
		       httpClient = HttpClients.custom()
		                .setConnectionManager(cm)
		                .setRetryHandler(httpRequestRetryHandler)
		                .build();
		        
	     }
	     
	  /**
	   		get请求
	   * @param url    访问url
	   * @param resultEnc  返回字符编码方式 默认编码是utf-8
	   * @return
	   */
	    public static String doGet(String url,String resultEnc){
	    	String result=null;
	    	CloseableHttpResponse  response=null;
	        HttpGet httpget = new HttpGet(url);
	        try {
	        	resultEnc=StringUtil.isBlank(resultEnc)?Constants.UTF8:resultEnc;
	        	httpget.setConfig(requestConfig);
	        	response = httpClient.execute(httpget,HttpClientContext.create());
	        	result=EntityUtils.toString(response.getEntity(), resultEnc);
			} catch (Exception e) {
				LOGGER.error("get请求 doPost",e);
				 return result;
			}finally{
				if(null!=response){
					try {
						response.close();
					} catch (IOException e) {
						LOGGER.error("get请求 doPost  IOException",e);
					}
				}
				httpget.abort();
			}
	        return result;
	    }
	    
   /**
   		get请求
   * @param url    访问url
   * @param resultEnc  返回字符编码方式 默认编码是utf-8
   * @return
   */
    public static byte[] doGetReturnBytes(String url,String resultEnc){
    	byte[] result =null;
    	CloseableHttpResponse  response=null;
        HttpGet httpget = new HttpGet(url);
        try {
        	resultEnc=StringUtil.isBlank(resultEnc)?Constants.UTF8:resultEnc;
        	httpget.setConfig(requestConfig);
        	response = httpClient.execute(httpget,HttpClientContext.create());
        	result= EntityUtils.toByteArray(response.getEntity());
		} catch (Exception e) {
			LOGGER.error("get请求 doPost",e);
		}finally{
			if(null!=response){
				try {
					response.close();
				} catch (IOException e) {
					LOGGER.error("get请求 doGetReturnBytes  IOException",e);
				}
			}
			httpget.abort();
		}
        return result;
        
    }
	    
	    
	    /**
   				post请求
		   * @param url    访问url
		   * @param resultEnc  返回字符编码方式  默认编码是utf-8
		   * @param params  请求参数
		   * @return
		   */
	    public static String doPost(String url,String resultEnc,Map<String, String> params){
	    	 String result=null;
	    	 CloseableHttpResponse  response=null;
	    	 HttpPost httpPost = new HttpPost(url);
	    	 try {
	    		resultEnc=StringUtil.isBlank(resultEnc)?Constants.UTF8:resultEnc;
	    		List<NameValuePair> nvps =buildNameValuePair(params);
	    		EntityBuilder builder = EntityBuilder.create();
	    		builder.setParameters(nvps);
	    		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Constants.UTF8));
	    		response = httpClient.execute(httpPost,HttpClientContext.create());
	    		result=EntityUtils.toString(response.getEntity(), resultEnc);
			} catch (Exception e) {
				LOGGER.error("post请求 doPost",e);
				 return result;
			}finally{
				if(null!=response){
					try {
						response.close();
					} catch (IOException e) {
						LOGGER.error("post请求 doPost  IOException",e);
					}
				}
				
				httpPost.abort();
			}
	    	 return result;
	    }
	    
	    /**
	     * MAP类型数组转换成NameValuePair类型
	     * 
	     */
	    public static List<NameValuePair> buildNameValuePair(Map<String, String> params) {
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	        if (params != null) {
	        	StringBuilder sb = new StringBuilder();
	            for (Map.Entry<String, String> entry : params.entrySet()) {
	                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	                if(!"password".equals(entry.getKey()) && !"otherInfo".equals(entry.getKey())) {
	                	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
	                }
	            }
	            LOGGER.debug("HttpClient请求参数【" + sb.toString()+ "】");
	        }

	        return nvps;
	    }
	    
}
