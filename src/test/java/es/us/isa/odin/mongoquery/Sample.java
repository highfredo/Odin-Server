package es.us.isa.odin.mongoquery;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import es.us.isa.odin.server.domain.documenttype.DocumentType;

@Document(collection = "sample")
public class Sample {

	@Id
	private String id;

	private DocumentType type;

	public Sample(DocumentType type) {
		super();
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}

}