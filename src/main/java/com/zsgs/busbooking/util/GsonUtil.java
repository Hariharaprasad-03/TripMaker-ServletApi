package com.zsgs.busbooking.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.time.LocalTime;

public class GsonUtil {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // From previous step
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter()) // From previous step
            .setPrettyPrinting()
            .create();

    public static Gson getGson() {
        return GSON;
    }
}