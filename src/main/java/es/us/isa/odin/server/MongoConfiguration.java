package es.us.isa.odin.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import es.us.isa.odin.server.converters.DBObjectToDocumentTypeConverter;
import es.us.isa.odin.server.converters.DBObjectToFileDocumentTypeConverter;
import es.us.isa.odin.server.converters.DocumentTypeToDBObjectConverter;
import es.us.isa.odin.server.converters.FileDocumentTypeToDBObjectConverter;

@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "odindb";
	}

	@Override
	protected UserCredentials getUserCredentials() {
		return new UserCredentials("odin", "odin");
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient("ds057538.mongolab.com", 57538);
	}

	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public CustomConversions customConversions() {
		List<Converter> converters = new ArrayList<Converter>();
		converters.add(dbObjectToDocumentTypeConverter());
		converters.add(dbObjectToFileDocumentTypeConverter());
		converters.add(documentTypeToDBObjectConverter());
		converters.add(fileDocumentTypeToDBObjectConverter());
		return new CustomConversions(converters);
	}

    @Bean
    public DBObjectToDocumentTypeConverter dbObjectToDocumentTypeConverter() {
    	return new DBObjectToDocumentTypeConverter();
    }
    
    @Bean
    public DBObjectToFileDocumentTypeConverter dbObjectToFileDocumentTypeConverter() {
    	return new DBObjectToFileDocumentTypeConverter();
    }
    
    @Bean
    public DocumentTypeToDBObjectConverter documentTypeToDBObjectConverter() {
    	return new DocumentTypeToDBObjectConverter();
    }
    
    @Bean
    public FileDocumentTypeToDBObjectConverter fileDocumentTypeToDBObjectConverter() {
    	return new FileDocumentTypeToDBObjectConverter();
    }

}