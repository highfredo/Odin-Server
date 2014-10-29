package es.us.isa.odin.server.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@org.springframework.data.mongodb.core.mapping.Document(collection="Document")
@JsonInclude(Include.NON_NULL)
public class MongoDocument extends Document<String> implements HaveId {

	@Id	
	private String id; 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Version
	@Override
    public Long getRevision() {
        return super.getRevision();
    }
	
	@CreatedDate
    public Date getCreation() {
        return super.getCreation();
    }
	
	@LastModifiedDate
    public Date getLastModification() {
        return super.getLastModification();
    }
	
	public String getPath() {
		return getUri().getSchemeSpecificPart();
	}
	
	public void setPath(String path) {
		if(!path.endsWith("/")) path+="/";
		if(!path.startsWith("/")) path = "/" + path;
		
		String suri = "//" + getOwner() + path;
		
		Pattern pattern = Pattern.compile("[^/]+/$");
		Matcher matcher = pattern.matcher(path);
		if (matcher.find()) {
		    setName(matcher.group(0).replace("/", ""));
		}
		
		suri = suri.replaceAll(" ", "%20");
		try {
			setUri(new URI(suri));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
    
}
