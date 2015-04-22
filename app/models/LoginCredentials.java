package models;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

/**
 * Created by Wojtek on 20/04/15.
 */

@Entity(value = "users", noClassnameStored = true)
public class LoginCredentials {

    @Id
    private ObjectId id;
    private String email;
    @NotSaved
    private String password;
    @Embedded
    private Password hashedPassword;
    private Date creationTime;
    private Date lastAccessTime;
    private Boolean disabled;
    @Embedded
    private List<SocialCredentials> socialCredentials;
    @Version
    private Long v;

    public LoginCredentials() {}

    public LoginCredentials(String email, Password hashedPassword, Boolean disabled, List<SocialCredentials> socialCredentials) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.disabled = disabled;
        this.socialCredentials = socialCredentials;
    }

    public void addSocialCredential(SocialCredentials sc) {
        this.socialCredentials.add(sc);
    }

    @PrePersist
    private void hashPassword() {
        if (password != null && !password.isEmpty()) {
            this.hashedPassword = new Password(password);
        }
    }

    @PrePersist
    private void setCreationTime() {
        if (this.creationTime == null) {
            this.creationTime = new Date();
        }
    }

    @PrePersist
    public void updateLastAccesTime() {
        this.lastAccessTime = new Date();
    }

    @Embedded
    public static class Password {
        private String hash;
        private String salt;

        public Password() {}

        public Password(String password) {
            this.salt = generateSalt();
            this.hash = encrypt(password, salt);
        }

        public Boolean compare(String otherPassword) {
            String otherHash = encrypt(otherPassword, this.salt);
            return hash.equals(otherHash);
        }

        private String encrypt(String password, String salt) {
            HashFunction sha256 = Hashing.sha256();
            return sha256.hashString(password + salt, Charset.forName("UTF-8")).toString();
        }

        private String generateSalt() {
            SecureRandom sr = new SecureRandom();
            return Long.toString(sr.nextLong());
        }
    }

    public String getEmail() {
        return email;
    }

    public Password getHashedPassword() {
        return hashedPassword;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return email + ":" + hashedPassword.hash + "@" + hashedPassword.salt + ":" + creationTime + ":" + lastAccessTime + ";";
    }
}
