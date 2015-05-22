package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.nimbusds.jose.JOSEException;
import models.LoginCredentials;
import org.bson.types.ObjectId;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.authentication.BasicAuthenticationService;
import services.data.BasicDataService;
import views.html.index;

public class Application extends Controller {
    @Inject
    static BasicDataService<LoginCredentials, ObjectId> dataService;

    @Inject
    static BasicAuthenticationService<LoginCredentials> authenticationService;

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result handleRandomTokenTest() {
        try {
            ObjectNode response = Json.newObject();
            LoginCredentials randomCredentials = dataService.findOneRandom();
            String jwtToken = authenticationService.generateToken(randomCredentials);
            response.put("access_token", jwtToken);

            Logger.debug("Test Random Token Successful. Token: " + jwtToken);
            return ok(response);
        } catch (JOSEException e) {
            e.printStackTrace();
            Logger.error("Error while generating token.");
            return internalServerError("Error while generating token.");
        }
    }
}
