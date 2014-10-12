package es.us.isa.odin.server.security;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class SocialSignInAdapter implements SignInAdapter {

    //@Autowired
    //private RequestCache requestCache;
 
	@Autowired
	UserAccountService userAccountService;

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        signin(localUserId, connection.getKey().getProviderId());
        return extractOriginalUrl(request);
    }
    
	public void signin(String userId, String provider) {
		UsernamePasswordAuthenticationToken token = null;
		UserAccount userAccount = (UserAccount) userAccountService.loadUserByUserId(userId); 
		if (userAccount != null) {
			token = new UsernamePasswordAuthenticationToken(userAccount, null, userAccount.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(token);
		}
	}

    private String extractOriginalUrl(NativeWebRequest request) {
    	/*
        HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
        SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
        if (saved == null) {
            return null;
        }
        requestCache.removeRequest(nativeReq, nativeRes);
        removeAutheticationAttributes(nativeReq.getSession(false));
        return saved.getRedirectUrl();
        */
        return "/";
    }

    private void removeAutheticationAttributes(HttpSession session) {
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}