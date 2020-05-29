package com.saini.rclone;

import com.saini.rclone.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SwaggerConfiguration.class)
public class SpringMicroApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMicroApplication.class, args);
	}

}
