package com.NhapHocVKUFullStack.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wards {
	private Integer id;
	
	@JsonProperty("ward_id")
    private int maxa;
	
	@JsonProperty("ward_name")
    private String tenxa;
	
	@JsonProperty("ward_type")
	private String tructhuoc;
	
	@JsonProperty("district_id")
    private int maquan;

	private List<Wards> results;
	
    public List<Wards> getResults() {
        return results;
    }

    public void addProvince(Wards wards) {
        results.add(wards);
    }
    
    private Integer status;

    private Timestamp createTime;

    private Timestamp updateTime;
    
    public void setDateTimeToNow() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = Timestamp.valueOf(now);
        this.updateTime = Timestamp.valueOf(now);
    }
}
