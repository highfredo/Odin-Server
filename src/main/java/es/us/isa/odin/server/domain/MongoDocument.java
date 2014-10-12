package es.us.isa.odin.server.domain;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@org.springframework.data.mongodb.core.mapping.Document(collection="Document")
@JsonInclude(Include.NON_NULL)
public class MongoDocument extends Document<String> {

	@Id
	@Override
    public String getId() {
        return super.getId();
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
    
}
