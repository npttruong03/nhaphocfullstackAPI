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
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class District {
	private Integer id;
	
	@JsonProperty("district_id")
    private int maquan;
	
	@JsonProperty("district_name")
    private String tenquan;
	
	@JsonProperty("district_type")
	private String tructhuoc;
	
	@JsonProperty("province_id")
    private int matinhthanh;

	private List<District> results;
	
    public List<District> getResults() {
        return results;
    }

    public void addProvince(District district) {
        results.add(district);
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
