package com.bookLibrary.rafapcjs;

 import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;
 @EnableScheduling
@SpringBootApplication(scanBasePackages = "com.bookLibrary.rafapcjs")

 public class RafapcjsApplication {

	public static void main(String[] args) {


		SpringApplication.run(RafapcjsApplication.class, args);

	}


}
