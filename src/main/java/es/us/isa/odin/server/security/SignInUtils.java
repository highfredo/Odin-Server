package es.us.isa.odin.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SignInUtils {

	@Autowired
	UserAccountRepository userAccountRepository;

	public void signin(String userId, String provider) {
		UsernamePasswordAuthenticationToken token = null;
		UserAccount userAccount = userAccountRepository.findByUsername(userId);
		if (userAccount != null) {
			token = new UsernamePasswordAuthenticationToken(userAccount, null, userAccount.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(token);
		}
	}
	
	public static UserAccount getPrincipal() {
		return (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
