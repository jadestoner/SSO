package com.jadeStoneStronger.SSO.App1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jadeStoneStronger.SSO.base.domain.LoginVo;

@RestController
public class LoginController {

	/**
	 * 接受认证中心的ticket
	 * @param vo
	 */
	@RequestMapping("/login")
	public void login(LoginVo vo) {
		
		// 请求认证中心，判断ticket是否正确
		
		
		//给前端页面返回一个cookie，表示登陆成功，并重定向到之前要访问的页面，访问时带上cookie
		
	}
	
}
