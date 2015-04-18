package controllers.businessSide;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.*;
import org.mongodb.morphia.Morphia;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.io.IOException;
import java.net.UnknownHostException;


public class DBCommunication extends Controller {
    static MongoClientURI uri  = new MongoClientURI("mongodb://omega:omega@ds037601.mongolab.com:37601/goparty");
    static MongoClient client;
    static DB db;
    static {
        try {
            client = new MongoClient(uri);
            db = client.getDB(uri.getDatabase());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    static DBCollection businessUsersCollection = db.getCollection("businessUsers");
    static Morphia morphiaForBUser = new Morphia().map(BusinessUser.class);

    @BodyParser.Of(BodyParser.Json.class)
    public static Result addBUserToDB(){
        JsonNode jsonInput = request().body().asJson();
//        System.out.println("!!!!!\n\n@@@@@@");
        System.out.println(jsonInput);
        try {
            BusinessUser newUser = BusinessUser.createBUser(jsonInput);
            DBObject bUserDbObj = morphiaForBUser.toDBObject(newUser);
            businessUsersCollection.save(bUserDbObj);
            return ok("New user added to db");
        } catch (IOException e) {
            return Results.badRequest("Wrong json format");
        }
    }

    public static Result getUserFromDB(String email){
        BasicDBObject searchQuerry = new BasicDBObject();
        searchQuerry.put("email", email);

        DBCursor results = businessUsersCollection.find(searchQuerry);
        if(results.hasNext()){
            DBObject userInJson = results.next();

            return ok(userInJson.toString());
        }else{
            return notFound("No such user");
        }
    }
}
