package com.devices.devicesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DevicesapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevicesapiApplication.class, args);
	}

}
