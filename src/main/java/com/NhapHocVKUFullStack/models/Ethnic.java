package com.NhapHocVKUFullStack.models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Ethnic {

	private Integer id;

	private String name;
	
	private boolean status;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;
}
