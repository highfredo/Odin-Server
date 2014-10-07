package es.us.isa.odin.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.isa.odin.server.repositories.DefaultDocumentRepository;
import es.us.isa.odin.server.repositorycore.IDocumentRepository;

@Service
@SuppressWarnings("rawtypes")
public class DefaultDocumentService extends AbstractDocumentService<Object> {

	@Autowired
	private DefaultDocumentRepository documentRepository;	

	
	@Override
	@SuppressWarnings("unchecked")
	public IDocumentRepository repository() {
		return documentRepository;
	}
	
}
