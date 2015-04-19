package controllers.businessSide;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.binary.Base64;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Entity
public class BusinessUser {

    @Id
    ObjectId id;

    public BusinessUser(String email, String name, String passowrd) {
        this.email = email;
        this.name = name;
        this.password = passowrd;
    }

    String email;
    String name;
    String password;




    public static BusinessUser createBUser(JsonNode jsonNode, boolean hashPassword) throws IOException {
        String email = jsonNode.findPath("email").textValue();
        String name = jsonNode.findPath("name").textValue();
        String password = jsonNode.findPath("password").textValue();
        if(email == null | name == null | password == null){
            System.out.println("Nulle w jsonie niestety");
            return null;
        }
        if(hashPassword){
            password = HashingClass.hashPassword(password);
        }
        return new BusinessUser(email, name, password);

    }


    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
            return "błąd IO w toString()";
        }
    }

    public boolean authenticate(String email, String unHashedPassword) {
        String hashedPassword = HashingClass.hashPassword(unHashedPassword);
//        System.out.println("myemail:"+this.email +"my password:" + this.password);
//        System.out.println("given email:" +email+"given passwrod:" + hashedPassword);
        if(this.email.equals(email) && this.password.equals(hashedPassword)){
            return true;
        }else{
            return false;
        }
    }
}

class HashingClass{
    static String hashPassword(String realPassword){
        try {
            byte[] bytesOfMessage = realPassword.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestBytes = md.digest(bytesOfMessage);
            String digestString = Base64.encodeBase64String(digestBytes);
            return digestString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
