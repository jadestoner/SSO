package com.jadeStoneStronger.SSO.base.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jadeStoneStronger.SSO.base.config.Constants;
import com.jadeStoneStronger.SSO.base.config.SSOInfoConfig;
import com.jadeStoneStronger.SSO.base.utils.RequestUtils;
import com.jadeStoneStronger.SSO.base.wrapper.RespMessage;

/**
 * 主要功能是，认证ticket的
 * 
 * @author 
 *
 */
@Component
@WebFilter("/*")
@Order(Ordered.LOWEST_PRECEDENCE)
public class TicketFilter extends OncePerRequestFilter  {

	@Autowired
	private SSOInfoConfig SSOInfoConfig;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println(RequestUtils.getRequestURL(request));
		String ticket = request.getParameter(Constants.TICKET);
		if(StringUtils.isNoneBlank(ticket) 
				&& SSOInfoConfig.support(request.getContextPath().substring(1))){
			// 请求认证中心，判断ticket是否正确
			long start = System.currentTimeMillis();
			RespMessage<Map> sessionContext = SSOInfoConfig.serviceValidate(ticket);
			System.out.println("耗时" + (System.currentTimeMillis() - start));
			System.out.println("ticket验证结束");
			if(sessionContext.success() && sessionContext.getContent()!=null) {
				System.out.println("解析ticket");

				Object userName = sessionContext.getContent().get("userName");
				// 创建session,表示sso这边登陆成功
				String ST = String.format(Constants.SESSION_LOGINED_PRE, userName);
				Map sessionContent = new HashMap();
				sessionContent.put("userName", userName);
				// sessionContent里可以放一些其他的东西，比如登陆时间，登陆来源，登陆账号等信息，
				request.getSession(true).setAttribute(ST, sessionContent);
				// 认证成功的情况下，生成一个cookie，
				response.addCookie(new Cookie(Constants.COOKIE, ST));
			}else {
				response.sendError(500, "error");
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}
