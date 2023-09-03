package com.NhapHocVKUFullStack.Service;


import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Ethnic;
import com.NhapHocVKUFullStack.models.Method;
import com.NhapHocVKUFullStack.models.Religion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MethodService {
	
	private AppConfig appConfig;
	RestTemplate restTemplate = new RestTemplate();
	@Autowired
	public MethodService(AppConfig appConfig)
	{
		this.appConfig = appConfig;
	}
	
	public List<Method> getDataFromAPI() throws JsonMappingException, JsonProcessingException {
//		String apiURL = "http://localhost:2222/admin/api/method";
		String apiURL = Utils.BASE_URL + "/admin/api/method";
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		 
		    String json = response.getBody();

		    ObjectMapper objectMapper = new ObjectMapper();
		    List<Method> listMethod = objectMapper.readValue(json, new TypeReference<List<Method>>() {});

		    return listMethod;
	}

	public String post(Method method) {
		String apiURL = Utils.BASE_URL +  "/admin/api/method/add";
//		String apiURL = "http://localhost:2222/admin/api/method/add";
	    RequestEntity<?> requestEntity = new RequestEntity<>(method, appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    return response.getBody();
	}
	
	
	public Method getById(int id) throws Exception {
	    List<Method> methods = getDataFromAPI();
	    Optional<Method> optionalMethod = methods.stream()
	            .filter(method -> method.getId() == id)
	            .findFirst();
	    if (optionalMethod.isPresent()) {
	        return optionalMethod.get();
	    } else {
	        throw new Exception("Religion not found with ID: " + id);
	    }
	}
	
	public Method getByIdMethod(String id) throws Exception {
	    List<Method> methods = getDataFromAPI();
	    Optional<Method> optionalMethod = methods.stream()
	            .filter(method -> method.getMa_phuong_thuc().equals(id))
	            .findFirst();
	    if (optionalMethod.isPresent()) {
	        return optionalMethod.get();
	    } else {
	        throw new Exception("Religion not found with ID: " + id);
	    }
	}
	

	
	public void editMethod(Method method) {
		String apiURL = Utils.BASE_URL+ "/admin/api/method/update/";
//        String apiURL = "http://localhost:2222/admin/api//method/update/{id}";
        RequestEntity<?> requestEntity = new RequestEntity<>(method , appConfig.cookieStore().getHeaders(), HttpMethod.PUT, URI.create(apiURL + method.getId()));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
    }
}
