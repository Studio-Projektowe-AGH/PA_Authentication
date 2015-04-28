package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.nimbusds.jose.JOSEException;
import models.LoginCredentials;
import models.SocialCredentials;
import org.bson.types.ObjectId;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.BasicAuthenticationService;
import services.BasicDataService;
import services.FacebookConnector;

import java.io.IOException;

/**
 * Created by Wojtek on 20/04/15.
 */
public class SigninController extends Controller {

    @Inject
    static BasicDataService<LoginCredentials, ObjectId> dataService;

    @Inject
    static BasicAuthenticationService<LoginCredentials> loginAuthenticationService;

    @Inject
    static BasicAuthenticationService<SocialCredentials> socialAuthenticationService;

    static FacebookConnector facebookConnector = new FacebookConnector();

    @BodyParser.Of(BodyParser.Json.class)
    public static Result handleCredentialSignin() {

        try {
            JsonNode jsonBody = request().body().asJson();
            ObjectMapper mapper = new ObjectMapper();
            LoginCredentials receivedCredentials = mapper.readValue(jsonBody.toString(), LoginCredentials.class);

            if (loginAuthenticationService.verifyCredentials(receivedCredentials)) {
                ObjectNode response = Json.newObject();
                String jwtToken = loginAuthenticationService.generateToken(receivedCredentials);
                response.put("access_token", jwtToken);

                dataService.updateLastAccessTime();
                Logger.debug("Signin Successful. Token: " + jwtToken);
                return ok(response);
            } else {
                Logger.debug("Signin Failed. Unauthorized.");
                return unauthorized();
            }
        } catch (IOException ioe) {
           ioe.printStackTrace();
            Logger.error("Signin Failed. Invalid JSON format.");
           return badRequest("Invalid JSON format.");
        } catch (JOSEException e) {
            e.printStackTrace();
            Logger.error("Error while generating token.");
            return internalServerError("Error while generating token.");
        }
    }

    public static Result handleProviderSignin(String provider) {
        try {
            JsonNode jsonBody = request().body().asJson();
            ObjectMapper mapper = new ObjectMapper();
            SocialCredentials receivedCredentials = mapper.readValue(jsonBody.toString(), SocialCredentials.class);

            if (socialAuthenticationService.verifyCredentials(receivedCredentials)) {
                ObjectNode response = Json.newObject();

                LoginCredentials loginCredentials = facebookConnector.generateLoginCredentials(receivedCredentials);
                dataService.save(loginCredentials);

                String jwtToken = loginAuthenticationService.generateToken(loginCredentials);
                response.put("access_token", jwtToken);

                dataService.updateLastAccessTime();
                Logger.debug("Signin Successful. Token: " + jwtToken);
                return ok(response);
            }
            return ok("OK");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Logger.error("Signin Failed. Invalid JSON format.");
            return badRequest("Invalid JSON format.");
        } catch (JOSEException e) {
            e.printStackTrace();
            Logger.error("Error while generating token.");
            return internalServerError("Error while generating token.");
        }
    }
}
