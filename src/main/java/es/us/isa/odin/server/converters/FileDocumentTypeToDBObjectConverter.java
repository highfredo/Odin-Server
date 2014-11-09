package es.us.isa.odin.server.converters;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import es.us.isa.odin.server.domain.documenttype.FileDocumentType;

public class FileDocumentTypeToDBObjectConverter implements Converter<FileDocumentType, DBObject> {

	@Override
	public DBObject convert(FileDocumentType source) {
		DBObject dbo = new BasicDBObject();
		dbo.put("type", source.getType());
		dbo.put("subtype", source.getSubType());
		return dbo;
	}

}
