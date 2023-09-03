package com.NhapHocVKUFullStack.config;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import com.NhapHocVKUFullStack.controllers.AuthInterceptor;

import jakarta.mail.Session;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private AppConfig appConfig;
	
	@Autowired
	public WebConfig(AppConfig appConfig)
	{
		this.appConfig = appConfig;
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// kiểm tra xem có token ko mới đc truy cập vào trang web
		registry.addInterceptor(new AuthInterceptor(appConfig)).addPathPatterns("/admin/**").excludePathPatterns("/admin/login/**");
	}
	
	
	
}
