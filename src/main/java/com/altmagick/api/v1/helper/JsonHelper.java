package com.altmagick.api.v1.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;

public class JsonHelper {
    private static final Logger LOG = Logger.getLogger(JsonHelper.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonHelper() {
    }

    public static String serializeToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOG.error("Error serializing object to JSON", e);
            return "{\"error\":{\"code\":500, \"message\":\"Serialization error\"}}";
        }
    }
}