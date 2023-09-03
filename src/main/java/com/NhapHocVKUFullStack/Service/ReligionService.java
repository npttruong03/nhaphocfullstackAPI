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
import com.NhapHocVKUFullStack.models.Religion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class ReligionService {
	private AppConfig appConfig;
	private RestTemplate restTemplate = new RestTemplate();
	@Autowired
	private ReligionService(AppConfig appConfig) {
		this.appConfig = appConfig;
	}
	public List<Religion> getDataFromAPI() throws JsonMappingException, JsonProcessingException {
		String apiURL = Utils.BASE_URL + "/admin/api/religion";

		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();

	    ObjectMapper objectMapper = new ObjectMapper();
	    List<Religion> listReligion = objectMapper.readValue(json, new TypeReference<List<Religion>>() {});

	    return listReligion;
	}
	
	public Religion post(Religion religion) {
		String apiURL = Utils.BASE_URL + "/admin/api/religion/";
		RequestEntity<?> requestEntity = new RequestEntity<>(religion ,appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(apiURL));
		ResponseEntity<Religion> response = restTemplate.exchange(requestEntity, Religion.class);
	    return response.getBody();
	}
	
//	--------------------get và edit----------------------------
	public Religion getById(int id) throws Exception {
	    List<Religion> religions = getDataFromAPI();
	    Optional<Religion> optionalReligion = religions.stream()
	            .filter(religion -> religion.getIdReligion() == id)
	            .findFirst();
	    if (optionalReligion.isPresent()) {
	        return optionalReligion.get();
	    } else {
	        throw new Exception("Religion not found with ID: " + id);
	    }
	}

	
	public Religion editReligion(Religion religion) {

		String apiURL = Utils.BASE_URL + "/admin/api/religion/edit/"+ religion.getIdReligion();

		RequestEntity<?> requestEntity = new RequestEntity<>(religion ,appConfig.cookieStore().getHeaders(), HttpMethod.PUT, URI.create(apiURL));
		ResponseEntity<Religion> response = restTemplate.exchange(requestEntity, Religion.class);
		return response.getBody();
	}
}
