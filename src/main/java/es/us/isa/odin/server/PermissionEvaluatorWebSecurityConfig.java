package es.us.isa.odin.server;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import es.us.isa.odin.server.security.permission.DocumentPermissionResolver;
import es.us.isa.odin.server.security.permission.DocumentsPermissionEvaluator;
import es.us.isa.odin.server.security.permission.PermissionResorver;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class PermissionEvaluatorWebSecurityConfig extends GlobalMethodSecurityConfiguration {
 		
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		HashMap<String, PermissionResorver> permissionResorvers = new HashMap<String, PermissionResorver>();
		permissionResorvers.put("Document", permissionResorver());
				
		expressionHandler.setPermissionEvaluator(new DocumentsPermissionEvaluator(permissionResorvers));
		return expressionHandler;  
	}

	 @Bean
	 @Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES)
	 public PermissionResorver permissionResorver() {
		 return new DocumentPermissionResolver();
	 }

}