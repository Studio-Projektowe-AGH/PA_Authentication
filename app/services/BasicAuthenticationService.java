package services;

import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

/**
 * Created by Wojtek on 22/04/15.
 */
public interface BasicAuthenticationService<T> {

    Boolean verifyCredentials(T credentials);
    Boolean verifyToken(String jwtToken) throws ParseException, JOSEException;
    String generateToken(T loginCredentials) throws JOSEException;
}
