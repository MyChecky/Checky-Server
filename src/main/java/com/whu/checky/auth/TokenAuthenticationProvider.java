package com.whu.checky.auth;


import com.whu.checky.auth.filter.TokenAuthenticationFilter;
import com.whu.checky.service.TokenService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final TokenService tokenService;

    @Autowired
    public TokenAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        if (authentication.isAuthenticated()) {
            return authentication;
        }

        Pair<String,String> pair = (Pair)(authentication.getCredentials());
        String token = pair.getKey();
        String id = pair.getValue();

        if (token==null||token.equals("")) {
            return authentication;
        }

        UserDetails user = tokenService.authenticateToken(token,id);

        if(user == null) return authentication;
        Authentication auth = new PreAuthenticatedAuthenticationToken(
                user, token, user.getAuthorities());
        auth.setAuthenticated(true);
        return auth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (TokenAuthentication.class.isAssignableFrom(aClass));
    }
}
