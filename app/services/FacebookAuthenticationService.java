package services;

import com.nimbusds.jose.JOSEException;
import com.restfb.FacebookClient;
import models.SocialCredentials;

import java.text.ParseException;

/**
 * Created by Wojtek on 28/04/15.
 */
public class FacebookAuthenticationService implements BasicAuthenticationService<SocialCredentials> {

    static FacebookConnector facebookConnector = new FacebookConnector();

    @Override
    public Boolean verifyCredentials(SocialCredentials socialCredentials) {
        FacebookClient.DebugTokenInfo dti = facebookConnector.facebookClient.debugToken(socialCredentials.getAccessToken());
        socialCredentials.setAccountId(dti.getUserId());
        socialCredentials.setExpiresOn(dti.getExpiresAt());
        return facebookConnector.facebookClient.debugToken(socialCredentials.getAccessToken()).isValid();
    }

    @Override
    public Boolean verifyToken(String jwtToken) throws ParseException, JOSEException {
        return true;
    }

    @Override
    public String generateToken(SocialCredentials loginCredentials) throws JOSEException {
        return "";
    }
}
