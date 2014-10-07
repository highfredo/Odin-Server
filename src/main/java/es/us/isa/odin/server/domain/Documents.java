package es.us.isa.odin.server.domain;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="documentos")
public class Documents<T> {
	
	@Id
	private String id;
	@Version
	private Long version;
	
	private String owner;
	private List<String> canRead;
	private List<String> canWrite;
	
	@CreatedDate
	private DateTime creationDate;
	private T entity;
	private Map<String, Object> metadata;
	
	public Documents(T entity) {
		metadata = new HashMap<>();
		this.entity = entity;
	}
	
	public Documents() {
		metadata = new HashMap<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long revision) {
		this.version = revision;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> extraData) {
		this.metadata = extraData;
	}
	
	public Object addMetadata(String key, Object value) {
		return this.metadata.put(key, value);
	}
	
	public Object removeMetadata(String key) {
		return this.metadata.remove(key);
	}
		
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<String> getCanRead() {
		return canRead;
	}

	public void setCanRead(List<String> canRead) {
		this.canRead = canRead;
	}
	
	public List<String> getCanWrite() {
		return canWrite;
	}

	public void setCanWrite(List<String> canWrite) {
		this.canWrite = canWrite;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
			return false;
		}

		Documents<?> that = (Documents<?>) obj;

		return this.id.equals(that.getId());
	}

}
