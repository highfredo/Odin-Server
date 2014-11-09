package es.us.isa.odin.server.domain.documenttype;


public class FileDocumentType extends DocumentType {

	private String subType;

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

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj) == true) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			FileDocumentType other = (FileDocumentType) obj;
			if (subType == null) {
				if (other.subType != null)
					return false;
			} else if (!subType.equals(other.subType))
				return false;
			return true;
		} else {
			return false;
		}
	}

}