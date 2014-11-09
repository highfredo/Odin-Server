package es.us.isa.odin.server.services;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import es.us.isa.odin.server.domain.Document;
import es.us.isa.odin.server.domain.DocumentPayloadInformation;


@SuppressWarnings("rawtypes")
public interface DocumentService<T extends Document> {

	public List<T> listDocuments();
	public T create();
	public T get(URI uri);
    public T save(T doc);
    public T save(T doc, InputStream file) throws NoSuchRequestHandlingMethodException;
    public boolean remove(URI uri);
    public DocumentPayloadInformation getDocumentPayload(URI uri) throws NoSuchRequestHandlingMethodException;	
    
}
