package controllers.businessSide;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.io.IOException;

/**
 * Created by Kris on 2015-04-19.
 */
public class BusinessSideHandler extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public static Result signUp(){
        JsonNode jsonInput = request().body().asJson();
        System.out.println(jsonInput);
        if(DBCommunication.existUser(jsonInput.findPath("email").textValue())){
            return Results.forbidden("User of this email already exists");
        }
        try {
            BusinessUser newUser = BusinessUser.createBUser(jsonInput,true);
            if(newUser == null){
                return badRequest("Wrong json format");
            }
            if(DBCommunication.addBUserToDB(newUser)) {
                return ok("New user added to db");
            }else{
                return internalServerError("Something went wrong with database");
            }
        } catch (IOException e) {
            return Results.badRequest("Wrong json format");
        }
    }




    @BodyParser.Of(BodyParser.Json.class)
    public static Result singIn(){
        JsonNode jsonInput = request().body().asJson();
        System.out.println(jsonInput);
        try {
            String email = jsonInput.findPath("email").textValue();
            String password = jsonInput.findPath("password").textValue();
            if(email == null | password == null){
                System.out.println("Nulle w jsonie niestety przy logowaniu");
                return badRequest("emain or password missing");
            }
            JsonNode userFromDBinJson = DBCommunication.getUserFromDB(email);
            System.out.println("odczyt z bazy");
            System.out.println(userFromDBinJson);
            BusinessUser userFromDB = BusinessUser.createBUser(userFromDBinJson,false);
            boolean authenticate = userFromDB.authenticate(email, password);
            if(authenticate){
                return ok("Tu powinien być token");
                //TODO Zaimplementuj odsyłanie tokena
            }else{
                return Results.unauthorized();
            }
        } catch (IOException e) {
            return Results.badRequest("Wrong json format");
        }
    }



    public static Result getUserFromDB(String email){
        JsonNode userInJson = DBCommunication.getUserFromDB(email);
        if(userInJson == null){
            return notFound("No such user");
        }else{
            return ok(userInJson);
        }
    }
}
