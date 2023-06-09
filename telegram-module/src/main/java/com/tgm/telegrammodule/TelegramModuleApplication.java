package com.tgm.telegrammodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TelegramModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramModuleApplication.class, args);
	}

}
