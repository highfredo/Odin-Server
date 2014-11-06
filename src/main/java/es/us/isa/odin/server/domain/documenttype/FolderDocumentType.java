package es.us.isa.odin.server.domain.documenttype;


public class FolderDocumentType implements DocumentType {

	@Override
	public String getType() {
		return DocumentTypes.FOLDER;
	}

}
