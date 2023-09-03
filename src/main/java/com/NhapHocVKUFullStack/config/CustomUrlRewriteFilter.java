package com.NhapHocVKUFullStack.config;

import java.io.IOException;
import java.io.InputStream;



import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;
import org.tuckey.web.filters.urlrewrite.UrlRewriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.tuckey.web.filters.urlrewrite.Conf;

@Component
public class CustomUrlRewriteFilter extends UrlRewriteFilter {
	private UrlRewriter urlRewriter;

	@Autowired
	Environment env;

	@Override
	public void loadUrlRewriter(FilterConfig filterConfig) throws ServletException {
	    try {
	        ClassPathResource classPathResource = new ClassPathResource("urlrewrite.xml");
	        InputStream inputStream = classPathResource.getInputStream();
	        Conf conf1 = new Conf(filterConfig.getServletContext(), inputStream, "urlrewrite.xml", "");
	        urlRewriter = new UrlRewriter(conf1);
	    } catch (Exception e) {
	        throw new ServletException(e);
	    }
	}

	@Override
	public UrlRewriter getUrlRewriter(ServletRequest request, ServletResponse response, FilterChain chain) {
	    return urlRewriter;
	}

	@Override
	public void destroyUrlRewriter() {
	    if(urlRewriter != null)
	        urlRewriter.destroy();
	}

}
