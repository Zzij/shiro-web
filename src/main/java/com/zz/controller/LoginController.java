package com.zz.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zz.vo.User;

@Controller
public class LoginController extends BaseController{

	@RequestMapping(value = "subLogin", method = RequestMethod.POST, produces= {"application/json;charset=utf-8"})
	@ResponseBody
	public String login(User user) {
		
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
		
		try {
			//设置是否要记住
			token.setRememberMe(true);
			subject.login(token);
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
		if(subject.isAuthenticated()) {
			return "成功过";
		}
		return "false";
	}
	
	//权限为admin的才返回
	@RequiresRoles("admin")
	@RequestMapping(value = "/testRole", method = RequestMethod.GET)
	@ResponseBody
	public String testRole() {
		return "testRole success";
	}
	
	@RequiresRoles("admin1")
	@RequestMapping(value = "/testRole1", method = RequestMethod.GET)
	@ResponseBody
	public String testRole1() {
		return "testRole1 success";
	}
	
	
	@RequestMapping(value = "/testroles", method = RequestMethod.GET)
	@ResponseBody
	public String testroles() {
		return "testroles success";
	}
	
	@RequestMapping(value = "/testroles1", method = RequestMethod.GET)
	@ResponseBody
	public String testroles1() {
		return "testroles1 success";
	}
	
	@RequestMapping(value = "/testperms", method = RequestMethod.GET)
	@ResponseBody
	public String testPerms() {
		return "testperms success";
	}
	
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public String logout() {
		
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "logout success";
	}
	
	@RequestMapping(value = "/remember", method = RequestMethod.GET)
	@ResponseBody
	public String remember() {
	
		return "remember success";
	}
	
	
}
