/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.us.isa.odin.server.domain;

import java.net.URI;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author japarejo
 */
public class Document<T> {

    private URI uri; // //{userId}/path/to/doc/{name}/#id
    private String name;
    private Long revision;
    private Date creation;
    private Date lastModification;   
    //private String path;
	private String owner;
	private Map<String, String> permissions; // {id_user: "r", id_user: "rw", public: "rw"}   
    private T payload;
    private String description;  
    private Boolean isFolder;
    private Long length;
    private String type; // DocumentType
    private Map<String, Object> metadata;   
    

    public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	/**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * @return the creation
     */
    public Date getCreation() {
        return creation;
    }

    /**
     * @param creation the creation to set
     */
    public void setCreation(Date creation) {
        this.creation = creation;
    }

    /**
     * @return the lastModification
     */
    public Date getLastModification() {
        return lastModification;
    }

    /**
     * @param lastModification the lastModification to set
     */
    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }
    	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Map<String, String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Map<String, String> permissions) {
		this.permissions = permissions;
	}

	/**
     * @return the payload
     */
    public T getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(T payload) {
        this.payload = payload;
    }

    /**
     * @return the metadata
     */
    public Map<String,Object> getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(Map<String,Object> metadata) {
        this.metadata = metadata;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

	public Boolean isFolder() {
		return isFolder;
	}
	
	public void setFolder(Boolean isFolder) {
		this.isFolder = isFolder;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	    
}
