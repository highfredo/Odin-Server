package es.us.isa.odin.server.converters;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;

public class StringToDocumentURIConverterImpl implements StringToDocumentURIConverter {

	@Autowired
	private MongoDocumentRepository repository;
	
	@Override
	public URI convert(String idOrPath) {
		URI uri = null;
		
		if(idOrPath.length() == 24 && !idOrPath.contains("/")) {
			// Es Id
			MongoDocument doc = repository.findOne(idOrPath);
			if(doc == null) return null;
			
			try {
				uri = new URI(doc.getUri().toString()+"#"+idOrPath);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			// Es Path
			// TODO:
		}
		
		return uri;
	}

}
