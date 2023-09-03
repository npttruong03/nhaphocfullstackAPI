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
public class paper {
	@JsonProperty("id")
	private Integer id;
	
	@JsonProperty("documentItems")
	private int documentitem;

	private Student student;
	
	@JsonProperty("id_khoa")
    private Integer idkhoa;

	@JsonProperty("id_nganh")
    private String idnganh;
    
	@JsonProperty("loaigiayto")
    private String loaigiayto;
    
	@JsonProperty("ghichu")
    private String ghichu;
    
	@JsonProperty("soluong")
    private int soluong;
    
	@JsonProperty("id_pttt")
    private int itpttt;

	@JsonProperty("status")
    private Boolean status;

    private Timestamp createTime;

    private Timestamp updateTime;
    
    public void setDateTimeToNow() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = Timestamp.valueOf(now);
        this.updateTime = Timestamp.valueOf(now);
    }

}
