package es.us.isa.odin.mongoquery;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class App {

	public static void main(String[] args) {

		// For XML
		// ApplicationContext ctx = new
		// GenericXmlApplicationContext("SpringConfig.xml");

		// For Annotation
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx
				.getBean("mongoTemplate");

		System.out.println("Buscando el path perdido");
		
		Sample path1 = new Sample("//543fe519139811a3853dfffe/drive/");
		Sample path2 = new Sample("//543fe519139811a3853dfffe/drive/asd/");
		Sample path3 = new Sample("//543fe519139811a3853dfffe/drive/aasdf/");
		Sample path4 = new Sample("//543fe519139811a3853dfffe/drive/aasdf/asdaf/");
		
		mongoOperation.save(path1);
		mongoOperation.save(path2);
		mongoOperation.save(path3);
		mongoOperation.save(path4);
		
		
		Query q = new Query(Criteria.where("path").regex("^//543fe519139811a3853dfffe/drive/[^./]+/(#.+)?$"));
		
		List<Sample> paths = mongoOperation.find(q, Sample.class);
		System.out.println(paths.size());
		for(Sample s:paths) {
			System.out.println(s);
		}

	}

}