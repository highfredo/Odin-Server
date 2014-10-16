package es.us.isa.odin.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class IndexController {

	
	@RequestMapping("/")
	public String view() {
		return "forward:index.html";
	}
	/*
	@RequestMapping("/sample")
	public String sample() {
		System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		return "index";
	}*/
	
}