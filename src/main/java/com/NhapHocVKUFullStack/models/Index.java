package com.NhapHocVKUFullStack.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Index{
		@JsonProperty("code")
	 	private Integer id;
	 	
		@JsonProperty("codename")
	    private String matinhthanh;
		
		@JsonProperty("name")
	    private String tentinhthanh;		

	    private Integer status;

	    private Timestamp createTime;

	    private Timestamp updateTime;
	    
	    public void setDateTimeToNow() {
	        LocalDateTime now = LocalDateTime.now();
	        this.createTime = Timestamp.valueOf(now);
	        this.updateTime = Timestamp.valueOf(now);
	    }

}
