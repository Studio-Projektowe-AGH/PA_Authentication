package services;

import org.mongodb.morphia.dao.DAO;

import java.util.List;

/**
 * Created by Wojtek on 21/04/15.
 */
public interface BasicDataService<T, K> extends DAO<T, K> {

    boolean exists(T element);
    List<T> findByCredentials(T element);
    T findOneByCredentials(T element);
    void updateLastAccessTime();
}
