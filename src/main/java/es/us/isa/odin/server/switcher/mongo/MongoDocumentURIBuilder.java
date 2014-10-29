package es.us.isa.odin.server.switcher.mongo;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;
import es.us.isa.odin.server.security.UserAccountService;
import es.us.isa.odin.server.switcher.DocumentURIBuilder;

public class MongoDocumentURIBuilder implements DocumentURIBuilder {

	@Autowired
	private MongoDocumentRepository repository;
	
	@Override
	public URI build(String idOrPath) throws NoSuchRequestHandlingMethodException {
		MongoDocument doc;
		
		if(idOrPath.length() == 24 && !idOrPath.contains("/")) {
			// Es Id
			doc = repository.findOne(idOrPath);
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
		
		if(doc == null)
			throw new NoSuchRequestHandlingMethodException(idOrPath + " Documento no encontrado", this.getClass());
		
		return doc.getUri().resolve("#" + doc.getId());
	}

}
