package es.us.isa.odin.server.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.http.MediaType;

import es.us.isa.odin.server.domain.documenttype.FileDocumentType;
import es.us.isa.odin.server.domain.documenttype.LinkDocumentType;

public class MongoDocumentPayloadInformation implements DocumentPayloadInformation {

	private MongoDocument document;
	private InputStream inputStream;

	public MongoDocumentPayloadInformation(MongoDocument document, InputStream inputStream) {
		super();
		this.document = document;
		this.inputStream = inputStream;
	}
	
	public MongoDocumentPayloadInformation(MongoDocument document) {
		this(document, null);
	}

	@Override
	public String name() {
		return document.getName();
	}

	@Override
	public boolean forceDownload() {
		return false;
	}

	@Override
	public InputStream inputStream() {
		if(inputStream != null) {
			return inputStream;
		} else {
			return new ByteArrayInputStream(document.getPayload().getBytes());
		}
	}

	@Override
	public long length() {
		if(inputStream != null) {
			return document.getLength();
		} else {
			return document.getPayload().length();
		}
	}

	@Override
	public String mimeType() {
		if(document.getType() instanceof FileDocumentType) {
			return ((FileDocumentType) document.getType() ).getSubType(); 
		}
		
		return MediaType.APPLICATION_OCTET_STREAM_VALUE;
	}

	@Override
	public String redirect() {
		if(document.getType() instanceof LinkDocumentType) {
			return document.getPayload(); 
		}
		
		return null;
	}

}
