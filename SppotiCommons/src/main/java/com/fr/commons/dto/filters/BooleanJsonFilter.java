package com.fr.commons.dto.filters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by djenanewail on 1/29/17.
 */
public class BooleanJsonFilter extends JsonSerializer<Boolean> {
    @Override
    public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider serializers) throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        if (!value) {
            jgen.writeBoolean(value);
        }
        jgen.writeEndObject();
    }
}
