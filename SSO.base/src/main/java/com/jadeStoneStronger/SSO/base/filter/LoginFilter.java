package com.jadeStoneStronger.SSO.base.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jadeStoneStronger.SSO.base.config.Constants;
import com.jadeStoneStronger.SSO.base.config.SSOInfoConfig;
import com.jadeStoneStronger.SSO.base.utils.CookieUtils;
import com.jadeStoneStronger.SSO.base.utils.RequestUtils;

@Component
@WebFilter("/*")
public class LoginFilter extends OncePerRequestFilter  {
	
	@Autowired
	private SSOInfoConfig SSOInfoConfig;
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String USER_ID = CookieUtils.getCookie(request, Constants.COOKIE_USER_ID);
		if(StringUtils.isEmpty(USER_ID) || request.getSession().getAttribute(USER_ID) == null) {
			if(SSOInfoConfig.support(request)) {
				String id = UUID.randomUUID().toString();
				redisTemplate.boundValueOps(id).set(RequestUtils.getRequestURL(request));
				response.sendRedirect(String.format(SSOInfoConfig.getLogin_url(),request.getContextPath().substring(1),id));
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}
