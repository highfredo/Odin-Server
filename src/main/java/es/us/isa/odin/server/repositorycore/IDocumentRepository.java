package es.us.isa.odin.server.repositorycore;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import es.us.isa.odin.server.domain.Documents;

@NoRepositoryBean
public interface IDocumentRepository<T> extends MongoRepository<Documents<T>, String> {

}