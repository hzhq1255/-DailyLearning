package org.hzhq.myutil.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2022-11-27 下午5:10
 */
public class JsonUtils {


    private static final Logger LOGGER = Logger.getLogger("JSON Utils");

    private static final UncheckedObjectMapper UNCHECKED_OBJECT_MAPPER = new UncheckedObjectMapper();

    public static <T> T jsonToObj(String json, Class<T> clazz){
        return UNCHECKED_OBJECT_MAPPER.readValue(json, clazz);
    }

    public static <T> T jsonToObj(String json, TypeReference<T> typeReference){
        return UNCHECKED_OBJECT_MAPPER.readValue(json, typeReference);
    }

    public static String objToJson(Object o){
        return UNCHECKED_OBJECT_MAPPER.writeValueAsString(o);
    }

    public static boolean isJSONValid(String json){
        return UNCHECKED_OBJECT_MAPPER.isJSON(json);
    }



    public static class UncheckedObjectMapper extends ObjectMapper {
        /**
         * Parses the given JSON string into a given type
         */
        public <T> T readValue(String content, Class<T> clazz) {
            try {
                Objects.requireNonNull(clazz, "convert to type clazz cannot be null");
                return super.readValue(content, clazz);
            } catch (IOException ioe) {
                LOGGER.log(Level.WARNING, String.format("JSON read Value to %s failed ", clazz), ioe);
                throw new  CompletionException(ioe);
            }
        }

        public <T> T readValue(String content, TypeReference<T> typeReference){
            try {
                Objects.requireNonNull(typeReference, "convert to type cannot be null");
                return super.readValue(content, typeReference);
            } catch (IOException ioe){
                LOGGER.log(Level.WARNING, String.format("JSON read Value to %s failed ", typeReference), ioe);
                throw new  CompletionException(ioe);
            }
        }

        public String writeValueAsString(Object o){
            try {
                return super.writeValueAsString(o);
            }catch (IOException ioe){
                LOGGER.log(Level.WARNING, String.format("write Value %s to JSON String failed ", o), ioe);
                throw new  CompletionException(ioe);
            }
        }

        public boolean isJSON(String content){
            try {
                super.readTree(content);
                return true;
            } catch (IOException e){
                return false;
            }
        }
    }
}
