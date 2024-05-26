package org.example.util;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.io.Serial;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public final class JsonUtil {

    private JsonUtil() {
    }

    public static DocumentContext readValue(String source) {
        try {
            return JsonPath.parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T jsonPath(DocumentContext context, String path) {
        if(context == null) {
            return null;
        }
        try {
            return context.read(path);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T readValue(Object context, String path) {
        if(context == null) {
            return null;
        }
        try {
            return JsonPath.read(context, path);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> String toJson(T value) {
        try {
            return getInstance().writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(String content, Class<T> valueType) {
        try {
            return getInstance().readValue(content, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(String content, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(content, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String content, Class<T> valueTypeRef) {
        try {
            return getInstance().readerForListOf(valueTypeRef).readValue(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> toMap(String content) {
        try {
            return (Map) getInstance().readValue(content, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toPojo(Map<String, Object> fromValue, Class<T> toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    public static ObjectMapper getInstance() {
        return JsonUtil.JacksonHolder.INSTANCE;
    }

    private static class JacksonHolder {
        private static final ObjectMapper INSTANCE = new JacksonObjectMapper();

        private JacksonHolder() {
        }
    }

    public static class JacksonObjectMapper extends ObjectMapper {
        @Serial
        private static final long serialVersionUID = 1L;

        public JacksonObjectMapper() {
            super.setLocale(Locale.CHINA).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault())).setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)).configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true).configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true).findAndRegisterModules().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(Feature.ALLOW_SINGLE_QUOTES, true).getDeserializationConfig().withoutFeatures(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES});
            super.findAndRegisterModules();
        }
    }
}
