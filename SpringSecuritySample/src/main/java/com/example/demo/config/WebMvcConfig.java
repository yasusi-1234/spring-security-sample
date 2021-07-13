package com.example.demo.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	public FlywayMigrationStrategy strategy() {
		return flyway -> {
			// flyway_schema_historyの初期化
			flyway.clean();
			// マイグレーション実行
			flyway.migrate();
		};
	}
}
