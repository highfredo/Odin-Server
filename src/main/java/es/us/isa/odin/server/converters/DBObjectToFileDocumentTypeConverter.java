package es.us.isa.odin.server.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.mongodb.DBObject;

import es.us.isa.odin.server.domain.documenttype.DocumentTypes;
import es.us.isa.odin.server.domain.documenttype.FileDocumentType;

public class DBObjectToFileDocumentTypeConverter implements Converter<DBObject, FileDocumentType>{
	
	@Autowired
	private DocumentTypes types;
	
	@Override
	public FileDocumentType convert(DBObject source) {
		String type = (String) source.get("type");
		String subType = (String) source.get("subtype");
		
		if(subType == null)
			return (FileDocumentType) DocumentTypes.FILE;
		
		return (FileDocumentType) types.get(type + "::" + subType);
	}

}
