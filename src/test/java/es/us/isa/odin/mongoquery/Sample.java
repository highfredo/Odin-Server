package es.us.isa.odin.mongoquery;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sample")
public class Sample {

	@Id
	private String id;

	private String path;
	private URI uri;
	
	public Sample(String path) {
		this.path = path;
		try {
			uri = new URI(path);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "Sample [id=" + id + ", path=" + path + "]";
	}
	
	

}