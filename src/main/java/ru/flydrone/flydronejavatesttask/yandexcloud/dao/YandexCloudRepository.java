package ru.flydrone.flydronejavatesttask.yandexcloud.dao;

import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;
import java.util.Optional;

public interface YandexCloudRepository {
    void saveObject(String bucketName, String id, InputStream file, String contentType, String fileName);
    void deleteObject(String bucketName, String id);
    Optional<S3Object> getObject(String bucketName, String id);
}
