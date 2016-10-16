/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年7月28日 下午12:55:12
 */
package com.xionger.qcb.common.util.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Project: trust-common
 * @Author zy
 * @Company: 
 * @Create Time: 2016年7月28日 下午12:55:12
 */
public class Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);
	static ObjectMapper mapper = new ObjectMapper();  
	public static void main(String[] args) {
		
        
		Student s = new Student();
		s.setStudentId(1111);
		
		try {
			System.out.println( mapper.writeValueAsString(s));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		String json = "{\"a\":3333}";
		
		Student ss = readValue(json, Student.class);
		System.out.println(ss.getStudentId());
		
		
	}
	
	public static <T> T readValue(String jsonStr, Class<T> valueType) {
		if (mapper == null) {
			mapper = new ObjectMapper();
		}

		try {
			return mapper.readValue(jsonStr, valueType);
		} catch (Exception e) {
			LOGGER.error("" + e);
		}

		return null;
	}
}

class Student {
    private int studentId;
  
    public Student() {}
 
    @JsonProperty("Student_Id")
    public int getStudentId() {
        return studentId;
    }
 
    @JsonProperty("a")
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}