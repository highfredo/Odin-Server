package es.us.isa.odin.server.domain.documenttype;

public class DocumentTypes {

	public static String FILE = "file";
	public static String TEXT = "text";
	public static String LINK = "link";
	public static String FOLDER = "folder";
	
	
	public static DocumentType getInstance(String type) {
		if(type.equals(FILE))
			return new FileDocumentType("");
		else if(type.equals(TEXT))
			return new TextDocumentType();
		else if(type.equals(LINK))
			return new LinkDocumentType();
		else if(type.equals(FOLDER))
			return new FolderDocumentType();
		
		else 
			return null;
	}
	
}
