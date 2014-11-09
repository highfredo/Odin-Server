package es.us.isa.odin.mongoquery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import es.us.isa.odin.server.domain.documenttype.DocumentTypes;

@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {

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

/*
	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}
*/
    
    @Bean
    public DocumentTypes documentTypes() {
    	return new DocumentTypes();
    }
}