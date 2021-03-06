package es.us.isa.odin.server.switcher.mongo;

import org.json.JSONObject;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.domain.documenttype.FileDocumentType;
import es.us.isa.odin.server.security.UserAccountService;
import es.us.isa.odin.server.switcher.DocumentSwitcherJsonObject;

public class MongoDocumentSwitcherJsonObject implements DocumentSwitcherJsonObject<MongoDocument> {

	@Override
	public JSONObject convert(MongoDocument source, String scope) {
	
		JSONObject result = new JSONObject();
		result.put("id", source.getId());
		result.put("revision", source.getRevision());
		result.put("name", source.getName());
		result.put("path", source.getUri().getPath());
		result.put("description", source.getDescription());
		result.put("type", source.getType().getType());
		result.put("metadata", source.getMetadata());
		result.put("lastModification", source.getLastModification());
		result.put("hasFile", source.getPayload() != null);
		result.put("owner", source.getOwner());
		result.put("length", source.getLength());
		
		if(source.getType() instanceof FileDocumentType == false)
			result.put("payload", source.getPayload());
		
		if(source.getPermissions() != null) {
			String yourId = UserAccountService.getPrincipal().getId();
			if(source.getPermissions().get(yourId) != null) {
				result.put("yourPermissions", source.getPermissions().get(yourId));
			} else if(source.getPermissions().get("public") != null) {
				result.put("yourPermissions", source.getPermissions().get("public"));
			}
		}
		
		return result;
	}

}
