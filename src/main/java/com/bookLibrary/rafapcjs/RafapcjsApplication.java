package com.bookLibrary.rafapcjs;

import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
public class RafapcjsApplication {

	public static void main(String[] args) {


		SpringApplication.run(RafapcjsApplication.class, args);

	}


}
