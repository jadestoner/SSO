package com.jadeStoneStronger.SSO.base.config;

import lombok.Data;

public class Constants {

	public static final String SESSION_LOGINED_PRE = "SESSION_LOGON_%s"; 
	
	public static final String COOKIE_USER_ID = "USER_ID"; 
	
	public static final String TICKET = "TICKET"; 
	
	public static final String TICKET_PARAM = "TICKET=%s"; 
	
	public static final String COOKIE = "COOKIE"; 
	

	
	
	public static enum PAGE{
		
		LOGIN("login"),
		ERROR("error"),
		P404("404"),
		P500("500");
		
		private String location;
		
		private PAGE(String location) {
			this.location = location;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}
	}
}
