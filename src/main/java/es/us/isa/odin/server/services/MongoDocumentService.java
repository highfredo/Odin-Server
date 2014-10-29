package es.us.isa.odin.server.services;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

import es.us.isa.odin.mongoquery.Sample;
import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;
import es.us.isa.odin.server.security.UserAccountService;

@Service
public class MongoDocumentService implements DocumentService<MongoDocument> {
	
	@Autowired
	private MongoDocumentRepository repositoy;
	
	@Autowired
	private GridFsOperations gridOperations;
	
	@Autowired
	private MongoOperations mongoOperation;
	
	@Override
	public MongoDocument create() {
		return createMongoDocument();
	}
	
	public static MongoDocument createMongoDocument() {
		MongoDocument doc = new MongoDocument();
		doc.setOwner(UserAccountService.getPrincipal().getId());
		doc.setCreation(new Date());
		doc.setMetadata(new HashMap<String, Object>());
		doc.setPermissions(new HashMap<String, String>());
		
		return doc;
	}
	
	@Override
	public List<MongoDocument> listDocuments() {
		URI uri = null;
		try {
			uri = new URI("//" + UserAccountService.getPrincipal().getId() + "/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return listDocuments(uri);
	}
	
	@Override
	public List<MongoDocument> listDocuments(URI uri) {
		String regexp = "^"+ uri.getSchemeSpecificPart().replaceAll(" ", "%20") +"[^w+/]+/(#.+)?$";
		Query q = new Query(Criteria.where("uri.schemeSpecificPart").regex(regexp));
		return mongoOperation.find(q, MongoDocument.class);
	}
		

	@Override
	//@PreAuthorize("hasPermission(#id, 'MongoDocument', 'DOCUMENT_WRITE')")
	public MongoDocument save(MongoDocument doc) {
		Date time = new Date();
		if(doc.getOwner() == null) {
			doc.setOwner(UserAccountService.getPrincipal().getId());
			doc.setCreation(time);
		}
		doc.setLastModification(time);
		if(!doc.getPath().endsWith("/")) doc.setPath(doc.getPath()+"/");
		
		return repositoy.save(doc);
	}

	@Override
	//@PreAuthorize("hasPermission(#id, 'MongoDocument', 'DOCUMENT_WRITE')")
	public MongoDocument save(MongoDocument doc, InputStream file) throws NoSuchRequestHandlingMethodException {		
		GridFSFile oldFsFile = gridOperations.findOne(findFsFileById(doc.getPayload()));
		
		GridFSFile fsFile = gridOperations.store(file, doc.getId());   
		
		if(oldFsFile != null) {
			gridOperations.delete(findFsFileById(doc.getPayload()));
		}
		
		doc.setPayload(fsFile.getId().toString());
		doc.setLength(fsFile.getLength());
		this.save(doc);
		
		return doc;
	}
	
	@Override
	//@PreAuthorize("hasPermission(#id, 'MongoDocument', 'DOCUMENT_WRITE')")
	public boolean remove(URI uri) {
		try {
			MongoDocument doc = repositoy.findOne(uri.getFragment());
			return this.remove(doc);
		} catch(Exception e) {
			return false;
		}
	}
	
	//@PreAuthorize("hasPermission(#id, 'MongoDocument', 'DOCUMENT_WRITE')")
	public boolean remove(MongoDocument doc) {
		try {
			if(doc.isFolder()) {
				List<MongoDocument> toDelete = this.listAllDocuments(doc.getPath());
				if(toDelete.isEmpty() == false) {
					for(MongoDocument d : toDelete) {
						repositoy.delete(d.getId());
					}
				}
				repositoy.delete(doc.getId());
			} else {
				gridOperations.delete(findFsFileById(doc.getPayload()));
				repositoy.delete(doc.getId());
			}
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	@Override
	//@PreAuthorize("hasPermission(#id, 'MongoDocument', 'DOCUMENT_READ')")
	public MongoDocument get(URI uri) {
		return repositoy.findOne(uri.getFragment());
	}
	

	@Override
	//@PreAuthorize("hasPermission(#id, 'MongoDocument', 'DOCUMENT_READ')")
	public InputStream getDocumentPayload(URI uri) throws NoSuchRequestHandlingMethodException {
		MongoDocument doc = repositoy.findOne(uri.getFragment());
		
		if(doc == null || StringUtils.isEmpty(doc.getPayload()))
			throw new NoSuchRequestHandlingMethodException(uri.getFragment() + " Documento no encontrado", this.getClass());

		GridFSDBFile fsdbFile = gridOperations.findOne(findFsFileById(doc.getPayload()));
		
		return fsdbFile.getInputStream();
	}

	
	/*
	@Override
	@PreAuthorize("hasPermission(#id, 'Document', 'DOCUMENT_WRITE') && hasPermission(#to, 'Document', 'DOCUMENT_WRITE')")
	public void move(String id, String to) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	@PreAuthorize("hasPermission(#id, 'Document', 'DOCUMENT_READ') && hasPermission(#to, 'Document', 'DOCUMENT_WRITE')")
	public void copy(String id, String to) {
		// TODO Auto-generated method stub
		
	}
	*/
	
	private Query findFsFileById(String fsid) {
		return Query.query(Criteria.where("_id").is(fsid));
	}

	private List<MongoDocument> listAllDocuments(String path) {
		return repositoy.findByUriSchemeSpecificPartStartsWith(path);
	}

	
}
