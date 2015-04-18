package controllers.businessSide;

import com.fasterxml.jackson.databind.JsonNode;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.IOException;


@Entity
public class BusinessUser {

    @Id
    ObjectId id;

    String email;
    String name;


    public BusinessUser(String email) {
        this.email = email;
    }


    public static BusinessUser createBUser(JsonNode jsonNode) throws IOException {
        String email = jsonNode.findPath("email").textValue();
        return new BusinessUser(email);

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
}
