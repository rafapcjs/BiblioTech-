package com.bookLibrary.rafapcjs;

import com.bookLibrary.rafapcjs.security.auth.persistence.model.RoleEntity;
import com.bookLibrary.rafapcjs.security.auth.persistence.model.RoleEnum;

import com.bookLibrary.rafapcjs.user.persistence.entities.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;


@SpringBootApplication(scanBasePackages = "com.bookLibrary.rafapcjs")
public class RafapcjsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RafapcjsApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(com.bookLibrary.rafapcjs.user.persistence.repositories.UserRepository userRepository) {
		return args -> {


			// Crear roles
			RoleEntity roleAdmin = RoleEntity.builder().roleEnum(RoleEnum.ADMIN).build();
			RoleEntity roleUser = RoleEntity.builder().roleEnum(RoleEnum.USER).build();

			// Crear usuarios
			UserEntity userSantiago = UserEntity.builder()
					.username("santiago")
					.password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.build();

			UserEntity userNelson = UserEntity.builder()
					.username("nelss")
					.password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleUser))
					.build();

			userRepository.saveAll(List.of(userSantiago,userNelson));
			System.out.println("Usuarios inicializados correctamente");

		};
	}
}
