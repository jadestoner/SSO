package com.jadeStoneStronger.SSO.base.utils;

import javax.servlet.http.HttpServletRequest;

import com.jadeStoneStronger.SSO.base.config.Constants;

public class RequestUtils {

	
	public static String addParam(String uri,String key,String value) {
		if(uri.indexOf("?")<0) {
			return uri.concat("?").concat(String.format(Constants.TICKET_PARAM, value));
		}
		return uri.concat(String.format(Constants.TICKET_PARAM, value));
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
