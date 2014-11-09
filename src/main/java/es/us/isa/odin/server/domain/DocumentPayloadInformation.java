package es.us.isa.odin.server.domain;

import java.io.InputStream;

public interface DocumentPayloadInformation {

	public String name();
	public boolean forceDownload();
	public InputStream inputStream();
	public long length();
	public String mimeType();
	public String redirect();
		
}
