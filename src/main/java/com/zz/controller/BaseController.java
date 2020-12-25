package com.zz.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.OK)
	@ResponseBody
	public String excptionHandle(HttpServletRequest req, Exception e) {
		return e.getMessage();
	}
}
