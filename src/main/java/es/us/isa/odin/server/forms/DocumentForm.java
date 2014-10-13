package es.us.isa.odin.server.forms;

import java.util.Date;
import java.util.Map;

import es.us.isa.odin.server.domain.Document;
import es.us.isa.odin.server.security.UserAccountService;

public class DocumentForm {

	private String id;
	private Long revision;
	private String name;
	private String path;
	private String description;
	private Boolean isFolder;
	private Map<String, Object> metadata;
	private Date lastModification;
	private Boolean hasFile;
	private String yourPermissions;
	private String owner;
	
	public DocumentForm() {
		super();
	}
	
	public DocumentForm(Document<?> doc) {
		super();
		this.setId(doc.getId());
		this.setRevision(doc.getRevision());
		this.setName(doc.getName());
		this.setPath(doc.getPath());
		this.setDescription(doc.getDescription());
		this.setIsFolder(doc.isFolder());
		this.setMetadata(doc.getMetadata());
		this.setLastModification(doc.getLastModification());
		this.setHasFile(doc.getPayload() != null);
		this.setOwner(doc.getOwner());
				
		if(doc.getPermissions() != null) {
			String yourId = UserAccountService.getPrincipal().getId();
			if(doc.getPermissions().get(yourId) != null) {
				this.setYourPermissions(doc.getPermissions().get(yourId));
			} else if(doc.getPermissions().get("public") != null) {
				this.setYourPermissions(doc.getPermissions().get("public"));
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getRevision() {
		return revision;
	}

	public void setRevision(Long revision) {
		this.revision = revision;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsFolder() {
		return isFolder;
	}

	public void setIsFolder(Boolean isFolder) {
		this.isFolder = isFolder;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

	public Boolean getHasFile() {
		return hasFile;
	}

	public void setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
	}

	public String getYourPermissions() {
		return yourPermissions;
	}

	public void setYourPermissions(String yourPermissions) {
		this.yourPermissions = yourPermissions;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
