package es.us.isa.odin.server.services;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;
import es.us.isa.odin.server.security.UserAccountService;

@Service
public class MongoDocumentService implements DocumentFolderService<MongoDocument> {
	
	@Autowired
	private MongoDocumentRepository repositoy;
	
	@Autowired
	private GridFsOperations gridOperations;

	@Override
	public List<MongoDocument> listDocuments() {
		return listDocuments(null);
	}

	@Override
	public MongoDocument create() {
		return MongoDocumentService.createDocument();
	}
	
	public static MongoDocument createDocument() {
		MongoDocument doc = new MongoDocument();
		doc.setCreation(new Date());
		doc.setOwner(UserAccountService.getPrincipal().getId());
		doc.setPermissions(new HashMap<String, String>());
		
		return doc;
	}

	@Override
	public MongoDocument get(URI uri) {
		return repositoy.findOne(uri.getFragment());
	}

	@Override
	public MongoDocument save(MongoDocument doc) {
		return save(doc, null);
	}

	@Override
	public MongoDocument save(MongoDocument doc, InputStream file) {
		Date time = new Date();
		doc.setLastModification(time);
		// TODO: handle payload
		return repositoy.save(doc);
	}

	@Override
	public boolean remove(URI uri) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public InputStream getDocumentPayload(MongoDocument doc) throws NoSuchRequestHandlingMethodException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MongoDocument> listDocuments(URI uri) {
		List<MongoDocument> result = null;
		
		if(uri == null)
			result = repositoy.findByUriSchemeSpecificPartIn("//"+UserAccountService.getPrincipal().getId());
		else
			result = repositoy.findByUriSchemeSpecificPartIn(uri.getSchemeSpecificPart());
		return result;
	}

	@Override
	public void move(URI fromUri, URI toUri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void copy(URI fromUri, URI toUri) {
		// TODO Auto-generated method stub
		
	}



}
