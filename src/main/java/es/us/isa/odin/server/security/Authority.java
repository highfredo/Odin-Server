package es.us.isa.odin.server.security;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;

/**
 * An authority (a security role) used by Spring Security.
 */

public class Authority implements GrantedAuthority {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5859839567188435380L;
	@NotNull
    @Size(min = 0, max = 50)
    @Id
    private String name;
	
	public Authority(){
		
	}
	
	public Authority(String name) {
		this.name = name;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
    	return name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if(o instanceof String) {
        	return this.name.equals(o);
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Authority authority = (Authority) o;

        if (name != null ? !name.equals(authority.name) : authority.name != null) {
            return false;
        }

        return true;
    }
    
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "name='" + name + '\'' +
                "}";
    }

}
