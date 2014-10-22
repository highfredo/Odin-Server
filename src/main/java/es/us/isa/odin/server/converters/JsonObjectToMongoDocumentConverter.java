package es.us.isa.odin.server.converters;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;
import es.us.isa.odin.server.services.MongoDocumentService;

public class JsonObjectToMongoDocumentConverter implements JsonObjectToDocumentConverter<MongoDocument> {

	@Autowired
	private MongoDocumentRepository repository;
	
	@Override
	public MongoDocument convert(JSONObject source) {
		MongoDocument document;
		if(source.has("id")) {
			document = repository.findOne(source.getString("id"));
			document.setRevision(source.getLong("revision"));
		} else {
			document = MongoDocumentService.createDocument();
		}

		URI uri = null;
		try {
			uri = new URI("//"+document.getOwner() + source.getString("path"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		document.setUri(uri);
		document.setName(source.getString("name"));
		//document.setDescription(source.getString("description"));
		// document.setType(type);
		// document.setMetadata((Map<String, Object>) source.get("metadata"));
		
		return document;
	}

}
