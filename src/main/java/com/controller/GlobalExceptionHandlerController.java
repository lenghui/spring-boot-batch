package com.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 
 * 全局异常捕捉
 * @author ASUS
 *
 */
@ControllerAdvice
public class GlobalExceptionHandlerController {

	@ResponseBody
	@ExceptionHandler(value=Exception.class)
	public String exHandler(Exception e) {
		e.printStackTrace();
		return e.toString();
	}
	
}
