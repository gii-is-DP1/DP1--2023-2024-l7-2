package org.springframework.samples.dwarf.player;

import java.io.IOException;

import org.springframework.samples.dwarf.game.Game;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class gameSerializer extends JsonSerializer<Game> {

    @Override
    public void serialize(Game value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getCode());
    }

}
