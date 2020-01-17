package com.example.backend.miscellaneous;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

public class MyJsonDateTimeSerializer extends JsonSerializer<DateTime> implements ContextualSerializer {
    private String format;

    public MyJsonDateTimeSerializer() {}

    public MyJsonDateTimeSerializer(String format) {
        this.format = format;
    }

    @Override
    public void serialize(DateTime dt, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        jg.writeString(formatter.print(dt));
    }
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider sp, BeanProperty bp) throws JsonMappingException {
        return new MyJsonDateTimeSerializer(bp.getAnnotation(MyDateTimeFormat.class).value());
    }
}
