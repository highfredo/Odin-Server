package es.us.isa.odin.server.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.us.isa.odin.server.domain.MongoDocument;

@Repository
public interface MongoDocumentRepository extends DocumentRepository<MongoDocument> {

	public List<MongoDocument> findByPathStartsWithAndOwner(String basePath, String owner);

	public List<MongoDocument> findByPathAndOwner(String path, String owner);	
}
