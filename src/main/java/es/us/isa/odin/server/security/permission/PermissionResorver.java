package es.us.isa.odin.server.security.permission;

import es.us.isa.odin.server.security.UserAccount;

public interface PermissionResorver {

	boolean isAllowed(UserAccount principal, Object targetDomainObject, String permission);
	boolean isAllowed(UserAccount principal, String targetId, String permission);

}
