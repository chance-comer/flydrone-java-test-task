package ru.flydrone.flydronejavatesttask.yandexcloud.dao;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;

public interface YandexCloudRepository {
    void saveObject(String id, InputStream file, String contentType, String fileName);
    void deleteObject(String id);
    Optional<S3Object> getObject(String id);
}
