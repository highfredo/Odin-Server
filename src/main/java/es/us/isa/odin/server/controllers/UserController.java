package es.us.isa.odin.server.controllers;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.us.isa.odin.server.security.UserAccountService;

@RestController
@RequestMapping("/user")
public class UserController {

	@RequestMapping(value="/me", method=RequestMethod.GET)
	public JSONObject get() {
		JSONObject result = new JSONObject();
		try {
			result.put("username", UserAccountService.getPrincipal().getUsername());
		} catch(Exception e) {
			result.put("username", "anon");
		}
		
		return result;
	}
	
}
