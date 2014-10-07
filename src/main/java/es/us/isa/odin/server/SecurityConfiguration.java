package es.us.isa.odin.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
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
