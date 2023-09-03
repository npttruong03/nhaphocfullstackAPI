package com.NhapHocVKUFullStack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import com.NhapHocVKUFullStack.config.CustomUrlRewriteFilter;
import jakarta.servlet.Filter;


@SpringBootApplication
public class NhapHocVkuFullStackApplication {

	public static void main(String[] args) {
		SpringApplication.run(NhapHocVkuFullStackApplication.class, args);
	}
	
	@Bean
	public FilterRegistrationBean<Filter> setFilterRegistrationBean() {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new CustomUrlRewriteFilter());
		return bean;
	}

}
