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
	public class Majors {
	
			
			private String id;
	
		 	
		    private String majorsID;
			
			
		    private String majorsName;
			
		    private String majorsNameStandFor;
			
		
		    private Boolean status;
	
		    private Timestamp createTime;
	
		    private Timestamp updateTime;
		    
	}
