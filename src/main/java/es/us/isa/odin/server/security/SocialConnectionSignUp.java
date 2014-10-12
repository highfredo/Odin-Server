package es.us.isa.odin.server.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

public class SocialConnectionSignUp implements ConnectionSignUp {

	private UserAccountService userAccountService;
	private SocialSignInAdapter signInAdapter;
	
	public SocialConnectionSignUp(UserAccountService userAccountService, SocialSignInAdapter signInAdapter) {
		this.userAccountService = userAccountService;
		this.signInAdapter = signInAdapter;
	}
	
	@Override
	public String execute(Connection<?> connection) {
		UserAccount	userAccount = userAccountService.create(connection);
		signInAdapter.signin(userAccount.getUsername(), connection.getKey().getProviderId());
		return userAccount.getUserId();
	}
	
}
