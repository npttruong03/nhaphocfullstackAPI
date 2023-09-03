package com.NhapHocVKUFullStack.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.NhapHocVKUFullStack.Service.CityService;
import com.NhapHocVKUFullStack.models.City;
import com.NhapHocVKUFullStack.models.Curriculum;
import com.NhapHocVKUFullStack.models.District;
import com.NhapHocVKUFullStack.models.Message;
import com.NhapHocVKUFullStack.models.Wards;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
@RequestMapping("/admin/city")
public class CityController {

    private static final String TINH_THANH_TITLE = "Danh sách Tỉnh thành";
    private static final String QUAN_HUYEN_TITLE = "Danh sách Quận huyện";
    private static final String XA_PHUONG_TITLE = "Danh sách Xã phường";

    private CityService cityService;
    private Message message;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public String getCity(Model model) throws JsonMappingException, JsonProcessingException {

    	List<City> cityList = cityService.getDataFromAPI();
    	
        String tableTitle = TINH_THANH_TITLE;
        model.addAttribute("tableTitle", tableTitle);

        for (City city : cityList) {
            if (city.getTentinhthanh() != null && !city.getTentinhthanh().isEmpty()) {
                city.setStatus(1);
                city.setDateTimeToNow();
            } else {
                city.setStatus(0);
            }
        }
        model.addAttribute("cities", cityList);

        return "City/tinhthanh";
    }
    

    
    
//    @GetMapping("/getCity/{id}")
//	public String getcity(@PathVariable int id, ModelMap modelMap) {
//		try {
//			District DistrictList = cityService.getCity(id);
//			modelMap.addAttribute("districts", DistrictList);
//			if (message != null)
//			{
//				modelMap.addAttribute("message", message);
//				message = null;
//			}
//			
//			return "quanhuyen";
//		} catch (JsonProcessingException e) {
//			return "redirect:/admin/city";
//
//		}
//		
//	}
    
    
	 @GetMapping("/getCity/{matinhthanh}")
	    public String indexDistrict(@PathVariable String matinhthanh,  ModelMap modelMap) throws JsonMappingException, JsonProcessingException {
		  String tableTitle = QUAN_HUYEN_TITLE;
	        modelMap.addAttribute("tableTitle", tableTitle);
	        List<District> DistrictList = cityService.getDataFromAPIDistrict(matinhthanh);
	        for (District district : DistrictList) {
   if (district.getTenquan() != null && !district.getTenquan().isEmpty()) {
 	  district.setStatus(1);
 	  district.setDateTimeToNow();
   } else {
 	  district.setStatus(0);
   }
}
      modelMap.addAttribute("districts", DistrictList);
	        return "City/quanhuyen";
	    }
	 
	 @GetMapping("/getWard/{maquan}")
	    public String indexWards(@PathVariable String maquan,  ModelMap modelMap) throws JsonMappingException, JsonProcessingException {
		 String tableTitle = XA_PHUONG_TITLE;
	        modelMap.addAttribute("tableTitle", tableTitle);
	        List<Wards> WardList = cityService.getDataFromAPIWard(maquan);
	        for (Wards wards : WardList) {
if (wards.getTenxa() != null && !wards.getTenxa().isEmpty()) {
	  wards.setStatus(1);
	  wards.setDateTimeToNow();
} else {
	  wards.setStatus(0);
}
}
   modelMap.addAttribute("wards", WardList);
	        return "City/xaphuong";
	    }
    
}