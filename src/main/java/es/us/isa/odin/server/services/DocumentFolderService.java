package es.us.isa.odin.server.services;

import java.net.URI;
import java.util.List;

import es.us.isa.odin.server.domain.Document;

@SuppressWarnings("rawtypes")
public interface DocumentFolderService<T extends Document> extends DocumentService<T> {
	
	public List<T> listDocuments(URI uri);  
    public void move(URI fromUri, URI toUri);
    public void copy(URI fromUri, URI toUri);
}