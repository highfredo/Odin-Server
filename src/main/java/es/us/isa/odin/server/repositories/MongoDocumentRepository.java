package es.us.isa.odin.server.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import es.us.isa.odin.server.domain.MongoDocument;

@Repository
public interface MongoDocumentRepository extends DocumentRepository<MongoDocument> {
	
	public MongoDocument findByUriSchemeSpecificPart(String uri);
	public List<MongoDocument> findByUriSchemeSpecificPartStartsWith(String uri);
}
