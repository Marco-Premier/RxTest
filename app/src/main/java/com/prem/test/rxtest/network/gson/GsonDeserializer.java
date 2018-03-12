package com.prem.test.rxtest.network.gson;

/**
 * Created by prem on 01/03/2018.
 */
public class GsonDeserializer implements JsonDeserializer<B> {

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        User user = new User();
        // custom parsing logic goes here
        return user;
    }
}