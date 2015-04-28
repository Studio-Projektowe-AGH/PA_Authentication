package models;

import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by Wojtek on 20/04/15.
 */

@Entity
public class SocialCredentials {
    private String providerName;
    private String accountId;
    private String accessToken;
    private Date expiresOn;

    public SocialCredentials() {}

    public SocialCredentials(String providerName, String accessToken, Date expiresOn) {
        this.providerName = providerName;
        this.accessToken = accessToken;
        this.expiresOn = expiresOn;
    }

    public String getProviderName() {
        return providerName;
    }
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public Date getExpiresOn() {
        return expiresOn;
    }
    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }
}
