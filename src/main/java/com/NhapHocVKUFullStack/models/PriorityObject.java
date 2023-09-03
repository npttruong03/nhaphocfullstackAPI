package com.NhapHocVKUFullStack.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriorityObject {
	private Integer id;

    private String idObject;

    private String name;

    private Double scoreAdd;
    
    private Boolean status;
}
