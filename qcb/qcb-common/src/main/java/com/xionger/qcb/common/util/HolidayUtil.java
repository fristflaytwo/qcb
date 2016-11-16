package com.xionger.qcb.common.util;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xionger.qcb.common.constants.Constants;
public class HolidayUtil {
	/**
     * @param httpArg
     *            :参数 例如：20160102
     * @return 返回结果 0：工作日 1：休息日 2：节假日
     * 参考地址:http://apistore.baidu.com/apiworks/servicedetail/1116.html
     */
    public static String request( String date) {
        String httpUrl=Constants.BAIDU_API_HOLIDAY;
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?d=" + date;
 
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey",  Constants.BAIDU_API_KEY);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, Constants.UTF8));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            is.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
     
    public static void main(String[] args) {
        //判断今天是否是工作日 周末 还是节假日
        String httpArg="20160206";
        String jsonResult = request(httpArg);
        System.out.println(jsonResult);
    }
}
