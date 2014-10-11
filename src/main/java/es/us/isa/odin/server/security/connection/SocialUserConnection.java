package es.us.isa.odin.server.security.connection;

import org.springframework.data.annotation.Id;

public class SocialUserConnection {

	@Id
	private String id;
	private String userId;
	private String providerId;
	private String providerUserId;
	private String accessToken;
	private String secret;
	private String refreshToken;
	private Long expireTime;
	

	public SocialUserConnection() {
		super();
	}

	public SocialUserConnection(String userId, String providerId,
			String providerUserId, String accessToken, String secret,
			String refreshToken, Long expireTime) {
		super();
		this.userId = userId;
		this.providerId = providerId;
		this.providerUserId = providerUserId;
		this.accessToken = accessToken;
		this.secret = secret;
		this.refreshToken = refreshToken;
		this.expireTime = expireTime;
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

}
