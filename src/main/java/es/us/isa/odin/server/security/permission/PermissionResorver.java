package es.us.isa.odin.server.security.permission;

import java.io.Serializable;

import es.us.isa.odin.server.security.UserAccount;

public interface PermissionResorver {

	boolean isAllowed(UserAccount principal, Object targetDomainObject, String permission);
	boolean isAllowed(UserAccount principal, Serializable targetId, String permission);

}
