package com.jadeStoneStronger.SSO
.App1;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ComponentScan(value="com.jadeStoneStronger.SSO")
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication app = new SpringApplication(App.class);
	    app.setBannerMode(Banner.Mode.OFF);
	    app.run(args);
    }
}
