package com.NhapHocVKUFullStack.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DistrictReponse {
	@JsonProperty("province_id")
    private int matinhthanh;
	
	private List<District> results;
	public DistrictReponse() {
		
	}

    public DistrictReponse(List<District> results, int matinhthanh) {
        this.results = results;
        this.matinhthanh = matinhthanh;
    }

    public List<District> getResults() {
        return results;
    }

    public void addProvince(District district) {
        results.add(district);
    }

	public int getMatinhthanh() {
		return matinhthanh;
	}

	public void setMatinhthanh(int matinhthanh) {
		this.matinhthanh = matinhthanh;
	}
    

}
