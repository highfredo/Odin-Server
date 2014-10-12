package es.us.isa.odin.server.services;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.repositories.MongoDocumentRepository;
import es.us.isa.odin.server.security.UserAccountService;

@Service
public class MongoDocumentService implements DocumentService<MongoDocument> {
	
	@Autowired
	private MongoDocumentRepository repositoy;
	
	@Autowired
	private GridFsOperations gridOperations;

	@Override
	public List<MongoDocument> listDocuments(String path) {
		return repositoy.findByPathStartsWith(path);
	}

	@Override
	public MongoDocument save(MongoDocument doc) {
		doc.setOwner(UserAccountService.getPrincipal().getId());
		return repositoy.save(doc);
	}

	@Override
	public boolean remove(String id) {
		MongoDocument doc = repositoy.findOne(id);
		gridOperations.delete(findFsFileById(doc.getPayload()));
		repositoy.delete(id);
		
		return repositoy.findOne(id) != null;
	}
	
	@Override
	@PreAuthorize("hasPermission(#id, 'Document', 'DOCUMENT_READ')")
	//@PreAuthorize("hasRole('USER')")
	public MongoDocument get(String id) {
		return repositoy.findOne(id);
	}
	
	@Override
	public void move(String id, String to) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void copy(String id, String to) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputStream getDocumentPayload(String id) {
		MongoDocument doc = repositoy.findOne(id);
		GridFSDBFile fsdbFile = gridOperations.findOne(findFsFileById(doc.getPayload()));
		
		return fsdbFile.getInputStream();
	}

	@Override
	public MongoDocument saveDocumentPayload(String id, InputStream file) {
		MongoDocument doc = repositoy.findOne(id);
		
		GridFSFile oldFsFile = gridOperations.findOne(findFsFileById(doc.getPayload()));
	 
		GridFSFile fsFile = gridOperations.store(file, id);   
		
		if(oldFsFile != null) {
			gridOperations.delete(findFsFileById(doc.getPayload()));
		}
		
        doc.setPayload(fsFile.getId().toString());
        this.save(doc);
                
		return doc;
	}
	
	private Query findFsFileById(String fsid) {
		return Query.query(Criteria.where("_id").is(fsid));
	}




	

}
