package org.springframework.samples.dwarf.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class UserDeserializer extends JsonDeserializer<User> {

    @Autowired
    private UserService us;

    @Override
    public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        User u = null;

        try {
            String username= p.getValueAsString();
            u = us.findUser(username);
        } catch (Exception e) {
            System.out.println(e);
        }

        return u;
    }

}
