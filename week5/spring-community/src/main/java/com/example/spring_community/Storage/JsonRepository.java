package com.example.spring_community.Storage;

import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public abstract class JsonRepository<T> {
    private final ObjectMapper objectMapper;

    public JsonRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected List<T> loadListFromJson(Path JSON_PATH, TypeReference<List<T>> typeReference) {
        try(InputStream in = Files.newInputStream(JSON_PATH)) {
            return objectMapper.readValue(in, typeReference);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_DB);
        }
    }

    protected void saveListToJson(Path JSON_PATH, String prefix, List<T> data) {
        try {
            Path temp = Files.createTempFile(JSON_PATH.getParent(), prefix, ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(temp.toFile(), data);
            Files.move(temp, JSON_PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_DB);
        }
    }

}
