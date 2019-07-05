package com.jadeStoneStronger.SSO.App1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jadeStoneStroner.sso.base.domain.Test;

@RestController
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/v1")
	public void v1() {
		Test test = new Test();
		System.out.println("v1");
	}
}
