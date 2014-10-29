package es.us.isa.odin.server.controllers;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.us.isa.odin.server.security.UserAccountService;

@RestController
@RequestMapping("/user")
public class UserController {

	@RequestMapping(value="", method=RequestMethod.GET)
	public JSONObject get() {
		JSONObject result = new JSONObject();
		result.put("username", UserAccountService.getPrincipal().getUsername());
		
		return result;
	}
	
}
