package com.xionger.qcb.main.controll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * 
 *登录处理控制层
 * @author dx
 * 
 */
@Controller
public class LoginController extends BaseController {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	 
	/**
	 * 跳转登录页面
	 * @return
	 */
	@RequestMapping("/login")
	
	public String login(){
		
		return "login";
	}
	
	/**
	 * 忘记密码
	 * @param model
	 * @return
	 */
	@RequestMapping("/forgetpwd")
	public String userFoot(){
		LOGGER.debug("==========>密码");
		return "/forgetpwd";
	}
	
	
}
