package controllers.businessSide;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

public class BusinessUserTest extends TestCase {

    @Test
    public void createBUserTest(){
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode jsonNode = mapper.readTree("{\"email\":\"krzysiekplachno@gmail.com\"");
            //BusinessUser businessUser = BusinessUser.createBUser(jsonNode);
            //System.out.println(businessUser);
        } catch (IOException e) {
            //assertTrue("wyjatek", false);
        }

    }

}