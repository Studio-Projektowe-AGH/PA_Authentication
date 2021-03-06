package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.nimbusds.jose.JOSEException;
import models.LoginCredentials;
import org.bson.types.ObjectId;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.authentication.BasicAuthenticationService;
import services.data.BasicDataService;

import java.io.IOException;

/**
 * Created by Wojtek on 20/04/15.
 */
public class SigninController extends Controller {

    @Inject
    static BasicDataService<LoginCredentials, ObjectId> dataService;

    @Inject
    static BasicAuthenticationService<LoginCredentials> authenticationService;

    @BodyParser.Of(BodyParser.Json.class)
    public static Result handleCredentialSignin() {
        try {
            JsonNode jsonBody = request().body().asJson();
            ObjectMapper mapper = new ObjectMapper();
            LoginCredentials receivedCredentials = mapper.readValue(jsonBody.toString(), LoginCredentials.class);
            if (authenticationService.verifyCredentials(receivedCredentials)) {
                ObjectNode response = Json.newObject();
                String jwtToken = authenticationService.generateToken(receivedCredentials);
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
}
