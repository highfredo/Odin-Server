package es.us.isa.odin.server.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.mongodb.DBObject;

import es.us.isa.odin.server.domain.documenttype.DocumentType;
import es.us.isa.odin.server.domain.documenttype.DocumentTypes;

public class DBObjectToDocumentTypeConverter implements Converter<DBObject, DocumentType>  {

	@Autowired
	private DocumentTypes types;
	
	@Override
	public DocumentType convert(DBObject source) {
		String type = (String) source.get("type");
		
		return types.get(type);
	}

}
