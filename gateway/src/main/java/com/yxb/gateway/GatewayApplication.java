package com.yxb.gateway;

import com.yxb.gateway.filters.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	/*
	* 将自定义的globalFilter类型的tokenFilter注入Spring Ioc
	* */
	@Bean
	public TokenFilter tokenFilter(){
		return new TokenFilter();
	}
}
