package com.yapp.fmz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableScheduling
public class FmzApplication {

	public static void main(String[] args) {
		SpringApplication.run(FmzApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(200); //8
		executor.setMaxPoolSize(200); // 8
		executor.setQueueCapacity(60000); // 5000
		executor.setThreadNamePrefix("GithubLookup-");
		executor.initialize();
		return executor;
	}


}
