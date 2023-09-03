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
public class StudentTuition {
	private Integer id;
	
	private Student idStudent;
	
	private String nameCashier;
	
	private Integer total;
	
	private String proofOfTuitionPay;
	
	private String tuitionFeeList;
	
	private boolean status;
	
	private String tuitionDay;
	
	private String phuongThucThanhToan;
	
}
