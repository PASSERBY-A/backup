package com.cmsz.cloudplatform.utils;

import java.util.Map;

import com.google.gson.GsonBuilder;
import com.hp.core.model.ResponseObject;

public class GsonHelper {
    private static final GsonBuilder s_gBuilder;
    static {
        s_gBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        s_gBuilder.setVersion(1.3);
        s_gBuilder.registerTypeAdapter(ResponseObject.class, new ResponseObjectTypeAdapter());
        s_gBuilder.registerTypeAdapter(Map.class, new StringMapTypeAdapter());
    }

    public static GsonBuilder getBuilder() {
        return s_gBuilder;
    }
}
