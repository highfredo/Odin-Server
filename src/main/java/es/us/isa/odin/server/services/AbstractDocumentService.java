package es.us.isa.odin.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import es.us.isa.odin.server.domain.Documents;
import es.us.isa.odin.server.repositorycore.IDocumentRepository;

public abstract class AbstractDocumentService<T> {

	@Autowired
	protected MongoOperations operations;
	
	public abstract IDocumentRepository<T> repository();
	
	public Documents<T> save(Documents<T> document) {
		return repository().save(document);
	}
	
	public List<Documents<T>> save(Iterable<Documents<T>> documents) {
		return repository().save(documents);
	}

	public Documents<T> findOne(String id) {
		return repository().findOne(id);
	}

	public Iterable<Documents<T>> findAll(Iterable<String> ids) {
		return repository().findAll(ids);
	}
	
	public List<Documents<T>> findAll() {
		return repository().findAll();
	}
	
	public void delete(String id) {
		repository().delete(id);
	}
	
	public void delete(Documents<T> document) {
		repository().delete(document);
	}
	
	public void delete(Iterable<Documents<T>> documents) {
		repository().delete(documents);
	}

	public long count() {
		return repository().count();
	}

	public boolean exists(String id) {
		return repository().exists(id);
	}

}
