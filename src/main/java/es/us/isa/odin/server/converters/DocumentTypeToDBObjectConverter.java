package es.us.isa.odin.server.converters;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import es.us.isa.odin.server.domain.documenttype.DocumentType;

public class DocumentTypeToDBObjectConverter implements Converter<DocumentType, DBObject> {

	@Override
	public DBObject convert(DocumentType source) {
		DBObject dbo = new BasicDBObject();
		dbo.put("type", source.getType());
		return dbo;
	}

}
