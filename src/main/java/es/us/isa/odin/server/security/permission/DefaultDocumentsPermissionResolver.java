package es.us.isa.odin.server.security.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import es.us.isa.odin.server.domain.Documents;
import es.us.isa.odin.server.repositories.DefaultDocumentRepository;
import es.us.isa.odin.server.security.UserAccount;
import es.us.isa.odin.server.security.permission.Permissions.DOCUMENTS;

public class DefaultDocumentsPermissionResolver implements PermissionResorver {

	@Autowired
	private DefaultDocumentRepository defaultDocumentRepository;
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean isAllowed(UserDetails principal, Object targetDomainObject, String permission) {
		return isAllowed(principal, (Documents) targetDomainObject, permission);
	}

	@Override
	public boolean isAllowed(UserDetails principal, Long targetId, String permission) {
		boolean allowed = false;
		
		switch (permission) {
		case DOCUMENTS.READ:
			allowed = true; //ver(principal, targetId);
			break;		
		
		case DOCUMENTS.WRITE:
			allowed = false; //ver(principal, targetId);
			break;		
		
		default:
			System.err.println("PERMISO NO ENCONTRADO: " +  permission);
			break;
		}
		
		return allowed;
	}

}
