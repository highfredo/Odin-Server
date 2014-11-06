package es.us.isa.odin.server.domain.documenttype;

public class TextDocumentType implements DocumentType {

	@Override
	public String getType() {
		return DocumentTypes.TEXT;
	}

}
