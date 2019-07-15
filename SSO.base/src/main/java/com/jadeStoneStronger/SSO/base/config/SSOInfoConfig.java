package com.jadeStoneStronger.SSO.base.config;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.expression.Maps;

import com.jadeStoneStronger.SSO.base.utils.CookieUtils;
import com.jadeStoneStronger.SSO.base.wrapper.RespMessage;

import lombok.Data;

/**
 * 单点登陆相关信息配置类
 * @author hongxu
 *
 */
@Component
@Configuration
@Data
public class SSOInfoConfig implements InitializingBean {

	@Value("${sso.center.login.url}")
	private String login_url;
	
	@Value("${sso.center.supported.apps}")
	private String supportedApps;
	
	@Value("${sso.center.serviceValidate.url}")
	private String serviceValidate_url;
	
	private List<String> supportedApplicationContexts;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	public RespMessage serviceValidate(String ticket) {
		System.out.println("ticket验证：" + ticket);
		RespMessage<?> result = restTemplate.getForObject(String.format(serviceValidate_url, ticket), RespMessage.class, new HashMap());
		return result;
	}
	
	public boolean support(String contextPath) {
		return supportedApplicationContexts.contains(contextPath);
	}
	
	public boolean support(HttpServletRequest request) {
		
		// 在配置的app列表中
		return support(request.getContextPath().substring(1))
				// 非首页
				&& request.getServletPath().length() > 1
				// ticket为空，ticket阶段是服务验证的阶段
				&& CookieUtils.getCookie(request, Constants.TICKET) == null
				// 有的ticket是放置在url上的
				&& request.getParameter(Constants.TICKET) == null
				// cookie和session，已登录阶段
				&&  request.getSession().getAttribute(CookieUtils.getCookie(request, Constants.COOKIE)) == null
						
				;
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		supportedApplicationContexts =Arrays.asList( StringUtils.tokenizeToStringArray(supportedApps, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}
}
