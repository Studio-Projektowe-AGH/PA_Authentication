package services.data;

import com.google.inject.Provider;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import models.LoginCredentials;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import play.Play;

import java.net.UnknownHostException;

/**
 * Created by Wojtek on 29/04/15.
 */
public class FacebookDataServiceProvider implements Provider<BasicDataService<LoginCredentials, ObjectId>> {
    static FacebookDataService facebookDataService = null;

    private void createDataService() {
        String uriString = Play.application().configuration().getString("mongo.uri");
        String dbName = Play.application().configuration().getString("mongo.db");

        try {
            MongoClientURI mongoClientURI = new MongoClientURI(uriString);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            Morphia morphia = new Morphia();

            facebookDataService = new FacebookDataService(mongoClient, morphia, dbName);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
    }

    @Override
    public BasicDataService<LoginCredentials, ObjectId> get() {

        if (facebookDataService == null) {
            createDataService();
        }
        return facebookDataService;
    }
}
