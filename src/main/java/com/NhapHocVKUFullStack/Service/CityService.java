package com.NhapHocVKUFullStack.Service;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.NhapHocVKUFullStack.models.City;
import com.NhapHocVKUFullStack.models.CityResponse;
import com.NhapHocVKUFullStack.models.Curriculum;
import com.NhapHocVKUFullStack.models.District;
import com.NhapHocVKUFullStack.models.DistrictReponse;
import com.NhapHocVKUFullStack.models.Wards;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class CityService {
	
	public List<City> getDataFromAPI() throws JsonMappingException, JsonProcessingException {
		String apiURL = "https://vapi.vnappmob.com/api/province/";
		RestTemplate restTemplate = new RestTemplate();
		 ResponseEntity<String> response = restTemplate.getForEntity(apiURL, String.class);
		    String json = response.getBody();

		    ObjectMapper objectMapper = new ObjectMapper();
//		    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		    CityResponse cityResponse = objectMapper.readValue(json, CityResponse.class);
//		    List<City> listCity = objectMapper.readValue(json, new TypeReference<List<City>>() {});
		    List<City> listCity = cityResponse.getResults();

		    return listCity;
	}
	
	
//	public District getCity(int id) throws JsonMappingException, JsonProcessingException {
//		String apiURL = "https://provinces.open-api.vn/api/p/" + id + "?depth=2";
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<District> response = restTemplate.getForEntity(apiURL, District.class);
//		District district = response.getBody();
//	    return district;
//	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public List<District> getDataFromAPIDistrict(String matinhthanh) throws JsonMappingException, JsonProcessingException {
		String apiURL = "https://vapi.vnappmob.com/api/province/district/" + matinhthanh;
		RestTemplate restTemplate = new RestTemplate();
		 ResponseEntity<District> response = restTemplate.getForEntity(apiURL, District.class);
		    District json = response.getBody();

		    ObjectMapper objectMapper = new ObjectMapper();
//		    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		    District districtReponse = objectMapper.convertValue(json, District.class);
//		    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//		    List<District> districts = objectMapper.convertValue(json, new TypeReference<List<District>>() {});
		    List<District> districts = districtReponse.getResults();

		    return districts;
	}
	
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public List<Wards> getDataFromAPIWard(String maquan) throws JsonMappingException, JsonProcessingException {
		String apiURL = "https://vapi.vnappmob.com/api/province/ward/" + maquan;
		RestTemplate restTemplate = new RestTemplate();
		 ResponseEntity<Wards> response = restTemplate.getForEntity(apiURL, Wards.class);
		    Wards json = response.getBody();

		    ObjectMapper objectMapper = new ObjectMapper();
//		    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		    Wards wards = objectMapper.convertValue(json, Wards.class);
//		    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//		    List<District> districts = objectMapper.convertValue(json, new TypeReference<List<District>>() {});
		    List<Wards> wardsList = wards.getResults();

		    return wardsList;
	}
	
}
