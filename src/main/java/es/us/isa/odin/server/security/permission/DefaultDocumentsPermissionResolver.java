package es.us.isa.odin.server.security.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import es.us.isa.odin.server.domain.Document;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;
import es.us.isa.odin.server.security.permission.Permissions.DOCUMENTS;

public class DefaultDocumentsPermissionResolver implements PermissionResorver {

	@Autowired
	private MongoDocumentRepository defaultDocumentRepository;
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean isAllowed(UserDetails principal, Object targetDomainObject, String permission) {
		return isAllowed(principal, (Document) targetDomainObject, permission);
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
