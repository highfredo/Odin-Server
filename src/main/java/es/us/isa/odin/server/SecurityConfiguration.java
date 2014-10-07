package es.us.isa.odin.server;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import es.us.isa.odin.server.security.permission.DefaultDocumentsPermissionResolver;
import es.us.isa.odin.server.security.permission.DocumentsPermissionEvaluator;
import es.us.isa.odin.server.security.permission.PermissionResorver;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/**").authenticated()
								.and().formLogin();
	}
	
	/*
	@Bean 
	public DefaultMethodSecurityExpressionHandler expressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		HashMap<String, PermissionResorver> permissionResorvers = new HashMap<String, PermissionResorver>();
		permissionResorvers.put("Documents", new DefaultDocumentsPermissionResolver());
		
		expressionHandler.setPermissionEvaluator(new DocumentsPermissionEvaluator(permissionResorvers));
		return expressionHandler;
	}*/
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.inMemoryAuthentication()
	        .withUser("user").password("password").roles("USER").and()
	        .withUser("admin").password("password").roles("USER", "ADMIN");
	}

}
