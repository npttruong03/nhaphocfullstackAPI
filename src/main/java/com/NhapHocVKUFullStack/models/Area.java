package com.NhapHocVKUFullStack.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Area {
	private Integer id;
	
	private String idArea;
	
	private String name;
	
	private Double scoreAdd;
	
	private Boolean status;

}
