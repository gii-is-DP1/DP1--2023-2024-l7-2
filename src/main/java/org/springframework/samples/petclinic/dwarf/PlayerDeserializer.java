package org.springframework.samples.petclinic.dwarf;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayereService;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class PlayerDeserializer extends JsonDeserializer<Player> {

    @Autowired
    private PlayereService ps;

    @Override
    public Player deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        Player g = null;

        try {
            g = ps.getByName(p.getText());
        } catch (Exception e) {
            System.out.println(e);
        }

        return g;
    }

    
}
