package services.data;

import com.mongodb.MongoClient;
import models.LoginCredentials;
import models.SocialCredentials;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wojtek on 28/04/15.
 */
public class FacebookDataService extends BasicDAO<LoginCredentials, ObjectId> implements BasicDataService<LoginCredentials,ObjectId> {

    public FacebookDataService(MongoClient mongo, Morphia morphia, String dbName) {
        super(mongo, morphia, dbName);
    }

    @Override
    public boolean exists(LoginCredentials credentials) {
        String accountId = credentials.getSocialCredentials().get(0).getAccountId();
        Query<LoginCredentials> query = createQuery().field("socialCredentials.accountId").contains(accountId);
        Logger.debug("Exists: " + query.toString());

        return exists(query);
    }

    @Override
    public List<LoginCredentials> findByCredentials(LoginCredentials credentials) {
        List<String> accountIds = new ArrayList<>();
        for (SocialCredentials credential : credentials.getSocialCredentials()) {
            accountIds.add(credential.getAccountId());
        }
        Query<LoginCredentials> query = createQuery().field("socialCredentials.accountId").in(accountIds);
        Logger.debug("Find By Credentials: " + query.toString());

        return find(query).asList();
    }

    @Override
    public LoginCredentials findOneByCredentials(LoginCredentials credentials) {
        String accountId = credentials.getSocialCredentials().get(0).getAccountId();
        Query<LoginCredentials> query = createQuery().field("socialCredentials.accountId").contains(accountId);
        Logger.debug("Find One By Credentials: " + query.toString());

        return findOne(query);
    }

    @Override
    public void updateLastAccessTime() {

    }
}
