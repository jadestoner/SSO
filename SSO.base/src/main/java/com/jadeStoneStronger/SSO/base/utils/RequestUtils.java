package com.jadeStoneStronger.SSO.base.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

	
	public static String addParam(String uri,String key,String value) {
		if(uri.indexOf("?")<0) {
			uri = uri.concat("?");
		}else {
			uri = uri.concat("&");
		}
		return uri.concat(key).concat("=").concat(value);
	}
	
	/**
	 * http://localhost:8080/appname/servletname/actionname?params=value1
	 * 返回 http://localhost:8080/appname/
	 * 
	 * @param request
	 * @return
	 */
	public static StringBuffer getHomePage(HttpServletRequest request) {
        StringBuffer url = new StringBuffer();
        String scheme = request.getScheme();
        int port = request.getServerPort();
        if (port < 0) {
            // Work around java.net.URL bug
            port = 80;
        }

        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80))
            || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(request.getContextPath());

        return url;
    }
	
	 public static StringBuffer getRequestURL(HttpServletRequest request) {
	        StringBuffer url = new StringBuffer();
	        String scheme = request.getScheme();
	        int port = request.getServerPort();
	        if (port < 0) {
	            // Work around java.net.URL bug
	            port = 80;
	        }

	        url.append(scheme);
	        url.append("://");
	        url.append(request.getServerName());
	        if ((scheme.equals("http") && (port != 80))
	            || (scheme.equals("https") && (port != 443))) {
	            url.append(':');
	            url.append(port);
	        }
	        url.append(request.getRequestURI());

	        return url;
	    }
}
