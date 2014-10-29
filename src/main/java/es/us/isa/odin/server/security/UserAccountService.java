package es.us.isa.odin.server.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import es.us.isa.odin.server.domain.Document;
import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.security.permission.Permissions;
import es.us.isa.odin.server.services.DocumentFolderService;


@Service
public class UserAccountService implements SocialUserDetailsService {
	
	@Autowired
	private UserAccountRepository repository;
	@Autowired
	private DocumentFolderService<MongoDocument> documentService;
	
	public UserAccount create(Connection<?> connection) {
		
		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(new Authority("ROLE_USER"));
		authorities.add(new Authority(Permissions.DOCUMENT.READ));
		authorities.add(new Authority(Permissions.DOCUMENT.WRITE));
			
		UserAccount user = new UserAccount(connection.getDisplayName(), "socialUser", true,
				true, true,	true, authorities);
		
		repository.save(user);
		
		MongoDocument rootDocument = new MongoDocument();
		rootDocument.setName("root");
		rootDocument.setOwner(user.getId());
		rootDocument.setPath("/");
		rootDocument.setFolder(true);
		documentService.save(rootDocument);
		
		return user;
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
		return repository.findOne(userId); 
	}

	public UserAccount findByUsername(String username) {
		return repository.findByUsername(username);
	}
	
	public static UserAccount getPrincipal() {
		return (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
