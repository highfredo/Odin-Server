package es.us.isa.odin.server.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

public class SocialConnectionSignUp implements ConnectionSignUp {

	private UserAccountService userAccountService;
	
	public SocialConnectionSignUp(UserAccountService userAccountService) {
		this.userAccountService = userAccountService;
	}
	
	@Override
	public String execute(Connection<?> connection) {
		UserAccount userAccount = userAccountService.findByUsername(connection.getDisplayName());
		
		if(userAccount == null)
			userAccount = userAccountService.create(connection);
		
		signin(userAccount.getUsername(), connection.getKey().getProviderId());
		
		return userAccount.getUserId();
	}
	
	private void signin(String userId, String provider) {
		UsernamePasswordAuthenticationToken token = null;
		UserAccount userAccount = userAccountService.findByUsername(userId);
		if (userAccount != null) {
			token = new UsernamePasswordAuthenticationToken(userAccount, null, userAccount.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(token);
		}
	}

}
