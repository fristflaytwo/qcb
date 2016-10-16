/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年7月28日 下午3:18:05
 */
package com.xionger.qcb.common.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;


/**
 * Copyright (C), 2014-4-22,
 * 
 * @version 1.0
 * @date 2014-4-22 javaBean转化为字符串公共类
 * @author SHENBO
 */
public class FastJsonUtil {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T jsonToBean(String json,Class clazz) {
		return (T) JSON.parseObject(json, clazz);
	}
	
	public static String beanToJson(Object object) {
		return JSON.toJSONString(object);
	}

	public static void main(String[] args) {
		Test1 test = new Test1();
		test.setStudentId(44444);
		
		String jsonString = beanToJson(test); 
		System.out.println(jsonString);
		
		
		String json = "{\"a\":3333,\"b\":444}";
		
		Test1 test1 = jsonToBean(json, Test1.class);
		System.out.println(test1.getStudentId());
		
	}
}
class Test1 {
    private int studentId;
  
    public Test1() {}
 
    @JSONField(name = "Student_Id")
    public int getStudentId() {
        return studentId;
    }
 
    @JSONField(name = "a")
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}