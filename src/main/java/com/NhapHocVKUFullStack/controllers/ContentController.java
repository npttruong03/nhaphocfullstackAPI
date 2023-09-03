package com.NhapHocVKUFullStack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.NhapHocVKUFullStack.Service.ContentService;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.dtos.ContentDTO;
import com.NhapHocVKUFullStack.models.Content;
import com.NhapHocVKUFullStack.models.Cookie;
import com.NhapHocVKUFullStack.models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.NhapHocVKUFullStack.Service.ContentService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/content")
public class ContentController {
	private Message message = null;
	@Autowired
	private ContentService contentService;

	   private final AppConfig appConfig;

	    @Autowired
	    public ContentController(AppConfig appConfig) {
	        this.appConfig = appConfig;
	    }
	@GetMapping()
	public String index(ModelMap modelMap, @RequestParam(value = "keyword", required = false) String keyword)
			throws Exception {
//		 String cookieValue = appConfig.cookieStore().getHeaders();
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		if (keyword == null) {
			modelMap.addAttribute("contents", this.contentService.getDataFromAPI());
			modelMap.addAttribute("cookieValue", appConfig.cookieStore().getHeaders());}
		else {
			modelMap.addAttribute("contents", this.contentService.searchDataFromAPI(keyword));
			modelMap.addAttribute("cookieValue", appConfig.cookieStore().getHeaders());}
		return "Content/content";
	}

	@GetMapping("/create")
	public String showCreate() {
		return "Content/create";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createContent(Content content, HttpServletRequest request) {
		try {
			contentService.post(content);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Thêm Nội dung thành công!");
			return "redirect:/admin/content";

		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Thêm Nội dung không thành công!");
			return "redirect:/admin/content/create";
		}
	}

	@GetMapping("/edit/{id}")
	public String HomeEdit(ModelMap modelMap, @Validated @PathVariable("id") int id) throws Exception {
		Content cnt = this.contentService.getById(id);
		modelMap.addAttribute("cnt", cnt);
		return "Content/edit";
	}

	@PostMapping("/edit/{id}")
	public String edit(Content content, @PathVariable("id") int id) {
		try {
			contentService.callApi(content, id);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			return "redirect:/admin/content";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!");

			return "redirect:/admin/content/edit/" + id;
		}
	}

}
