		package com.NhapHocVKUFullStack.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Content;
import com.NhapHocVKUFullStack.models.DocumentItems;
import com.NhapHocVKUFullStack.models.Student;
import com.NhapHocVKUFullStack.models.paper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@Service
public class PaperService {
	private HttpHeaders headers = new HttpHeaders();
	private AppConfig appConfig;
	private String api = Utils.BASE_URL;
	@Autowired
	public PaperService(AppConfig appConfig)
	{
		this.appConfig = appConfig;
		}

	public Map<String, Integer> getDataFromAPI() throws Exception {

	    String apiURL = api+"/api/admin/list";
	    RestTemplate restTemplate = new RestTemplate();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(apiURL));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();

	    ObjectMapper objectMapper = new ObjectMapper();
	    List<Student> listStudent = objectMapper.readValue(json, new TypeReference<List<Student>>() {});

	    Map<String, Integer> documentItemsCount = new HashMap<>();

	    for (Student student : listStudent) {
	        String documentItems = student.getDocumentItems();
	        if (documentItems != null) {
	            documentItemsCount.put(documentItems, documentItemsCount.getOrDefault(documentItems, 0) + 1);
	        }
	    }

	    return documentItemsCount;
	}

	public List<Student> getDataFromAPIsvdn() throws Exception {

	    String apiURL = api+"/api/admin/list";
	    RestTemplate restTemplate = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_JSON);
	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(apiURL));
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();

	    ObjectMapper objectMapper = new ObjectMapper();
	    List<Student> listStudent = objectMapper.readValue(json, new TypeReference<List<Student>>() {});

	    return listStudent;
	}
	
}