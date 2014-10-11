package es.us.isa.odin.server.security.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.security.provider.SocialAuthenticationService;
import org.springframework.social.security.provider.SocialAuthenticationService.ConnectionCardinality;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class MongoConnectionRepository implements ConnectionRepository {

    private final String userId;
    private final SocialUserConnectionRepository socialUserConnectionRepository;
    private final SocialAuthenticationServiceLocator socialAuthenticationServiceLocator;
    private final TextEncryptor textEncryptor;
    
	public MongoConnectionRepository(
			String userId,
			SocialUserConnectionRepository socialUserConnectionRepository,
			SocialAuthenticationServiceLocator socialAuthenticationServiceLocator,
			TextEncryptor textEncryptor) {
        this.userId = userId;
        this.socialUserConnectionRepository = socialUserConnectionRepository;
        this.socialAuthenticationServiceLocator = socialAuthenticationServiceLocator;
        this.textEncryptor = textEncryptor;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
        List<SocialUserConnection> userSocialConnectionList = socialUserConnectionRepository.findByUserId(userId);

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        Set<String> registeredProviderIds = socialAuthenticationServiceLocator.registeredProviderIds();
        for (String registeredProviderId : registeredProviderIds) {
            connections.put(registeredProviderId, Collections.<Connection<?>> emptyList());
        }
        
        for (SocialUserConnection userSocialConnection : userSocialConnectionList) {
            String providerId = userSocialConnection.getProviderId();
            if (connections.get(providerId).size() == 0) {
                connections.put(providerId, new LinkedList<Connection<?>>());
            }
            connections.add(providerId, buildConnection(userSocialConnection));
        }
        return connections;
	}

	@Override
	public List<Connection<?>> findConnections(String providerId) {
        List<Connection<?>> resultList = new LinkedList<Connection<?>>();
        List<SocialUserConnection> userSocialConnectionList = socialUserConnectionRepository.findByUserIdAndProviderId(userId, providerId);
        for (SocialUserConnection userSocialConnection : userSocialConnectionList) {
            resultList.add(buildConnection(userSocialConnection));
        }
        return resultList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
        if (providerUsers == null || providerUsers.isEmpty()) {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }
        MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
        for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext();) {
            Entry<String, List<String>> entry = it.next();
            String providerId = entry.getKey();
            List<String> providerUserIds = entry.getValue();
            List<SocialUserConnection> userSocialConnections = socialUserConnectionRepository.findByProviderIdAndProviderUserIdIn(providerId, providerUserIds);
            List<Connection<?>> connections = new ArrayList<Connection<?>>(providerUserIds.size());
            for (int i = 0; i < providerUserIds.size(); i++) {
                connections.add(null);
            }
            connectionsForUsers.put(providerId, connections);

            for (SocialUserConnection userSocialConnection : userSocialConnections) {
                String providerUserId = userSocialConnection.getProviderUserId();
                int connectionIndex = providerUserIds.indexOf(providerUserId);
                connections.set(connectionIndex, buildConnection(userSocialConnection));
            }

        }
        return connectionsForUsers;
	}

	@Override
	public Connection<?> getConnection(ConnectionKey connectionKey) {
		SocialUserConnection userSocialConnection = socialUserConnectionRepository.findByUserIdAndProviderIdAndProviderUserId(
				userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
		
        if (userSocialConnection != null) {
            return buildConnection(userSocialConnection);
        }
        throw new NoSuchConnectionException(connectionKey);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        }
        return connection;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) findPrimaryConnection(providerId);
	}

	@Override
	public void addConnection(Connection<?> connection) {
        //check cardinality
        SocialAuthenticationService<?> socialAuthenticationService = this.socialAuthenticationServiceLocator.getAuthenticationService(connection.getKey().getProviderId());
        if (socialAuthenticationService.getConnectionCardinality() == ConnectionCardinality.ONE_TO_ONE || socialAuthenticationService.getConnectionCardinality() == ConnectionCardinality.ONE_TO_MANY) {
            List<SocialUserConnection> storedConnections = socialUserConnectionRepository.findByProviderIdAndProviderUserId(connection.getKey().getProviderId(), connection.getKey().getProviderUserId());
            if (storedConnections.size() > 0){
                //not allow one providerId connect to multiple userId
                throw new DuplicateConnectionException(connection.getKey());
            }
        }
        
        SocialUserConnection userSocialConnection = socialUserConnectionRepository.findByUserIdAndProviderIdAndProviderUserId(
        		userId, connection.getKey().getProviderId(), connection.getKey().getProviderUserId());
        
        if (userSocialConnection == null) {
            ConnectionData data = connection.createData();
            userSocialConnection = new SocialUserConnection(userId, data.getProviderId(),
            		data.getProviderUserId(), encrypt(data.getAccessToken()), encrypt(data.getSecret()),
            		encrypt(data.getRefreshToken()), data.getExpireTime());
            
            socialUserConnectionRepository.save(userSocialConnection);
        } else {
            throw new DuplicateConnectionException(connection.getKey());
        }
	}

	@Override
	public void updateConnection(Connection<?> connection) {
        ConnectionData data = connection.createData();
        SocialUserConnection userSocialConnection = socialUserConnectionRepository
                .findByUserIdAndProviderIdAndProviderUserId(userId, connection.getKey().getProviderId(), connection
                        .getKey().getProviderUserId());
        if (userSocialConnection != null) {
            userSocialConnection.setAccessToken(encrypt(data.getAccessToken()));
            userSocialConnection.setSecret(encrypt(data.getSecret()));
            userSocialConnection.setRefreshToken(encrypt(data.getRefreshToken()));
            userSocialConnection.setExpireTime(data.getExpireTime());
            socialUserConnectionRepository.save(userSocialConnection);
        }
	}

	@Override
	public void removeConnections(String providerId) {
        List<SocialUserConnection> userSocialConnectionList = socialUserConnectionRepository.findByUserIdAndProviderId(userId, providerId);
        for (SocialUserConnection userSocialConnection : userSocialConnectionList) {
            socialUserConnectionRepository.delete(userSocialConnection);
        }
	}

	@Override
	public void removeConnection(ConnectionKey connectionKey) {
		SocialUserConnection userSocialConnection = socialUserConnectionRepository.findByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
		socialUserConnectionRepository.delete(userSocialConnection);
	}
	
	
    // internal helpers

    private Connection<?> buildConnection(SocialUserConnection userSocialConnection) {
        ConnectionData connectionData = new ConnectionData(userSocialConnection.getProviderId(),
                userSocialConnection.getProviderUserId(), null, null, null,
                decrypt(userSocialConnection.getAccessToken()), decrypt(userSocialConnection.getSecret()),
                decrypt(userSocialConnection.getRefreshToken()), userSocialConnection.getExpireTime());
        ConnectionFactory<?> connectionFactory = this.socialAuthenticationServiceLocator.getConnectionFactory(connectionData.getProviderId());
        return connectionFactory.createConnection(connectionData);
    }

    private Connection<?> findPrimaryConnection(String providerId) {
        List<SocialUserConnection> userSocialConnectionList = socialUserConnectionRepository.findByUserIdAndProviderId(userId, providerId);
        return buildConnection(userSocialConnectionList.get(0));
    }

    private <A> String getProviderId(Class<A> apiType) {
        return socialAuthenticationServiceLocator.getConnectionFactory(apiType).getProviderId();
    }

    private String encrypt(String text) {
        return text != null ? textEncryptor.encrypt(text) : text;
    }

    private String decrypt(String encryptedText) {
        return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
    }

}
