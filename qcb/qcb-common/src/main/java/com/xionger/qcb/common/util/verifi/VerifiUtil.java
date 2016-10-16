package com.xionger.qcb.common.util.verifi;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xionger.qcb.common.util.string.StringUtil;




/**
 * @Description:对象验证参数
 * @author kx
 * @date 2016年5月26日 上午10:26:52
 * @version v1.0
 */
public class VerifiUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(VerifiUtil.class);

	public static final int FIFTEEM = 15;
	public static final int EIGHTEEM = 18;

	private VerifiUtil() {
	}

	

	public static String vaildation(Object object) {
		StringBuilder result = new StringBuilder();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

		for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
			if (StringUtil.isNotBlank(constraintViolation.getMessage())) {
				result.append(constraintViolation.getPropertyPath() + "=" + constraintViolation.getMessage() + " , ");
			}
		}
		if (result.lastIndexOf(" , ") != -1) {
			return result.substring(0, result.lastIndexOf(" , "));
		}

		return result.toString();
	}

	/**
	 * 身份证校验
	 * 
	 * @return
	 */
	public static String checkIDCard(String value) {
		String message = "";
		String regular = "";

		if (StringUtil.isBlank(value)) {
			message = "身份证格式错误，请检查!";
		} else if (FIFTEEM == value.length()) {// 15位身份证正则表达式
			regular = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{2}[xX\\d]$";
			message = getCheckIDCard(value, regular);
		} else if (EIGHTEEM == value.length()) {// 18位身份证正则表达式
			regular = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[xX\\d]$";
			message = getCheckIDCard(value, regular);
		} else {
			message = "身份证格式错误，请检查!";
		}
		LOGGER.info(message);
		return message;
	}

	private static String getCheckIDCard(String value, String regular) {
		String message = "";
		Pattern pattern = Pattern.compile(regular);
		Matcher matcher = pattern.matcher(value);
		if (!matcher.matches()) {
			message = "身份证格式错误，请检查!";
		}
		return message;
	}

	public static boolean isNumber(String value) {

		if (StringUtil.isBlank(value)) {
			return true;
		}
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher matcher = pattern.matcher(value);

		return !matcher.matches();
	}
	
	/**
	 * 是否不包含汉字，并且长度为30  true：不包含 ； false包含
	 * @param value
	 * @return
	 */
	public static boolean isNotChinese(String value) {

		if (StringUtil.isBlank(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[^\u4e00-\u9fa5]{1,30}$");
		Matcher matcher = pattern.matcher(value);

		return matcher.matches();
	}
	

	public static boolean matcher(String value, String regular) {

		if (StringUtil.isBlank(value)) {
			return false;
		}

		Pattern pattern = Pattern.compile(regular);
		Matcher matcher = pattern.matcher(value);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(isNotChinese("45v@@#2adf"));
		
	}

}
