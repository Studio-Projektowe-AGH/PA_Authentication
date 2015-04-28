package services.authentication;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.restfb.FacebookClient;
import models.LoginCredentials;
import models.SocialCredentials;
import org.bson.types.ObjectId;
import play.Play;
import play.libs.Json;
import services.FacebookConnector;
import services.data.BasicDataService;

import javax.inject.Named;
import java.text.ParseException;

/**
 * Created by Wojtek on 28/04/15.
 */
public class FacebookAuthenticationService implements BasicAuthenticationService<SocialCredentials> {

    final static private String secret;
    static FacebookConnector facebookConnector = new FacebookConnector();

    @Inject
    @Named("SocialDataService")
    static BasicDataService<LoginCredentials, ObjectId> dataService;

    static {
        secret = Play.application().configuration().getString("jwt.secret");
    }

    @Override
    public Boolean verifyCredentials(SocialCredentials socialCredentials) {
        FacebookClient.DebugTokenInfo dti = facebookConnector.facebookClient.debugToken(socialCredentials.getAccessToken());
        socialCredentials.setAccountId(dti.getUserId());
        socialCredentials.setExpiresOn(dti.getExpiresAt());
        return dti.isValid();
    }

    @Override
    public Boolean verifyToken(String jwtToken) throws ParseException, JOSEException {
        JWSObject jwsObject = JWSObject.parse(jwtToken);
        JWSVerifier verifier = new MACVerifier(secret.getBytes());
        return jwsObject.verify(verifier);
    }

    @Override
    public String generateToken(SocialCredentials loginCredentials) throws JOSEException {
        LoginCredentials storedCredentials = dataService.findOneByCredentials(new LoginCredentials(loginCredentials));
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        header.setContentType("text/plain");

        ObjectNode payloadObject = Json.newObject();
        payloadObject.put("userId", storedCredentials.getUserId());
        payloadObject.put("userRole", storedCredentials.getRole().toString());

        Payload payload = new Payload(payloadObject.toString());
        JWSObject jwsObject = new JWSObject(header, payload);
        JWSSigner signer = new MACSigner(secret.getBytes());
        jwsObject.sign(signer);

        return jwsObject.serialize();
    }
}
