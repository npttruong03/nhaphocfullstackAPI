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
	public class Khoa {
	
		 private Integer id;
	
		    private String khoa;
	
		    private int namBatDau;
	
		    private int namKetThuc;
	
		    private String thoiGianBatDau;
	
		    private String thoiGianKetThuc;
	
		    private boolean status;
	
		    private Timestamp createTime;
	
		    private Timestamp updateTime;
		    
	
	   
	    
	 
	}
