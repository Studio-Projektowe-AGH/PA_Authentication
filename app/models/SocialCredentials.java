package models;

import org.mongodb.morphia.annotations.Embedded;

import java.util.Date;

/**
 * Created by Wojtek on 20/04/15.
 */

@Embedded
public class SocialCredentials {
    String providerName;
    String accessToken;
    Date expiresOn;

    public SocialCredentials(String providerName, String accessToken, Date expiresOn) {
        this.providerName = providerName;
        this.accessToken = accessToken;
        this.expiresOn = expiresOn;
    }
}
