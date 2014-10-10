package es.us.isa.odin.server.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class UserAccountService implements SocialUserDetailsService {
	
	@Autowired
	private UserAccountRepository repository;
	
	public UserAccount create(Connection<?> connection) {
		
		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(new Authority("ROLE_USER"));
			
		UserAccount user = new UserAccount(connection.getDisplayName(), "socialUser", true,
				true, true,	true, authorities);
		
		repository.save(user);
		
		return user;
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
		return repository.findByUsername(userId);
	}

}