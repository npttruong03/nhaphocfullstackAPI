package com.NhapHocVKUFullStack.Service;

import java.lang.reflect.Array;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Account;
import com.NhapHocVKUFullStack.models.Cookie;
import com.NhapHocVKUFullStack.models.Index;
import com.NhapHocVKUFullStack.models.LoginRequest;
import com.NhapHocVKUFullStack.models.Role;
import com.NhapHocVKUFullStack.models.SignupRequest;
import com.NhapHocVKUFullStack.models.Student;
import com.NhapHocVKUFullStack.response.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class LoginService {
	
	private MessageResponse messageResponse;
	private AppConfig appConfig;
    @Autowired
    public LoginService(AppConfig appConfig)
    {
    	this.appConfig = appConfig;
    }
    
    
    public String loginAndGetToken(String username, String password) throws Exception {

//    	 String loginUrl = "http://localhost:2221/api/auth/signin"; // Thay đổi thành URL của API đăng nhập
        String loginUrl = Utils.BASE_URL + "/api/auth/signin";

    	LoginRequest loginRequest = new LoginRequest();


//    	 String hashedPassword = passwordEncoder.encode(password);
         loginRequest.setUsername(username);
         loginRequest.setPassword(password);
         
         // Tạo các thông tin đăng nhập gửi đến server
         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_JSON);

         // Tạo HttpEntity chứa headers
         HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest,headers);
         // Tạo RestTemplate
         RestTemplate restTemplate = new RestTemplate();

         // Gọi API đăng nhập
         ResponseEntity<String> response = restTemplate.exchange(loginUrl, HttpMethod.POST, entity, String.class);
         if (response.getStatusCode().is2xxSuccessful()) {

             String json = response.getBody(); 
             ObjectMapper objectMapper = new ObjectMapper();
             LoggedInfo loggedInfo = objectMapper.readValue(json, LoginService.LoggedInfo.class);
             appConfig.cookieStore().setCookie(loggedInfo.getToken());

//             appConfig.cookieStore().setRoles(loggedInfo.getRole());

             appConfig.cookieStore().setRoles(loggedInfo.getRole());
             appConfig.cookieStore().setUsername(loggedInfo.getUsername());
             return "OK";
         } else {
             throw new RuntimeException("Failed to login. Status code: " + response.getStatusCodeValue());
         }
     }
    
    
    public String signupAndGetToken(String username, String password, String email, List<String> role) throws Exception {

//   	 String loginUrl = "http://localhost:2221/api/auth/signup"; // Thay đổi thành URL của API đăng nhập
       String loginUrl = Utils.BASE_URL + "/api/auth/signup";

       SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword(password);
        signupRequest.setEmail(email);
        signupRequest.setRole(role);
        
        // Tạo các thông tin đăng nhập gửi đến server
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo HttpEntity chứa headers
//        HttpEntity<SignupRequest> entity = new HttpEntity<>(signupRequest,headers);
//        System.out.println("dmm" + entity);
        
        // Tạo RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        
        
        RequestEntity<?> requestEntity = new RequestEntity<>(signupRequest, appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(loginUrl));
        // Gọi API đăng nhập
    	ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);   
        String json = response.getBody();
        return json;
            
    }
    

    public String logout() {
//    	String api = "http:localhost:2222/api/auth/signout";
    	String api = "http://222.255.130.100:7070/vku/api/auth/signout";
    	
    	RestTemplate restTemplate = new RestTemplate();
    	RequestEntity<?> requestEntity = new RequestEntity(appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(api));
    	ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
    	if (responseEntity.getBody().equals("Logout OK"))
    	{
        	appConfig.cookieStore().setCookie("");
        	return "OK";
    	}
    	return "Logout fail";
    }
    
    public String checkToken() {
    	String token = appConfig.cookieStore().getCookie();
    	System.out.println("get "+ token);
    	return token;
    }

    public String addUsernameAnhRole() {
    	String token = appConfig.cookieStore().getCookie();
    	System.out.println("lay " + token);
    	return token;
    }
    
    static class LoggedInfo {
		  String token;
		  List<String> role;
		  String username;
		
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public List<String> getRole() {
			return role;
		}
		public void setRole(List<String> roles) {
			this.role = roles;
		}
		
		public String getUsername() {
			return username;
		}
		
		public void setUsername(String username) {
			this.username = username;
		}
  }
   
   
}

