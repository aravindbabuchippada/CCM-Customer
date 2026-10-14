package com.ccm.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ccm.*"})
@EntityScan(basePackages = {"com.ccm.customer.entity"})
@EnableFeignClients
public class CcmCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CcmCustomerApplication.class, args);
	}

}
