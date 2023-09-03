package com.NhapHocVKUFullStack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.NhapHocVKUFullStack.Service.CurriculumService;
import com.NhapHocVKUFullStack.Service.KhoaService;
import com.NhapHocVKUFullStack.Service.MajorsService;
import com.NhapHocVKUFullStack.Service.TuitionFeeListService;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Content;
import com.NhapHocVKUFullStack.models.Message;
import com.NhapHocVKUFullStack.models.TuitionFeeList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/tuitionFeeList")
public class TuitionFeeListController {
	private Message message = null;
	
	@Autowired
	private TuitionFeeListService tuitionFeeListService;
	@Autowired
	private KhoaService khoaService;
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private MajorsService majorsService;
	
	private final AppConfig appConfig;

    @Autowired
    public TuitionFeeListController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }
	
	@GetMapping()
	public String index(ModelMap modelMap) throws Exception {
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		String cookieValue = appConfig.cookieStore().getCookie();
		modelMap.addAttribute("cookieValue", cookieValue);
		modelMap.addAttribute("khoas", this.khoaService.getDataFromAPI());
		modelMap.addAttribute("curries",this.curriculumService.getAllSttTrue());
		modelMap.addAttribute("majors", this.majorsService.getDataFromAPIsttTrue());
//		if (khoaId == null&&curriculumId==null &&majorsId==null )
			modelMap.addAttribute("tuitions", this.tuitionFeeListService.getDataFromApi());
		
//			modelMap.addAttribute("tuitions", this.tuitionFeeListService.searchDataFromAPI(khoaId,majorsId,curriculumId));
		return "tuitionFee/index";
	}
	
	@GetMapping("/create")
	public String showCreate(ModelMap modelMap)throws Exception {
		TuitionFeeList tuitionFeeList = new TuitionFeeList();
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		String cookieValue = appConfig.cookieStore().getCookie();
		modelMap.addAttribute("cookieValue", cookieValue);
		modelMap.addAttribute("fee", tuitionFeeList);
		modelMap.addAttribute("khoas", this.khoaService.getDatasttKhoatrue());
		modelMap.addAttribute("curries",this.curriculumService.getAllSttTrue());
		modelMap.addAttribute("majors", this.majorsService.getDataFromAPIsttTrue());
		return "tuitionFee/create";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createTuition(@ModelAttribute("fee") TuitionFeeList tuitionFeeList, HttpServletRequest request) {
		try {
			tuitionFeeListService.post(tuitionFeeList);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Thêm Học Phí thành công!");
			return "redirect:/admin/tuitionFeeList";

		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Thêm Học phí không thành công!"+e.toString());
			return "redirect:/admin/tuitionFeeList/create";
		}
	}
	
	@GetMapping("/edit/{id}")
	public String HomeEdit(ModelMap modelMap, @Validated @PathVariable("id") int id) throws Exception {
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		TuitionFeeList tuitionFeeList = tuitionFeeListService.getById(id);
		String cookieValue = appConfig.cookieStore().getCookie();
		modelMap.addAttribute("cookieValue", cookieValue);
		modelMap.addAttribute("fee", tuitionFeeList);
		modelMap.addAttribute("khoas", this.khoaService.getDatasttKhoatrue());
		modelMap.addAttribute("curries",this.curriculumService.getAllSttTrue());
		modelMap.addAttribute("majors", this.majorsService.getDataFromAPIsttTrue());
		return "tuitionFee/edit";
	}
	
	@PostMapping("/edit/{id}")
	public String edit(@ModelAttribute("fee") TuitionFeeList tuitionFeeList, @PathVariable("id") int id) {
		try {
			tuitionFeeListService.Update(tuitionFeeList, id);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			return "redirect:/admin/tuitionFeeList";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!" +e.toString());

			return "redirect:/admin/tuitionFeeList/edit/" + id;
		}
	}
	
	
	@GetMapping("/choose")
	public String choose(ModelMap modelMap) throws Exception {
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		modelMap.addAttribute("tuitions", this.tuitionFeeListService.getTuitionUpdateStt());
		modelMap.addAttribute("khoas", this.khoaService.getDatasttKhoatrue());
		modelMap.addAttribute("curries",this.curriculumService.getAllSttTrue());
		modelMap.addAttribute("majors", this.majorsService.getDataFromAPIsttTrue());
		return "tuitionFee/choose";
	}
	
	@PostMapping("/choose")
    public String updateStatus(@RequestParam("ids") List<Integer> ids) {
		System.out.println(ids.toString());
        try {
            tuitionFeeListService.UpdateSTT(ids);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			return "redirect:/admin/tuitionFeeList/choose";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!"+e.toString());
//			return e.toString();
			return "redirect:/admin/tuitionFeeList/choose";
		}

    }
	
	@PostMapping("/editSttFlase")
	public String edit() {
		try {
			tuitionFeeListService.UpdatesttFalse();
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			return "redirect:/admin/tuitionFeeList";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!"+e.toString());

			return "redirect:/admin/tuitionFeeList";
		}
	}
	
	@PostMapping("/import/tuitionfee")
    public String importDocumentItemsExcel(@RequestParam("file") MultipartFile file) {
		try {
        tuitionFeeListService.importTuitionFeelistFromExcel(file);
        message = new Message();
		message.setStatus("success");
		message.setMessage("Thêm danh mục học phí thành công!");
        return "redirect:/admin/tuitionFeeList";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("success");
			message.setMessage(e.toString());
	        return "redirect:/admin/tuitionFeeList";
//			return edit().toString();
		}
    }
	
	
}
