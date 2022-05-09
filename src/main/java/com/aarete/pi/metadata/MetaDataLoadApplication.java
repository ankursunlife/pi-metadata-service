package com.aarete.pi.metadata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableResourceServer
@EnableCaching
@EnableSwagger2

public class MetaDataLoadApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetaDataLoadApplication.class, args);
	}
}