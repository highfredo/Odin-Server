package es.us.isa.odin.server.security.connection;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialUserConnectionRepository extends MongoRepository<SocialUserConnection, String> {

	List<SocialUserConnection> findByProviderIdAndProviderUserId(String providerId, String providerUserId);

	List<SocialUserConnection> findByProviderIdAndProviderUserIdIn(String providerId, Set<String> providerUserIds);

	List<SocialUserConnection> findByUserIdAndProviderId(String userId,	String providerId);

	List<SocialUserConnection> findByUserId(String userId);

	List<SocialUserConnection> findByProviderIdAndProviderUserIdIn(String providerId, List<String> providerUserIds);

	SocialUserConnection findByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);
	
}
