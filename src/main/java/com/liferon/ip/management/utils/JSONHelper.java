package com.liferon.ip.management.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class JSONHelper {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setVisibility(MAPPER.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        MAPPER.registerModule(new JavaTimeModule());
    }

    public static String fileToString(final String path) throws IOException {
        final InputStream inputStream = new ClassPathResource(path).getInputStream();

        return IOUtils.toString(inputStream, String.valueOf(StandardCharsets.UTF_8));
    }

    public static <T> T fileToBean(final String path, final Class<T> type) throws IOException {
        final File file = new ClassPathResource(path).getFile();

        return MAPPER.readValue(file, TypeFactory.defaultInstance().constructType(type));
    }

    public static <T> List<T> fileToBeanList(final String path, final Class<T> type) throws IOException {
        final File file = new ClassPathResource(path).getFile();

        return fileToBeanList(file, type);
    }

    public static <T> List<T> fileToBeanList(final File file, final Class<T> type) throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().
                constructCollectionType(List.class, type);

        return MAPPER.readValue(file, TypeFactory.defaultInstance().constructType(javaType));
    }

}