package com.NhapHocVKUFullStack.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Curriculum {
	private Integer id;
	
	private Khoa khoa;
	 
	private Majors majors;
	
	private String name;
	
	private Boolean status;
	
	private Integer hienthi;
}
