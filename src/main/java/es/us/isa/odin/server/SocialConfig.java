package es.us.isa.odin.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import es.us.isa.odin.server.security.SocialConnectionSignUp;
import es.us.isa.odin.server.security.SocialSignInAdapter;
import es.us.isa.odin.server.security.UserAccount;
import es.us.isa.odin.server.security.UserAccountService;
import es.us.isa.odin.server.security.connection.MongoUsersConnectionRepository;
import es.us.isa.odin.server.security.connection.SocialUserConnectionRepository;

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {
	
	@Autowired
	private SocialUserConnectionRepository socialUserConnectionRepository;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private SignInAdapter signInAdapter;

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
		
		GoogleConnectionFactory gcf = new GoogleConnectionFactory(
                env.getProperty("google.consumerKey"),
                env.getProperty("google.consumerSecret")
        );
		gcf.setScope("profile");
		
		cfConfig.addConnectionFactory(gcf);		
		cfConfig.addConnectionFactory(new TwitterConnectionFactory(
                env.getProperty("twitter.consumerKey"),
                env.getProperty("twitter.consumerSecret")
        ));	
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new UserIdSource() {	
			@Override
			public String getUserId() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null) {
					throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
				}
				return ((UserAccount) authentication.getPrincipal()).getId();
			}
		};
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		MongoUsersConnectionRepository conf = new MongoUsersConnectionRepository(socialUserConnectionRepository, (SocialAuthenticationServiceLocator) connectionFactoryLocator, Encryptors.noOpText());
		conf.setConnectionSignUp(new SocialConnectionSignUp(userAccountService, (SocialSignInAdapter) signInAdapter));
		return conf;
	}
    
	@Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }


	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public Twitter twitter(ConnectionRepository repository) {
		Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
		return connection != null ? connection.getApi() : null;
	}
}