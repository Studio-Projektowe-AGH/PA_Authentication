package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.LoginCredentials;
import org.bson.types.ObjectId;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.Logger;
import services.BasicDataService;

import java.io.IOException;

/**
 * Created by Wojtek on 20/04/15.
 */
public class SigninController extends Controller {

    @Inject
    static BasicDataService<LoginCredentials, ObjectId> dataService;

    @BodyParser.Of(BodyParser.Json.class)
    public static Result handleCredentialSignin() {

        try {
            JsonNode jsonBody = request().body().asJson();
            ObjectMapper mapper = new ObjectMapper();
            LoginCredentials receivedCredentials = mapper.readValue(jsonBody.toString(), LoginCredentials.class);
            LoginCredentials storedCredentials = dataService.findOneByEmail(receivedCredentials.getEmail());

            if (storedCredentials != null) {
                if (storedCredentials.getHashedPassword().compare(receivedCredentials.getPassword())) {
                    Logger.debug("Signin Successful");
                    return ok("Signin Successful");
                } else {
                    Logger.debug("Signin Failed - Password mismatch.");
                    return unauthorized();
                }
            } else {
                Logger.debug("Signin Failed - User not found.");
                return unauthorized();
            }
        } catch (IOException ioe) {
           ioe.printStackTrace();
           return badRequest("Invalid JSON format.");
        }
    }

    public static Result handleProviderSignin(String provider) {
        return ok("OK");
    }
}
