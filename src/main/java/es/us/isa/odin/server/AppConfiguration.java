package es.us.isa.odin.server;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.mongodb.DBObject;

import es.us.isa.odin.server.converters.DBObjectToDocumentTypeConverter;
import es.us.isa.odin.server.converters.DBObjectToFileDocumentTypeConverter;
import es.us.isa.odin.server.converters.DocumentTypeToDBObjectConverter;
import es.us.isa.odin.server.converters.FileDocumentTypeToDBObjectConverter;
import es.us.isa.odin.server.domain.documenttype.DocumentTypes;
import es.us.isa.odin.server.switcher.DocumentSwitcherJsonObject;
import es.us.isa.odin.server.switcher.DocumentURIBuilder;
import es.us.isa.odin.server.switcher.JsonObjectSwitcherDocument;
import es.us.isa.odin.server.switcher.mongo.JsonObjectSwitcherMongoDocument;
import es.us.isa.odin.server.switcher.mongo.MongoDocumentSwitcherJsonObject;
import es.us.isa.odin.server.switcher.mongo.MongoDocumentURIBuilder;


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class AppConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired
	private Environment env;
	
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("128MB");
        factory.setMaxRequestSize("128MB");
        return factory.createMultipartConfig();
    } 
    
    @Bean
    public JsonOrgModule jsonOrgModule() {
    	return new JsonOrgModule();
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	if(env.acceptsProfiles("dev")) {
    		registry.addResourceHandler("/**").addResourceLocations("classpath:/app/");
    		registry.addResourceHandler("/bower_components/**").addResourceLocations("classpath:/bower_components/");
    	} else {
    		registry.addResourceHandler("/**").addResourceLocations("classpath:/dist/");
    	}
    }
    
    @Bean
    public JsonObjectSwitcherDocument<?> jsonObjectSwitcherDocument() {
    	return new JsonObjectSwitcherMongoDocument();
    }   
    
    @Bean
    public DocumentSwitcherJsonObject<?> documentSwitcherJsonObject() {
    	return new MongoDocumentSwitcherJsonObject();
    } 
    
    @Bean
    public DocumentURIBuilder documentURIBuilder() {
    	return new MongoDocumentURIBuilder();
    }
    
    @Bean
    public DocumentTypes documentTypes() {
    	return new DocumentTypes();
    }

}
