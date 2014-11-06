package es.us.isa.odin.mongoquery;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class CopyOfApp {

	public static void main(String[] args) {

		String a = "/ruta/original/";
		String b = "/ruta/original/fichero.txt";
		String c = "/ruta/asd/";
		
		System.out.println(
			b.replace(a, c)
		);

	}

}