package com.jadeStoneStronger.SSO.base.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtils {

	
	
	
	public static String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies =  request.getCookies();
	    if(cookies != null){
	        for(Cookie cookie : cookies){
	            if(cookie.getName().equals(key)){
	                return cookie.getValue();
	            }
	        }
	    }
	    return null;
	}
	
	
	public Cookie gen(String key, String value) {
		Cookie cookie = new Cookie(key,value);
		return cookie;
	}
	
}
