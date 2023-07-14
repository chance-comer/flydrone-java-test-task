package ru.flydrone.flydronejavatesttask;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface YandexCloudRepository {
    Optional<String> saveObject(String id, MultipartFile file);
    Optional<String> deleteObject(String id);
    Optional<S3Object> getObject(String id);
}
