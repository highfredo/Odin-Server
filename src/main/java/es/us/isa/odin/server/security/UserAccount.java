package es.us.isa.odin.server.security;

import java.util.Collection;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.security.SocialUserDetails;

public class UserAccount extends User implements SocialUserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8754493499675361598L;

	public UserAccount(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
	}
	
	@Id
	private String id;
	@Email
	private String email;
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
	public boolean havePermission(String permission) {
		return this.getAuthorities().contains(permission);
	}
	
	public boolean havePermission(Authority permission) {
		return this.getAuthorities().contains(permission);
	}

	@Override
	public String getUserId() {
		return super.getUsername(); //this.id;
	}

}
