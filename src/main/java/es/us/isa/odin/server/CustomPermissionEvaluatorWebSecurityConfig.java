package es.us.isa.odin.server;

import java.util.HashMap;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import es.us.isa.odin.server.security.permission.DefaultDocumentsPermissionResolver;
import es.us.isa.odin.server.security.permission.DocumentsPermissionEvaluator;
import es.us.isa.odin.server.security.permission.PermissionResorver;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class CustomPermissionEvaluatorWebSecurityConfig extends GlobalMethodSecurityConfiguration {
 

	  @Override
	  protected MethodSecurityExpressionHandler createExpressionHandler() {
			DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
			HashMap<String, PermissionResorver> permissionResorvers = new HashMap<String, PermissionResorver>();
			permissionResorvers.put("Documents", new DefaultDocumentsPermissionResolver());
			
			expressionHandler.setPermissionEvaluator(new DocumentsPermissionEvaluator(permissionResorvers));
			return expressionHandler;  
	 }


}