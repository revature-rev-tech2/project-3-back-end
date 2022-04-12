package com.project3.revtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class RevtechApplication {

	public static void main(String[] args) {
		SpringApplication.run(RevtechApplication.class, args);
	}
	
}
