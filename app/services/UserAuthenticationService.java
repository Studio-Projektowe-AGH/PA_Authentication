package services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import models.LoginCredentials;
import org.bson.types.ObjectId;
import play.Play;
import play.libs.Json;

import java.text.ParseException;

/**
 * Created by Wojtek on 22/04/15.
 */
public class UserAuthenticationService implements BasicAuthenticationService<LoginCredentials> {

    final static private String secret;

    @Inject
    static BasicDataService<LoginCredentials, ObjectId> dataService;

    static {
        secret = Play.application().configuration().getString("jwt.secret");
    }

    @Override
    public Boolean verifyCredentials(LoginCredentials receivedCredentials) {
        LoginCredentials storedCredentials = dataService.findOneByCredentials(receivedCredentials);
        if (storedCredentials != null && storedCredentials.getHashedPassword().compare(receivedCredentials.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean verifyToken(String jwtToken) throws ParseException, JOSEException {
        JWSObject jwsObject = JWSObject.parse(jwtToken);
        JWSVerifier verifier = new MACVerifier(secret.getBytes());
        return jwsObject.verify(verifier);
    }

    @Override
    public String generateToken(LoginCredentials loginCredentials) throws JOSEException {
        LoginCredentials storedCredentials = dataService.findOneByCredentials(loginCredentials);
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
