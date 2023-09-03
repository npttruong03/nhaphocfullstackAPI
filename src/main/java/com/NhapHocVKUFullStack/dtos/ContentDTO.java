package com.NhapHocVKUFullStack.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDTO {
	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private String content;
	
	private boolean status;
	
}
