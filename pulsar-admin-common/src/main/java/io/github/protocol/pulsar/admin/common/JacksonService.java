package io.github.protocol.pulsar.admin.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JacksonService {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static byte[] toBytes(@Nullable Object o) throws JsonProcessingException {
        return o == null ? null : MAPPER.writeValueAsBytes(o);
    }

    public static <T> T toObject(@Nullable byte[] json, @NotNull Class<T> type) throws IOException {
        if (json == null || json.length == 0) {
            return null;
        }
        return MAPPER.readValue(json, type);
    }

    public static <T> T toRefer(byte[] json, TypeReference<T> ref) throws IOException {
        if (json == null || json.length == 0) {
            return null;
        }
        return MAPPER.readValue(json, ref);
    }

    public static <T> List<T> toList(byte[] json, TypeReference<List<T>> typeRef) throws IOException {
        if (json == null || json.length == 0) {
            return new ArrayList<>();
        }
        return MAPPER.readValue(json, typeRef);
    }

    public static JsonNode toJsonNode(String json) throws JsonProcessingException {
        return MAPPER.readTree(json);
    }

    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return MAPPER.createArrayNode();
    }
}
