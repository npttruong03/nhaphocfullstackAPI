package com.NhapHocVKUFullStack.models;

import java.util.ArrayList;
import java.util.List;

public class CityResponse {
	private List<City> results;

    public CityResponse() {
        results = new ArrayList<>();
    }

    public List<City> getResults() {
        return results;
    }

    public void addProvince(City city) {
        results.add(city);
    }
}
