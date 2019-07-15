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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jadeStoneStronger.SSO.base.config.Constants;
import com.jadeStoneStronger.SSO.base.config.SSOInfoConfig;
import com.jadeStoneStronger.SSO.base.domain.LoginVo;
import com.jadeStoneStronger.SSO.base.utils.CookieUtils;
import com.jadeStoneStronger.SSO.base.utils.RequestUtils;
import com.jadeStoneStronger.SSO.base.wrapper.MsgWrapper;
import com.jadeStoneStronger.SSO.base.wrapper.RespMessage;
import com.jadeStoneStronger.SSO.center.server.LoginService;

@Controller
public class LoginController {
	
	/**
	 * 验证ticket的规则也很简单，存在即是有效
	 */
	Map ticketInfos = new HashMap();

	@Autowired
	private SSOInfoConfig SSOInfoConfig;
	
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private LoginService loginService;
	
	/**
	 *    统一认证入口，重定向到sso的登陆页面
	 * @param from
	 * @param requestId
	 * @return
	 */
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
	
	/**
	 *  登陆，登陆成功后，重定向到应用的ticket认证接口
	 * 
	 * @param vo
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/login")
	public void login(LoginVo vo,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String cookie_logined = CookieUtils.getCookie(request, Constants.COOKIE);
		//cas已经登陆过,判断session是否有效
		if(!StringUtils.isEmpty(cookie_logined)) {
			Map sessionContent = (Map)request.getSession().getAttribute(cookie_logined);
			// 一些逻辑判断，这里简单的判断是否存在
			if(sessionContent != null) {
				// 生成一个ticket，给登陆者使用
				String ticket = UUID.randomUUID().toString();
				ticketInfos.put(ticket, sessionContent);
				Object requestId = redisTemplate.boundValueOps(vo.getLoginToken()).get();
				if(StringUtils.isEmpty(requestId)) {
					return;
				}
				Object storedRequest = redisTemplate.boundValueOps(requestId).get();
				if(StringUtils.isEmpty(requestId)) {
					return ;
				}
				response.addHeader("location", RequestUtils.addParam(storedRequest.toString(),Constants.TICKET,ticket));
				response.setStatus(302);
				response.flushBuffer();
				return;
			}
			
		}
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
		// 创建session,表示sso这边登陆成功
		String ST = String.format(Constants.SESSION_LOGINED_PRE,vo.getUsername());
		Map sessionContent = new HashMap();
		sessionContent.put("userName", vo.getUsername());
		// sessionContent里可以放一些其他的东西，比如登陆时间，登陆来源，登陆账号等信息，
		request.getSession(true).setAttribute(ST, sessionContent);
		// 再生成一个ticket作为子应用的登陆凭证
		String ticket = UUID.randomUUID().toString();
		ticketInfos.put(ticket, sessionContent);
		
		// 创建cookie,
		//	先保存一份在redis里
		//	再返回到页面的cookie里
		//	再跳转到登陆前请求的页面
		// COOKIE的value是session的key,再次访问的时候简单判断一下是否存在就好了
		response.addCookie(new Cookie(Constants.COOKIE, ST));
		//重定向到各应用的服务认证接口
		System.out.println(RequestUtils.addParam(storedRequest.toString(),Constants.TICKET,ticket));
		response.addHeader("location", RequestUtils.addParam(storedRequest.toString(),Constants.TICKET,ticket));
		response.setStatus(302);
		
		response.flushBuffer();
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/serviceValidate")
	public @ResponseBody RespMessage<?> serviceValidate(
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Object ticket = request.getParameter(Constants.TICKET);
		System.out.println("loginControlelr.serviceValidate, ticket= " + ticket);
		if(ticket != null) {
			Object sessionContext = ticketInfos.remove(ticket.toString());
			
			if(sessionContext == null) {
				return MsgWrapper.error("无效的ticket");
			}
			System.out.println(sessionContext.toString());
			return MsgWrapper.success(sessionContext);
		}
		return MsgWrapper.error("无效的ticket");
	}
	
}
