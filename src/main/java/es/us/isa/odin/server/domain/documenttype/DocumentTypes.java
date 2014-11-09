package es.us.isa.odin.server.domain.documenttype;

import java.util.HashMap;
import java.util.Map;


public class DocumentTypes {

	public static DocumentType FILE;
	public static DocumentType TEXT;
	public static DocumentType LINK;
	public static DocumentType FOLDER;
	
	
	public static String FILE_VALUE = "file";
	public static String TEXT_VALUE = "text";
	public static String LINK_VALUE = "link";
	public static String FOLDER_VALUE = "folder";
	
	static {
		FILE = new FileDocumentType(null);
		TEXT = null;
		LINK = null;
		FOLDER = new FolderDocumentType();
	}
	
	public Map<String, DocumentType> repository;
	
	public DocumentTypes() {
		this.repository = new HashMap<String, DocumentType>();
		this.repository.put(FILE_VALUE, FILE);
		this.repository.put(TEXT_VALUE, TEXT);
		this.repository.put(LINK_VALUE, LINK);
		this.repository.put(FOLDER_VALUE, FOLDER);
	}
	
	public DocumentType get(String name) {
		return repository.get(name);
	}
	
	public boolean has(String name) {
		return repository.containsKey(name);
	}
	
	public DocumentType getFromMimeType(String mime) {
		String name = "file::" + mime;
		DocumentType type = get(name);
		
		if(type == null) {
			return FILE;
		}
		
		return type;
	}
	
	public void register(String name, DocumentType type) {
		if(name == null || type == null)
			throw new IllegalArgumentException("ni 'name' ni 'type' pueden ser nulos");
		
		repository.put(name, type);
	}
	
	
			
}