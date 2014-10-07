package es.us.isa.odin.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.isa.odin.server.services.DefaultDocumentService;


@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	private DefaultDocumentService defaultDocumentService;
	
	@RequestMapping
	public String view() {
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		return "index";
	}
	
	
	@RequestMapping("/create")
	@ResponseBody
	public String create() {
		
		return "OK";
	}
	
	@RequestMapping("/va")
	@ResponseBody
	public String va() {
		defaultDocumentService.va(123L);
		return "OK";
	}
	
	@RequestMapping("/nova")
	@ResponseBody
	public String nova() {
		defaultDocumentService.nova(123L);
		return "OK";
	}


}