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

import es.us.isa.odin.server.converters.DocumentToJsonObjectConverter;
import es.us.isa.odin.server.converters.JsonObjectToDocumentConverter;
import es.us.isa.odin.server.converters.JsonObjectToMongoDocumentConverter;
import es.us.isa.odin.server.converters.MongoDocumentToJsonObjectConverter;
import es.us.isa.odin.server.converters.StringToDocumentURIConverter;
import es.us.isa.odin.server.converters.StringToDocumentURIConverterImpl;


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
    public StringToDocumentURIConverter stringToDocumentURIConverter() {
    	return new StringToDocumentURIConverterImpl();
    } 
    
    @Bean
    public JsonObjectToDocumentConverter<?> jsonObjectToDocumentConverter() {
    	return new JsonObjectToMongoDocumentConverter();
    }   
    
    @Bean
    public DocumentToJsonObjectConverter<?> documentToJsonObjectConverter() {
    	return new MongoDocumentToJsonObjectConverter();
    }   
    
}
