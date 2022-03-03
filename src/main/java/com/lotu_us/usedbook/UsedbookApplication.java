package com.lotu_us.usedbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class UsedbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsedbookApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}
	//security config에 추가하려했지만 순환참조가 발생하여 여기에 작성한다.
}
