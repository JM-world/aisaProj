package com.aisa.survey;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;



//@SpringBootApplication(exclude=DataSourceAutoConfiguration.class) //(exclude=DataSourceAutoConfiguration.class) : 현재 데이터베이스 필요없음
@SpringBootApplication
public class AsiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsiaApplication.class, args);
		
	}

}
