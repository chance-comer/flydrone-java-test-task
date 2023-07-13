package ru.flydrone.flydronejavatesttask;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface YandexCloudRepository {
    Optional<String> saveObject(String id, MultipartFile file);
    Optional<String> deleteObject(String id);
    Optional<MultipartFile> getObject(String id);
}
