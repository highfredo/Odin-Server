package es.us.isa.odin.server.services;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

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
	public MongoDocument create() {
		return createMongoDocument();
	}
	
	public static MongoDocument createMongoDocument() {
		MongoDocument doc = new MongoDocument();
		doc.setOwner(UserAccountService.getPrincipal().getId());
		doc.setCreation(new Date());
		
		return doc;
	}
	
	@Override
	public List<MongoDocument> listDocuments(String path) {
		if(!path.endsWith("/")) path+="/";
		return repositoy.findByPathAndOwner(path, UserAccountService.getPrincipal().getId());
	}
	
	@Override
	public List<MongoDocument> listAllDocuments(String path) {
		if(!path.endsWith("/")) path+="/";
		return repositoy.findByPathStartsWithAndOwner(path, UserAccountService.getPrincipal().getId());
	}

	@Override
	@PreAuthorize("hasPermission(#id, 'Document', 'DOCUMENT_WRITE')")
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
	@PreAuthorize("hasPermission(#id, 'Document', 'DOCUMENT_WRITE')")
	public boolean remove(String id) {
		try {
			MongoDocument doc = repositoy.findOne(id);
			return this.remove(doc);
		} catch(Exception e) {
			return false;
		}
	}
	
	@PreAuthorize("hasPermission(#id, 'Document', 'DOCUMENT_WRITE')")
	public boolean remove(MongoDocument doc) {
		try {
			if(doc.isFolder()) {
				List<MongoDocument> toDelete = this.listAllDocuments(doc.getPath() + doc.getName());
				if(toDelete.isEmpty()) {
					repositoy.delete(doc.getId());
				} else {
					for(MongoDocument d : toDelete) {
						remove(d);
					}
				}
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
	@PreAuthorize("hasPermission(#id, 'Document', 'DOCUMENT_READ')")
	public MongoDocument get(String id) {
		return repositoy.findOne(id);
	}
	

	@Override
	@PreAuthorize("hasPermission(#id, 'Document', 'DOCUMENT_READ')")
	public InputStream getDocumentPayload(String id) throws NoSuchRequestHandlingMethodException {
		MongoDocument doc = repositoy.findOne(id);
		
		if(doc == null || StringUtils.isEmpty(doc.getPayload()))
			throw new NoSuchRequestHandlingMethodException(id + " Documento no encontrado", this.getClass());

		GridFSDBFile fsdbFile = gridOperations.findOne(findFsFileById(doc.getPayload()));
		
		return fsdbFile.getInputStream();
	}

	@Override
	@PreAuthorize("hasPermission(#id, 'Document', 'DOCUMENT_WRITE')")
	public MongoDocument saveDocumentPayload(String id, InputStream file) throws NoSuchRequestHandlingMethodException {
		MongoDocument doc = repositoy.findOne(id);
		
		if(doc == null)
			throw new NoSuchRequestHandlingMethodException(id + " Documento no encontrado", this.getClass());

		GridFSFile oldFsFile = gridOperations.findOne(findFsFileById(doc.getPayload()));
	 
		GridFSFile fsFile = gridOperations.store(file, id);   
		
		if(oldFsFile != null) {
			gridOperations.delete(findFsFileById(doc.getPayload()));
		}
		
        doc.setPayload(fsFile.getId().toString());
        doc.setLength(fsFile.getLength());
        this.save(doc);
                
		return doc;
	}
	
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
	
	private Query findFsFileById(String fsid) {
		return Query.query(Criteria.where("_id").is(fsid));
	}



	

}
