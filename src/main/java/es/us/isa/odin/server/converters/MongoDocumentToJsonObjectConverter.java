package es.us.isa.odin.server.converters;

import org.json.JSONObject;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.security.UserAccountService;

public class MongoDocumentToJsonObjectConverter implements DocumentToJsonObjectConverter<MongoDocument> {

	
	@Override
	public JSONObject convert(MongoDocument source) {
		JSONObject result = new JSONObject();
		result.put("path", source.getUri().getPath());
		result.put("revision", source.getRevision());
		result.put("name", source.getName());
		result.put("description", source.getDescription());
		result.put("metadata", source.getMetadata());
		result.put("lastModification", source.getLastModification());
		result.put("owner", source.getOwner());
		result.put("type", source.getType());
		result.put("length", source.getLength());
		
		String yourId = UserAccountService.getPrincipal().getId();
		if(source.getPermissions().get(yourId) != null) {
			result.put("yourPermissions", source.getPermissions().get(yourId));
		} else if(source.getPermissions().get("public") != null) {
			result.put("yourPermissions", source.getPermissions().get("public"));
		}
		
		return result;
	}

}
