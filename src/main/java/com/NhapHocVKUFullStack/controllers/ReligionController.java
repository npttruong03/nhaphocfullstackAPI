package com.NhapHocVKUFullStack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.NhapHocVKUFullStack.Service.ReligionService;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Message;
import com.NhapHocVKUFullStack.models.Religion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/religion")
public class ReligionController {

	@Autowired
    private ReligionService religionService;
	Message message = null;
    @GetMapping
    public String index(@RequestParam(defaultValue = "1") int page, Model model) throws JsonMappingException, JsonProcessingException {
        int pageSize = 5;
        List<Religion> religions = this.religionService.getDataFromAPI();
        
        int totalItems = religions.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        
        // Tính toán chỉ mục bắt đầu và kết thúc của danh sách phần tử trên trang hiện tại
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        
        List<Religion> paginatedReligions = religions.subList(startIndex, endIndex);
        
        model.addAttribute("religions", paginatedReligions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        if (message != null)
		{
			model.addAttribute("message", message);
			message = null;
		}
        return "tongiao/tongiao";
    }

    @GetMapping("/add")
    public String addReligion(ModelMap modelMap)
    {
    	Religion religion = new Religion();
    	modelMap.addAttribute("religion", religion);
    	if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
    	return "tongiao/tongiao_add";
    }
	 
	 @RequestMapping(value = "tongiao_add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)

	  public String createContent(Religion religion, HttpServletRequest request)  {
		 religionService.post(religion);
		 message = new Message();
		message.setStatus("success");
		message.setMessage("Thêm tôn giáo thành công!");
		 return "redirect:/admin/religion";
	    }
//	 --------------------get and edit---------------------
	 @GetMapping("/edit/{id}")
	    public String showEdit(@PathVariable int id, ModelMap modelMap) {
	        Religion religion;
			try {
				religion = religionService.getById(id);
		        modelMap.addAttribute("religion", religion);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        return "tongiao/tongiao_edit";
	    }

	 @PostMapping("/religion_edit/{id}")
	 public String editContent(@PathVariable("id") int id, Religion religion, HttpServletRequest request) {
	     religion.setIdReligion(id);
	     religionService.editReligion(religion);
	     message = new Message();
		message.setStatus("success");
		message.setMessage("Cập nhật tôn giáo thành công!");
	     return "redirect:/admin/religion";
	 }

}