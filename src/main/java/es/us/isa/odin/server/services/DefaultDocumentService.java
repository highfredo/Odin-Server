package es.us.isa.odin.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
	
	@PreAuthorize("hasPermission(#id, 'Documents', 'DOCUMENTS_READ')")
	public String va(Long id){
		return "HOLA";
	}
	
	@PreAuthorize("hasPermission(#id, 'Documents', 'DOCUMENTS_WRITE')")
	public String nova(Long id){
		return "ADIOS";
	}
	
}
