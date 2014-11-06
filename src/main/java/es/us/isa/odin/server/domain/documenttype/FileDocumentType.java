package es.us.isa.odin.server.domain.documenttype;

public class FileDocumentType implements DocumentType {

	private String mimeType;

	public FileDocumentType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public String getType() {
		return DocumentTypes.FILE;
	}

	@Override
	public String toString() {
		return getType() + "::" + mimeType;
	}
	
	

}
