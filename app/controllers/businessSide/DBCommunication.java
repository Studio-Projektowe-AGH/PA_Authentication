package controllers.businessSide;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import org.mongodb.morphia.Morphia;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.io.IOException;
import java.net.UnknownHostException;


public class DBCommunication  {
    static ObjectMapper mapper = new ObjectMapper();

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

    public static boolean addBUserToDB(BusinessUser newUser){
            DBObject bUserDbObj = morphiaForBUser.toDBObject(newUser);
        WriteResult ifSaved = businessUsersCollection.save(bUserDbObj);
//        if(ifSaved.getN() != 1){
//            System.out.println("Zapisano tyle dokumentow: "+ifSaved.getN());
//            return false;
//        }else{
//            return true;
//        }
        return true;
    }

    public static JsonNode getUserFromDB(String email){
        BasicDBObject searchQuerry = new BasicDBObject();
        searchQuerry.put("email", email);

        DBCursor results = businessUsersCollection.find(searchQuerry);
        if(results.hasNext()){
            DBObject userDBObject = results.next();
            try {
                JsonNode userInJson = mapper.readTree(userDBObject.toString());
                return userInJson;
            } catch (IOException e) {
                return null;
            }
        }else{
            return null;
        }
    }

    public static boolean existUser(String email) {
        if (getUserFromDB(email) == null) {
            return false;
        }else{
            return true;
        }

    }



}
