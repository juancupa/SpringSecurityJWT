package com.example;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.models.ERole;
import com.example.models.RoleEntity;
import com.example.models.UserEntity;
import com.example.repository.UserRepository;

@SpringBootApplication
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);

	}
	@Autowired
	PasswordEncoder passwordEncoder;

	
	@Autowired
	UserRepository userRepository;
	
	@Bean
	CommandLineRunner init() {
		return args ->{
			
			UserEntity userEntity = UserEntity.builder()
				       .email("juan.cupapedroza@gmail.com")
				       .username("juan")
				       .password(passwordEncoder.encode("1234"))
				       .roles(Set.of(RoleEntity.builder()
				    		     .name(ERole.valueOf(ERole.ADMIN.name()))
				    		     .build()))
				       .build();
			
			UserEntity userEntity1 = UserEntity.builder()
				       .email("anyi@gmail.com")
				       .username("anyi")
				       .password(passwordEncoder.encode("1234"))
				       .roles(Set.of(RoleEntity.builder()
				    		     .name(ERole.valueOf(ERole.USER.name()))
				    		     .build()))
				       .build();
			
			UserEntity userEntity2 = UserEntity.builder()
				       .email("santiago@gmail.com")
				       .username("santiago")
				       .password(passwordEncoder.encode("1234"))
				       .roles(Set.of(RoleEntity.builder()
				    		     .name(ERole.valueOf(ERole.INVITED.name()))
				    		     .build()))
				       .build();
			
			userRepository.save(userEntity);
			userRepository.save(userEntity1);
			userRepository.save(userEntity2);
			
		};
	}
}
