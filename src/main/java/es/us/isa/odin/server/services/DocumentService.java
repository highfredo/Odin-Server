package es.us.isa.odin.server.services;

import java.io.InputStream;
import java.util.List;

import es.us.isa.odin.server.domain.Document;


@SuppressWarnings("rawtypes")
public interface DocumentService<T extends Document> {

    public List<T> listDocuments(String path);
    public List<T> listDocumentsRecursive(String path);
    public T save(T doc);
    public boolean remove(String id);
    
    public T get(String id);
    public void move(String id, String to);
    public void copy(String id, String to);

    public InputStream getDocumentPayload(String id);	
    public T saveDocumentPayload(String id, InputStream file);
}
