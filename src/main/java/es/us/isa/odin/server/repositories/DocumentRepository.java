package es.us.isa.odin.server.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import es.us.isa.odin.server.domain.Document;

@NoRepositoryBean
@SuppressWarnings("rawtypes")
public interface DocumentRepository<T extends Document> extends MongoRepository<T, String> {
	
}