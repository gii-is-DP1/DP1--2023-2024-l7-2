package org.springframework.samples.petclinic.player;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class gameDeserializer extends JsonDeserializer<Game> {

    @Autowired
    private GameService gs;

    @Override
    public Game deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        Game g = null;

        try {
            g = gs.getGameByCode(p.getText());
        } catch (Exception e) {
            System.out.println(e);
        }

        return g;
    }

    
}
