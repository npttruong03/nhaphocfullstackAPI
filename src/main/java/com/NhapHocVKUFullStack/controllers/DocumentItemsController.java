package com.NhapHocVKUFullStack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.NhapHocVKUFullStack.Service.DocumentItemsService;
import com.NhapHocVKUFullStack.Service.KhoaService;
import com.NhapHocVKUFullStack.Service.MajorsService;
import com.NhapHocVKUFullStack.Service.MethodService;
import com.NhapHocVKUFullStack.models.DocumentItems;
import com.NhapHocVKUFullStack.models.Message;

@Controller
@RequestMapping("/admin/documentItems")
public class DocumentItemsController {
	@Autowired
	private DocumentItemsService documentItemsService;
	
	@Autowired
	private KhoaService khoaService;
	
	@Autowired
	private MajorsService majorsService;
	
	@Autowired
	private MethodService methodService;
	
	Message message = null;
	
	@GetMapping
	public String getAll(ModelMap modelMap) throws Exception{
		modelMap.addAttribute("documents", documentItemsService.getAll());
		modelMap.addAttribute("majors", majorsService.getDataFromAPI());
		modelMap.addAttribute("khoas", khoaService.getDataFromAPI());
		modelMap.addAttribute("methods", methodService.getDataFromAPI());
		
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "documentItems/documentItems";
	}
	
	@GetMapping("/add")
	public String add(ModelMap modelMap) throws Exception {
		DocumentItems documentItems = new DocumentItems();
		documentItems.setSoLuong(1);
		modelMap.addAttribute("document", documentItems);
		modelMap.addAttribute("majors", majorsService.getDataFromAPI());
		modelMap.addAttribute("khoas", khoaService.getDataFromAPI());
		modelMap.addAttribute("methods", methodService.getDataFromAPI());
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "documentItems/documentItems_add";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute("document") DocumentItems documentItems) {
		Boolean rs;
		try {
			rs = documentItemsService.saveDocument(documentItems);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Thêm danh mục giấy tờ thành công!");
			return "redirect:/admin/documentItems";
			
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Thêm danh mục giấy tờ không thành công!");
			return "redirect:/admin/documentItems/add";
		}
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute("curry") DocumentItems documentItems, ModelMap modelMap) {
		Boolean rs;
		try {
			rs = documentItemsService.updateDocument(documentItems);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			
			return "redirect:/admin/documentItems";
			
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!");
			
			return "redirect:/admin/documentItems/edit/" + documentItems.getId();
		} 
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, ModelMap modelMap) {
		try {
			DocumentItems document = documentItemsService.getDocument(id);
			modelMap.addAttribute("document", document);
			modelMap.addAttribute("majors", majorsService.getDataFromAPI());
			modelMap.addAttribute("khoas", khoaService.getDataFromAPI());
			modelMap.addAttribute("methods", methodService.getDataFromAPI());
			if (message != null)
			{
				modelMap.addAttribute("message", message);
				message = null;
			}
			
			return "documentItems/documentItems_edit";
		} catch (Exception e) {
			return "redirect:/admin/documentItems/add";

		}
	}
	
	@GetMapping("/choose")
	public String getForChoose(ModelMap modelMap) throws Exception {
		modelMap.addAttribute("khoa", khoaService.getNewestKhoa());
		modelMap.addAttribute("majors", majorsService.getDataFromAPI());
		modelMap.addAttribute("methods", methodService.getDataFromAPI());
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "documentItems/documentItems_choose";
	}
	
	@PostMapping("/choose")
	public String saveRequire(@RequestParam("listId") String listId) {
		try {
			documentItemsService.selectRequire(listId);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Xác nhận thành công!");
			
			return "redirect:/admin/documentItems/choose";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Xác nhận không thành công!");
			
			return "redirect:/admin/documentItems/choose";
			
		}
	}
	
	@PostMapping("/import/documentitems")
    public String importDocumentItemsExcel(@RequestParam("file") MultipartFile file) {
		try {
        documentItemsService.importDocumentItemsFromExcel(file);
        message = new Message();
		message.setStatus("success");
		message.setMessage("Thêm danh mục giấy tờ thành công!");
        return "redirect:/admin/documentItems";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("success");
			message.setMessage(e.toString());
	        return "redirect:/admin/documentItems";
		}
    }

	
}
