package com.zsgs.busbooking.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {

    @Override
    public void write(JsonWriter out, LocalTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toString()); // HH:mm:ss
        }
    }

    @Override
    public LocalTime read(JsonReader in) throws IOException {
        return LocalTime.parse(in.nextString());
    }
}
