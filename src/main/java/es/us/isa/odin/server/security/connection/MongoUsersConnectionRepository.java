package es.us.isa.odin.server.security.connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationServiceLocator;


// Referencia: https://www.jiwhiz.com/#/blogs/50f4f033e4b04d4d302ba03a
public class MongoUsersConnectionRepository implements UsersConnectionRepository {
	
    private final SocialUserConnectionRepository socialUserConnectionRepository;
    private final SocialAuthenticationServiceLocator socialAuthenticationServiceLocator;
    private final TextEncryptor textEncryptor;
    private ConnectionSignUp connectionSignUp; // Clase que creará una nueva cuenta si esta no existe
    
    
    public MongoUsersConnectionRepository(SocialUserConnectionRepository socialUserConnectionRepository, 
    			SocialAuthenticationServiceLocator socialAuthenticationServiceLocator, TextEncryptor textEncryptor){
    	
        this.socialUserConnectionRepository = socialUserConnectionRepository;
        this.socialAuthenticationServiceLocator = socialAuthenticationServiceLocator;
        this.textEncryptor = textEncryptor;
        
    }
    

	public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }

	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        List<SocialUserConnection> userSocialConnectionList = socialUserConnectionRepository.findByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
        List<String> localUserIds = new ArrayList<String>();
        for (SocialUserConnection userSocialConnection : userSocialConnectionList){
            localUserIds.add(userSocialConnection.getUserId());
        }
        
        if (localUserIds.size() == 0 && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null) {
                createConnectionRepository(newUserId).addConnection(connection);
                return Arrays.asList(newUserId);
            }
        }
        return localUserIds;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        final Set<String> localUserIds = new HashSet<String>();
        List<SocialUserConnection> userSocialConnectionList = socialUserConnectionRepository.findByProviderIdAndProviderUserIdIn(providerId, providerUserIds);
        for (SocialUserConnection userSocialConnection : userSocialConnectionList){
            localUserIds.add(userSocialConnection.getUserId());
        }
        return localUserIds;
	}

	@Override
	public ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new MongoConnectionRepository(userId, socialUserConnectionRepository, socialAuthenticationServiceLocator, textEncryptor);
	}

}
