package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.LoginCredentials;
import org.bson.types.ObjectId;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.BasicDataService;

import java.io.IOException;

/**
 * Created by Wojtek on 20/04/15.
 */
public class SignupController extends Controller {

    @Inject
    static BasicDataService<LoginCredentials, ObjectId> dataService;

    @BodyParser.Of(BodyParser.Json.class)
    public static Result handleSignup() {

        try {
            JsonNode jsonBody = request().body().asJson();
            ObjectMapper mapper = new ObjectMapper();
            LoginCredentials receivedCredentials = mapper.readValue(jsonBody.toString(), LoginCredentials.class);

            if (!dataService.exists(receivedCredentials)) {
                dataService.save(receivedCredentials);
                Logger.info("Signup Successful");
                return ok("Signup Successful");
            } else {
                Logger.info("Signup Failed");
                return badRequest("Incorrect credentials");
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return badRequest("Invalid JSON format.");
        }
    }
}
