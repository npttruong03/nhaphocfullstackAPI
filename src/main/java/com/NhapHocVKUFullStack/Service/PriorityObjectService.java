package com.NhapHocVKUFullStack.Service;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.PriorityObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PriorityObjectService {
	private AppConfig appConfig;
	private RestTemplate restTemplate = new RestTemplate();
	public PriorityObjectService(AppConfig appConfig)
	{
		this.appConfig = appConfig;
	}
	private String api = Utils.BASE_URL;
	public List<PriorityObject> getAll() throws Exception {

		String apiURL = api+"/admin/api/object/getAll";
	 	RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<PriorityObject> list = objectMapper.readValue(json, new TypeReference<List<PriorityObject>>() {});

	    return list;
	}
	
	public PriorityObject getObject(int id) throws Exception {
		String apiURL = api+"/admin/api/object/get/" + id;
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
	    ResponseEntity<PriorityObject> response = restTemplate.exchange(requestEntity, PriorityObject.class);
	    PriorityObject object = response.getBody();

	    return object;
	}

	public Boolean saveObject(PriorityObject object) throws Exception {
		String apiURL = api+"/admin/api/object/save";
		RequestEntity<?> requestEntity = new RequestEntity<>(object, appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(apiURL));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();

	    if (json == null) {
	    	return false;
	    }

	    return true;
	}
	
	public Boolean updateObject(PriorityObject object) throws Exception {
		String apiurl = api+"/admin/api/object/update/" + object.getId();
		RequestEntity<?> requestEntity = new RequestEntity<>(object, appConfig.cookieStore().getHeaders(), HttpMethod.PUT, URI.create(apiurl));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		
		String json = response.getBody();
		
		if (json == null)
		{
			return false;
		}
		
		return true;
	}
}
