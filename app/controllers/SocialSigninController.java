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
import play.mvc.Controller;
import play.mvc.Result;
import services.FacebookConnector;
import services.authentication.BasicAuthenticationService;
import services.data.BasicDataService;

import javax.inject.Named;
import java.io.IOException;

/**
 * Created by Wojtek on 20/04/15.
 */
public class SocialSigninController extends Controller {

    @Inject
    @Named("SocialDataService")
    static BasicDataService<LoginCredentials, ObjectId> dataService;

    @Inject
    static BasicAuthenticationService<SocialCredentials> authenticationService;

    static FacebookConnector facebookConnector = new FacebookConnector();

    public static Result handleProviderSignin(String provider) {
        try {
            JsonNode jsonBody = request().body().asJson();
            ObjectMapper mapper = new ObjectMapper();
            SocialCredentials receivedCredentials = mapper.readValue(jsonBody.toString(), SocialCredentials.class);

            if (authenticationService.verifyCredentials(receivedCredentials)) {
                if (!dataService.exists(new LoginCredentials(receivedCredentials))) {
                    LoginCredentials loginCredentials = facebookConnector.generateLoginCredentials(receivedCredentials);
                    dataService.save(loginCredentials);
                }

                ObjectNode response = Json.newObject();
                String jwtToken = authenticationService.generateToken(receivedCredentials);
                response.put("access_token", jwtToken);

                dataService.updateLastAccessTime();
                Logger.debug("Signin Successful. Token: " + jwtToken);
                return ok(response);
            } else {
                return badRequest("Bad request.");
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
}
