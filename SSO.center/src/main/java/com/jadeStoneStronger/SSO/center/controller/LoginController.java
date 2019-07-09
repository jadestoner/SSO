package com.jadeStoneStronger.SSO.center.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jadeStoneStronger.SSO.base.config.Constants;
import com.jadeStoneStronger.SSO.base.config.SSOInfoConfig;
import com.jadeStoneStronger.SSO.base.domain.LoginVo;
import com.jadeStoneStronger.SSO.base.utils.RequestUtils;
import com.jadeStoneStronger.SSO.center.server.LoginService;

@Controller
public class LoginController {

	@Autowired
	private SSOInfoConfig SSOInfoConfig;
	
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private LoginService loginService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/uniformAuthentication")
	public ModelAndView uniformAuthentication(@RequestParam("from")String from, @RequestParam("requestId")String requestId) {
		ModelAndView mav = new ModelAndView();
		if(!SSOInfoConfig.support(from)) {
			mav.setViewName("error");
			return mav;
		}
		
		String loginToken = UUID.randomUUID().toString(); 
		redisTemplate.boundValueOps(loginToken).set(requestId, 60*5, TimeUnit.SECONDS);
		
		mav.addObject("from",from);
		mav.addObject("loginToken", loginToken);
		mav.setViewName("login");
		
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/login")
	public void login(LoginVo vo,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Object requestId = redisTemplate.boundValueOps(vo.getLoginToken()).get();
		if(StringUtils.isEmpty(requestId)) {
			return;
		}
		Object storedRequest = redisTemplate.boundValueOps(requestId).get();
		if(StringUtils.isEmpty(requestId)) {
			return ;
		}
		if(!loginService.loginValidate(vo)) {
			return;
		}
		// 创建session,
		String ST = String.format(Constants.SESSION_LOGINED_PRE,vo.getUsername());
		Map sessionContent = new HashMap();
		request.getSession(true).setAttribute(ST, sessionContent);
		
		// 创建cookie,
		//	先保存一份在redis里
		//	再返回到页面的cookie里
		//	再跳转到登陆前请求的页面
		String ST_2_COOKIE = "ST_".concat(UUID.randomUUID().toString());
		redisTemplate.boundValueOps(ST_2_COOKIE).set(ST);
//		
		response.addCookie(new Cookie(Constants.TICKET, ST_2_COOKIE));
		response.addHeader("location", storedRequest.toString());
		response.setStatus(302);
		
		response.flushBuffer();
	}
	
}
