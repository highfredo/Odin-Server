package es.us.isa.odin.server.switcher.mongo;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;
import es.us.isa.odin.server.security.UserAccountService;
import es.us.isa.odin.server.switcher.DocumentURIBuilder;

public class MongoDocumentURIBuilder implements DocumentURIBuilder {

	@Autowired
	private MongoDocumentRepository repository;
	
	@Override
	public URI build(String idOrPath) {
		MongoDocument doc;
		
		if(idOrPath.length() == 24 && !idOrPath.contains("/")) {
			// Es Id
			doc = repository.findOne(idOrPath);
		} else if(idOrPath.isEmpty() || idOrPath.equals("/")) {
			
			URI baseURI = null;
			try {
				baseURI = new URI("//" + UserAccountService.getPrincipal().getId() + "/");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} 
			return baseURI;
			
		} else {
			// Es Path
			String path = idOrPath;
			if(!path.endsWith("/")) path+="/";
			if(!path.startsWith("/")) path = "/" + path;
			
			String suri;
			if(path.matches("^/[a-z0-9]{24}/.*$")) {
				suri = "/" + path;
			} else {
				suri = "//" + UserAccountService.getPrincipal().getId() + path; 				
			}
			
			doc = repository.findByUriSchemeSpecificPart(suri);
		}
		
		return doc.getUri().resolve("#" + doc.getId());
	}

}
