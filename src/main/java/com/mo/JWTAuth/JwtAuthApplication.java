package com.mo.JWTAuth;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class JwtAuthApplication {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(JwtAuthApplication.class, args);

		ServerInfo serverInfo = ctx.getBean(ServerInfo.class);
		serverInfo.print();
	}

	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	ServerInfo serverInfo() {
		return new ServerInfo();
	}

	private static class ServerInfo {
		@Value("${server.port}")
		private Integer port;

		@Value("${spring.profiles.active:NotSet}")
		private String activeProfile;

		public void print(){
			System.out.println(
					String.format("Service running on port %d \n" +
							"Active profile is %s", port, activeProfile));
		}
	}
}
