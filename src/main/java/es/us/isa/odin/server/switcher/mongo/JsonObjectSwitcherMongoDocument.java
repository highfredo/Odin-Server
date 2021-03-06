package es.us.isa.odin.server.switcher.mongo;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.domain.documenttype.DocumentTypes;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;
import es.us.isa.odin.server.services.MongoDocumentService;
import es.us.isa.odin.server.switcher.JsonObjectSwitcherDocument;

public class JsonObjectSwitcherMongoDocument implements JsonObjectSwitcherDocument<MongoDocument> {

	@Autowired
	private MongoDocumentRepository repository;
	@Autowired
	private DocumentTypes documentTypes;
	
	@Override
	public MongoDocument convert(JSONObject source, String scope) {
		
		MongoDocument document;
		if(source.has("id")) {
			document = repository.findOne(source.getString("id"));
			document.setRevision(source.getLong("revision"));
		} else {
			document = MongoDocumentService.createMongoDocument();
		}

		if(source.has("name")) 
			document.setName(source.getString("name"));
		
		if(source.has("path"))
			document.setPath(source.getString("path"));
		else if(document.getPath() == null)
			document.setPath("/");
		
		if(source.has("description"))
			document.setDescription(source.getString("description"));
				
		if(source.has("type"))
			document.setType(documentTypes.get(source.getString("type")));
		
		if(source.has("payload")) {
			document.setPayload(source.getString("payload"));
		}
		
		
		return document;
	}

}
