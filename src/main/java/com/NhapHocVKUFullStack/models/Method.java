package com.NhapHocVKUFullStack.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Method {
	 @JsonProperty("id")
	private Integer id;
	 
	 @JsonProperty("ma_phuong_thuc")
	public String ma_phuong_thuc;
	 
	 @JsonProperty("ten_phuong_thuc")
	private String ten_phuong_thuc;
	 
	 @JsonProperty("status")
	private boolean status;

	private Timestamp createTime;
	
    private Timestamp updateTime;
    
    public void setDateTimeToNow() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = Timestamp.valueOf(now);
        this.updateTime = Timestamp.valueOf(now);
    }
}
