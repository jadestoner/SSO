package com.jadeStoneStronger.SSO.App1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jadeStoneStronger.SSO.base.domain.Test;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private RedisTemplate redisTemplate;
	
	@RequestMapping("/v1")
	public void v1() {
		redisTemplate.boundValueOps("v1").set("vvvv");
		Test test = new Test();
		System.out.println("v1");
		System.out.println(redisTemplate.boundValueOps("v1").get());
	}
}
