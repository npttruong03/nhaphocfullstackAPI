package com.NhapHocVKUFullStack.models;

import java.util.List;

import org.springframework.http.HttpHeaders;

public class Cookie {
	private String cookie;	
	private HttpHeaders headers;
	private List<String> roles;
	private String username;
    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
        headers = new HttpHeaders();
        headers.add("Cookie", "nhaphocvku=" + cookie + "; Max-Age=3600");
    }
    
    public HttpHeaders getHeaders() {
		return headers;
	}
    
    public List<String> getRoles() {
    	return roles;
    }
    
    public void setRoles(List<String> roles) {
    	this.roles = roles;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
}
