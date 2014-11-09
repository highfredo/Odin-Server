package es.us.isa.odin.server.domain.documenttype;


public class FileDocumentType implements DocumentType {

	private String subType;
	// private String type = DocumentTypes.FILE_VALUE;

	public FileDocumentType(String subType) {
		this.subType = subType;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	@Override
	public String getType() {
		return DocumentTypes.FILE_VALUE;
	}

	@Override
	public String toString() {
		return getType() + "::" + subType;
	}

}