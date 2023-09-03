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
public class TuitionFeeList {
private Integer id;
	
	private Khoa khoa;

	private Majors majors;

	private Curriculum curriculum;
	
	private Integer tuition;
	
	private String name;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;
	
	private Boolean status;

	
}
	