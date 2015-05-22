package services.data;

import org.mongodb.morphia.dao.DAO;
import org.mongodb.morphia.query.Query;
import play.Logger;

import java.util.List;
import java.util.Random;

/**
 * Created by Wojtek on 21/04/15.
 */
public interface BasicDataService<T, K> extends DAO<T, K> {

    boolean exists(T element);

    List<T> findByCredentials(T element);

    T findOneByCredentials(T element);

    void updateLastAccessTime();

    default T findOneRandom() {
        Integer countTotal = (int) createQuery().countAll();
        Integer randomOffset = Math.abs(new Random().nextInt()) % countTotal;
        Query<T> query = createQuery().offset(randomOffset);
        Logger.debug("Find One Random (" + randomOffset + "): " + query.toString());

        return findOne(query);
    }
}
