package services;

import com.nimbusds.jose.JOSEException;
import models.LoginCredentials;

import java.text.ParseException;

/**
 * Created by Wojtek on 22/04/15.
 */
public interface BasicAuthenticationService {

    Boolean verifyCredentials(LoginCredentials loginCredentials);
    Boolean verifyToken(String jwtToken) throws ParseException, JOSEException;
    String generateToken(LoginCredentials loginCredentials) throws JOSEException;

}
