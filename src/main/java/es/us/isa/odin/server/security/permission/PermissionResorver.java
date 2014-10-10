package es.us.isa.odin.server.security.permission;

import org.springframework.security.core.userdetails.UserDetails;

public interface PermissionResorver {

	boolean isAllowed(UserDetails principal, Object targetDomainObject, String permission);
	boolean isAllowed(UserDetails principal, Long targetId, String permission);

}
