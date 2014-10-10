package es.us.isa.odin.server.security.permission;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import es.us.isa.odin.server.domain.Document;
import es.us.isa.odin.server.security.UserAccount;
import es.us.isa.odin.server.security.UserAccountRepository;

public class DocumentsPermissionEvaluator implements PermissionEvaluator {
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	private Map<String, PermissionResorver> resolvers;
	
	public DocumentsPermissionEvaluator() {
		resolvers = new HashMap<String, PermissionResorver>();
	}
	
	public DocumentsPermissionEvaluator(Map<String, PermissionResorver> resolvers) {
		this.resolvers = resolvers;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		Document entity = (Document) targetDomainObject;
		return hasPermission(authentication, entity.getId(), entity.getClass().getSimpleName(), permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		boolean hasPermission = true;
		boolean isAllowed = false;
		
		if (canHandle(authentication, permission)) {
			UserDetails user = (UserDetails) authentication.getPrincipal();
			
			// Check si el usuario tiene ese permiso
			// hasPermission = user.getPermissions().contains(permission); TODO:

			// Check si el permiso es valido
			if(hasPermission) { 
				PermissionResorver resolver = resolvers.get(targetType);
				if(resolver != null) 
					isAllowed = resolver.isAllowed(user, (Long) targetId, (String) permission);
				else
					System.err.println("RESOLVER NOT FOUND");
			}
		}
		
		return isAllowed;
	}

	
	private boolean canHandle(Authentication authentication, Object permission) {
		return authentication != null && permission instanceof String && (authentication.getPrincipal() instanceof UserDetails);
	}
	
	
	// GETTERS & SETTER
	public Map<String, PermissionResorver> getResolvers() {
		return resolvers;
	}

	public void setResolvers(Map<String, PermissionResorver> resolvers) {
		this.resolvers = resolvers;
	}
	
	

}
