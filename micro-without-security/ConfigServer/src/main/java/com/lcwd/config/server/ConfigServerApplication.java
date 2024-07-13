package com.lcwd.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;



@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
		System.out.println("ConfigServerApplication Started !!!");
	}

}

// 	http://desktop-9m84vob:8085/application/default
//	http://desktop-9m84vob:8085/application/dev
//	http://desktop-9m84vob:8085/application/prod