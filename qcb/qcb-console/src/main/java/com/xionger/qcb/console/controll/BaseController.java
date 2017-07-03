package com.xionger.qcb.console.controll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xionger.qcb.common.enums.ResultEnum;
import com.xionger.qcb.common.exception.BusinessException;
import com.xionger.qcb.model.vo.ResultVo;





public class BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public ResultVo handleIOException(BusinessException e) {
		LOGGER.warn(e.getMessage(), e);
		ResultVo rv= new ResultVo(true,ResultEnum.RESULT_CODE_9999);
		
		return rv;
	}
}

