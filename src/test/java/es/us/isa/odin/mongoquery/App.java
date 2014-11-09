package es.us.isa.odin.mongoquery;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import es.us.isa.odin.server.domain.documenttype.DocumentType;
import es.us.isa.odin.server.domain.documenttype.DocumentTypes;
import es.us.isa.odin.server.domain.documenttype.FileDocumentType;

public class App {

	public static void main(String[] args) {

		// For Annotation
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		System.out.println("Pruebas");
		DocumentTypes documentTypes = (DocumentTypes) ctx.getBean("documentTypes");
		documentTypes.register("file::json", new FileDocumentType("json"));
		
		DocumentType tFile1 = DocumentTypes.FILE;
		Sample s1 = new Sample(tFile1);
		mongoOperation.save(s1);
		
		
		DocumentType tFile2 = mongoOperation.findById(s1.getId(), Sample.class).getType();
		DocumentType tJson = documentTypes.getFromMimeType("json");
		
		System.out.println(tFile1 == tFile2);

	}

}




