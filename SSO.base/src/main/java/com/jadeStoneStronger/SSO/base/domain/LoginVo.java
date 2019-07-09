package com.jadeStoneStronger.SSO.base.domain;

import lombok.Data;

@Data
public class LoginVo {

	private String username;
	private String password;
	
	private String from;
	private String loginToken;

	
	
}
