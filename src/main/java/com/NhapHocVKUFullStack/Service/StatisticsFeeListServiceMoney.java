package com.NhapHocVKUFullStack.Service;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.DocumentItems;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.Student;
import com.NhapHocVKUFullStack.models.StudentTuition;
import com.NhapHocVKUFullStack.models.TuitionFeeList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsFeeListServiceMoney {
    private String apiURL = Utils.BASE_URL+"/admin/api/studentTuition";
//    private String apiURL1 = "http://47.254.215.213/vku/api/document/getAll";
    private RestTemplate restTemplate = new RestTemplate();
    private AppConfig appConfig;

    @Autowired
    public StatisticsFeeListServiceMoney(AppConfig appConfig) {
        this.appConfig = appConfig;
    }
	
	private List<StudentTuition> listStudentTuitions  ;
	 List<TuitionFeeList> tuitionFeeList  ;
    public List<StudentTuition> getDataFromApi() throws Exception {
        RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET,
                URI.create(apiURL));
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
        String json = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        List<StudentTuition> list = objectMapper.readValue(json, new TypeReference<List<StudentTuition>>() {});
        return list;
    }
    
    public List<StudentTuition> getDataFromApi1()throws Exception{
	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
    ObjectMapper objectMapper = new ObjectMapper();
    listStudentTuitions  = objectMapper.readValue(json, new TypeReference<List<StudentTuition>>() {});
    return listStudentTuitions;
}
    public List<TuitionFeeList> getDataFromApi2() throws Exception{

//		RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiURL));

	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));

	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<TuitionFeeList> list = objectMapper.readValue(json, new TypeReference<List<TuitionFeeList>>() {});
	
	    return list;
	}
    
//    public List<Student> getDataFromAPI3() throws Exception {
//	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL1));
//	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
//	    String json = response.getBody();
//	    ObjectMapper objectMapper = new ObjectMapper();
//	    List<Student> listStudent = objectMapper.readValue(json, new TypeReference<List<Student>>() {});
//
//	   
//	    return listStudent;
//	}
//    
    public Map<String, Integer> calculateTuitionByTypengay() throws Exception {
        List<StudentTuition> studentTuitions = getDataFromApi1();
        Map<String, Integer> tuitionByDay = new HashMap<>();

        for (StudentTuition studentTuition : studentTuitions) {
            String tuitionDay = studentTuition.getTuitionDay();
            int tuitionAmount = studentTuition.getTotal();
            tuitionByDay.put(tuitionDay, tuitionByDay.getOrDefault(tuitionDay, 0) + tuitionAmount);
        }

        return tuitionByDay;
    }
    public Map<String, Integer> calculateTuitionByNgayThu() throws Exception {
        List<StudentTuition> studentTuitions = getDataFromApi1();
        Map<String, Integer> tuitionByNgayThu = new HashMap<>();
        for (StudentTuition studentTuition : studentTuitions) {
            String nguoiThu = studentTuition.getNameCashier();
            int tuitionAmount = studentTuition.getTotal();
            tuitionByNgayThu.put(nguoiThu, tuitionByNgayThu.getOrDefault(nguoiThu, 0) + tuitionAmount);
        }
        return tuitionByNgayThu;
    }
   
    
   
   }
