package com.NhapHocVKUFullStack.models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Content {

	private Integer id;
	
	private String title;
	
	private String content;
	
	private boolean status;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;
}
