package ru.flydrone.flydronejavatesttask.yandexcloud.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface YandexCloudService {
    void saveObject(String id, MultipartFile file);
    void deleteObject(String id);
    S3Object getObject(String id);
}
