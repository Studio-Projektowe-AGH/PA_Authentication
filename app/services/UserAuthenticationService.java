package services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import models.LoginCredentials;
import net.minidev.json.JSONObject;
import org.bson.types.ObjectId;
import play.Play;
import play.api.libs.json.JsPath;
import play.libs.Json;

import java.text.ParseException;

/**
 * Created by Wojtek on 22/04/15.
 */
public class UserAuthenticationService implements BasicAuthenticationService {

    final static private String secret;

    @Inject
    static BasicDataService<LoginCredentials, ObjectId> dataService;

    static {
        secret = Play.application().configuration().getString("jwt.secret");
    }

    public Boolean verifyCredentials(LoginCredentials receivedCredentials) {
        LoginCredentials storedCredentials = dataService.findOneByEmail(receivedCredentials.getEmail());
        if (storedCredentials != null && storedCredentials.getHashedPassword().compare(receivedCredentials.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean verifyToken(String jwtToken) throws ParseException, JOSEException {
        JWSObject jwsObject = JWSObject.parse(jwtToken);
        JWSVerifier verifier = new MACVerifier(secret.getBytes());
        return jwsObject.verify(verifier);
    }

    public String getTokenPayload(String jwtToken) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(jwtToken);
        return jwsObject.getPayload().toString();
    }

    public String generateToken(LoginCredentials loginCredentials) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        header.setContentType("text/plain");

        ObjectNode payloadObject = Json.newObject();
        payloadObject.put("userId", loginCredentials.getUserId());
        payloadObject.put("userRole", loginCredentials.getRole().toString());

        Payload payload = new Payload(payloadObject.toString());
        JWSObject jwsObject = new JWSObject(header, payload);
        JWSSigner signer = new MACSigner(secret.getBytes());
        jwsObject.sign(signer);

        return jwsObject.serialize();
    }
}
