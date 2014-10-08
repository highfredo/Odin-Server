package es.us.isa.odin.server.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import es.us.isa.odin.server.repositorycore.IDocumentRepository;

@Repository
public interface DefaultDocumentRepository extends IDocumentRepository<Object> {

	//@Query("")
	//public String getPermissionsForUser(String userId);
	
}
