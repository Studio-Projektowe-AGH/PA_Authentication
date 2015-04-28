package services;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import models.LoginCredentials;
import models.SocialCredentials;
import play.Play;

import java.util.Arrays;

/**
 * Created by Wojtek on 28/04/15.
 */
public class FacebookConnector {
    public FacebookClient facebookClient;

    public FacebookConnector() {
        String clientToken = Play.application().configuration().getString("fb.client_token");
        String secret = Play.application().configuration().getString("fb.secret");
        this.facebookClient = new DefaultFacebookClient(clientToken, secret, Version.VERSION_2_3);
    }

    public LoginCredentials generateLoginCredentials(SocialCredentials socialCredentials) {
        FacebookClient userConnector = new DefaultFacebookClient(socialCredentials.getAccessToken(), Version.VERSION_2_3);
        JsonObject me = userConnector.fetchObject("me", JsonObject.class);
        LoginCredentials loginCredentials = new LoginCredentials("", "", LoginCredentials.UserRole.individual, Arrays.asList(socialCredentials));
        return loginCredentials;
    }
}
