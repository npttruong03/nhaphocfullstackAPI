package com.NhapHocVKUFullStack.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
		@JsonProperty("username")
	  private String username;
		
		@JsonProperty("email")
	    private String email; 
		
	    private List<String> role = new ArrayList<>();
	    
	    @JsonProperty("password")
	    private String password;
}
