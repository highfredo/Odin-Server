package es.us.isa.odin.server.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.us.isa.odin.server.domain.MongoDocument;

@Repository
public interface MongoDocumentRepository extends DocumentRepository<MongoDocument> {

	public List<MongoDocument> findByPathStartsWith(String basePath);

	public List<MongoDocument> findByPath(String path);	
}
