package org.springframework.samples.dwarf.dwarf;

import java.io.IOException;

import org.springframework.samples.dwarf.player.Player;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PlayerSerializer extends JsonSerializer<Player> {

    @Override
    public void serialize(Player value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getName());
    }

}
