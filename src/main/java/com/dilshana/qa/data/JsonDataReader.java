package com.dilshana.qa.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/** Reads a JSON array from the classpath into a list of typed records. */
public final class JsonDataReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonDataReader() {
    }

    public static <T> List<T> readList(String resource, Class<T> type) {
        try (InputStream in = JsonDataReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Test data file not found: " + resource);
            }
            return MAPPER.readerForListOf(type).readValue(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read test data " + resource, e);
        }
    }
}
