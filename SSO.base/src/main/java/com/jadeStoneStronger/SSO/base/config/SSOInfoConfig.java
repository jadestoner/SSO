package com.jadeStoneStronger.SSO.base.config;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jadeStoneStronger.SSO.base.utils.CookieUtils;

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
	
	private List<String> supportedApplicationContexts;
	
	
	
	public boolean support(String contextPath) {
		return supportedApplicationContexts.contains(contextPath);
	}
	
	public boolean support(HttpServletRequest request) {
		
		return support(request.getContextPath().substring(1))
				&& CookieUtils.getCookie(request, Constants.TICKET) == null
				;
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		supportedApplicationContexts =Arrays.asList( StringUtils.tokenizeToStringArray(supportedApps, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}
}
