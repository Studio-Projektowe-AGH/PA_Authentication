package services.data;

import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import models.LoginCredentials;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import play.Logger;

import java.util.List;

/**
 * Created by Wojtek on 20/04/15.
 */

@Singleton
public class UserDataService extends BasicDAO<LoginCredentials, ObjectId> implements BasicDataService<LoginCredentials,ObjectId> {

    public UserDataService(MongoClient mongo, Morphia morphia, String dbName) {
        super(mongo, morphia, dbName);
    }

    @Override
    public boolean exists(LoginCredentials loginCredentials) {
        Query<LoginCredentials> query = createQuery().field("email").equal(loginCredentials.getEmail().toLowerCase());
        Logger.debug("Exists: " + query.toString());

        return exists(query);
    }

    @Override
    public List<LoginCredentials> findByCredentials(LoginCredentials credentials) {
        Query<LoginCredentials> query = createQuery().field("email").equal(credentials.getEmail().toLowerCase());
        Logger.debug("Find By Credentials: " + query.toString());

        return find(query).asList();
    }

    @Override
    public LoginCredentials findOneByCredentials(LoginCredentials credentials) {
        Query<LoginCredentials> query = createQuery().field("email").equal(credentials.getEmail().toLowerCase());
        Logger.debug("Find One By Credentials: " + query.toString());

        return findOne(query);
    }

    @Override
    public void updateLastAccessTime() {

    }
}
