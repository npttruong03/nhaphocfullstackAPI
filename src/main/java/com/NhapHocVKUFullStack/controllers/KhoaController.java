package com.NhapHocVKUFullStack.controllers;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.NhapHocVKUFullStack.Service.KhoaService;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/khoa")
public class KhoaController {
    private Message message = null;
    @Autowired
    private KhoaService khoaService;
    private final AppConfig appConfig;

    @Autowired
    public KhoaController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }
    @GetMapping()
    public String index(ModelMap modelMap, @RequestParam(value = "keyword", required = false) String keyword)
            throws Exception {
        if (message != null) {
            modelMap.addAttribute("message", message);
            message = null;
        }
        if (keyword == null) {
            modelMap.addAttribute("khoas", this.khoaService.getDataFromAPI());
        	modelMap.addAttribute("cookieValue", appConfig.cookieStore().getHeaders());
        } else {
            modelMap.addAttribute("khoas", this.khoaService.searchDataFromAPI(keyword));
        	modelMap.addAttribute("cookieValue", appConfig.cookieStore().getHeaders());
        }
        return "Khoa/khoa";
    }
    
    @GetMapping("/create")
    public String showCreate(ModelMap modelMap) {
        modelMap.addAttribute("khoa", new Khoa());
        return "Khoa/create";
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createKhoa(Khoa khoa, HttpServletRequest request){
      
        
        try {
        	 khoaService.post(khoa);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Thêm Nội dung thành công!");
			   return "redirect:/admin/khoa";

		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Thêm Nội dung không thành công!");
			return "redirect:/admin/khoa/create";
		}
	

    }

    @GetMapping("/edit/{id}")
    public String showEdit(ModelMap modelMap, @PathVariable("id") int id) throws Exception {
        Khoa khoa = this.khoaService.getById(id);
        if (khoa.getId() == null) {
            throw new Exception("Khoa not found with id: " + id);
        }
        modelMap.addAttribute("khoa", khoa);
        return "Khoa/edit";
    }

    @PostMapping("/edit/{id}")
    public String editKhoa(@PathVariable("id") int id, @Validated Khoa khoa) throws Exception {
        try {
            khoaService.callApi(khoa, id);
            message = new Message();
            message.setStatus("success");
            message.setMessage("Cập nhật thành công!");
            return "redirect:/admin/khoa";
        } catch (Exception e) {
            message = new Message();
            message.setStatus("fail");
            message.setMessage("Cập nhật không thành công!");
            return "redirect:/admin/khoa/edit/" + id;
        }
    }
    
}
