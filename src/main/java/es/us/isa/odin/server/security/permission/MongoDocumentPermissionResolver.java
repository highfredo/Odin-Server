package es.us.isa.odin.server.security.permission;

import java.io.Serializable;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;
import es.us.isa.odin.server.security.UserAccount;
import es.us.isa.odin.server.security.permission.Permissions.DOCUMENT;

@Component
public class MongoDocumentPermissionResolver implements PermissionResorver {

	@Autowired
	private MongoDocumentRepository repository;
	
	
	@Override
	public boolean isAllowed(UserAccount principal, Object targetDomainObject, String permission) {
		return isAllowed(principal, ((MongoDocument) targetDomainObject).getId(), permission);
	}

	@Override
	public boolean isAllowed(UserAccount principal, Serializable targetId, String permission) {
		boolean allowed = false;
		
		if(targetId == null) return true;
		
		MongoDocument doc = null;
		if(targetId instanceof String) {
			doc = repository.findOne((String) targetId);
		} else if(targetId instanceof URI) {
			URI targetURI = (URI) targetId;
			doc = repository.findOne(targetURI.getFragment());			
		}
		
		
		if(doc == null) return true;
		
		switch (permission) {
		case DOCUMENT.READ:
			allowed = doc.getOwner().equals(principal.getId()) || 
				(doc.getPermissions()!=null && doc.getPermissions().get(principal.getId())!=null && doc.getPermissions().get(principal.getId()).contains("r")) ||
				(doc.getPermissions()!=null && doc.getPermissions().get("public")!=null && doc.getPermissions().get("public").contains("r"));
			break;		
		
		case DOCUMENT.WRITE:
			allowed = doc.getOwner().equals(principal.getId()) || 
				(doc.getPermissions()!=null && doc.getPermissions().get(principal.getId())!=null && doc.getPermissions().get(principal.getId()).contains("w")) ||
				(doc.getPermissions()!=null && doc.getPermissions().get("public")!=null && doc.getPermissions().get("public").contains("w"));
			break;		
		
		default:
			System.err.println("PERMISO NO ENCONTRADO: " +  permission);
			break;
		}
		
		return allowed;
	}
	
}
