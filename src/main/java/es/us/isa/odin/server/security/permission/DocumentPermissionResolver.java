package es.us.isa.odin.server.security.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.us.isa.odin.server.domain.Document;
import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;
import es.us.isa.odin.server.security.UserAccount;
import es.us.isa.odin.server.security.permission.Permissions.DOCUMENT;

@Component
public class DocumentPermissionResolver implements PermissionResorver {

	@Autowired
	private MongoDocumentRepository repository;
	
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean isAllowed(UserAccount principal, Object targetDomainObject, String permission) {
		return isAllowed(principal, (Document) targetDomainObject, permission);
	}

	@Override
	public boolean isAllowed(UserAccount principal, String targetId, String permission) {
		boolean allowed = false;
		
		MongoDocument doc = repository.findOne(targetId);
		
		switch (permission) {
		case DOCUMENT.READ:
			allowed = doc.getOwner().equals(principal.getId()) || doc.getPermissions().get(principal.getId()).contains("r");
			break;		
		
		case DOCUMENT.WRITE:
			allowed = doc.getOwner().equals(principal.getId()) || doc.getPermissions().get(principal.getId()).contains("w");
			break;		
		
		default:
			System.err.println("PERMISO NO ENCONTRADO: " +  permission);
			break;
		}
		
		return allowed;
	}
	
}
