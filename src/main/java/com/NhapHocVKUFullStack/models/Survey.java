package com.NhapHocVKUFullStack.models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Survey {
private Integer id;
	
	private Khoa khoa;

	private Majors majors;
	
	private String name;
	
	private String url;
	
	private String decription;
	
	private int shared;
	
	private boolean status;
}
