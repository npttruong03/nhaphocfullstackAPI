package com.NhapHocVKUFullStack.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentItems {
	private Integer id;
	
	private Khoa khoa;
	
	private Majors majors;
	
	private Method method;
	
	private String documentType;
	
	private Integer soLuong;
	
	private String note;
	
	private Boolean status;

	public boolean isSubmitted() {
	    return status; // Trả về giá trị của trường "status"
	}
}
