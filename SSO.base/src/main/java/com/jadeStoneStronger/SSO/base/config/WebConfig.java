package com.jadeStoneStronger.SSO.base.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
		.addMapping("/**")  
        .allowedOrigins("*")  
        .allowCredentials(true)  
        .allowedMethods("GET", "POST","OPTIONS")  
        .maxAge(3600);
	}
	
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(new HandlerExceptionResolver(){
			@Override
			public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
				ModelAndView mv = new ModelAndView();
				mv.setViewName(Constants.PAGE.ERROR.getLocation());
				mv.setStatus(HttpStatus.BAD_GATEWAY);
				mv.addObject("errorMsg", ex.getMessage());
				return mv;
			}
		});
	}
}
