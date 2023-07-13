package ru.flydrone.flydronejavatesttask;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

@Repository
public class YandexCloudRepositoryImpl implements YandexCloudRepository {
    @Autowired
    private AmazonS3 yandexCloudS3Client;
    final String bucketName = "bucket-user-profile-avatar";

    @Override
    public Optional<String> saveObject(String id, MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setUserMetadata(Map.ofEntries(
                entry("fileName", file.getOriginalFilename())
        ));

        try {
            yandexCloudS3Client.putObject(bucketName, id, file.getInputStream(), objectMetadata);
        }
        catch (IOException ex) {
            return Optional.empty();
        }
        catch (AmazonServiceException ex) {
            return Optional.empty();
        }
        return Optional.of(id);
    }

    @Override
    public Optional<String> deleteObject(String id) {
        try {
            yandexCloudS3Client.deleteObject(bucketName, id);
            return Optional.of(id);
        }
        catch (AmazonServiceException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<MultipartFile> getObject(String id) {
        return Optional.empty();
    }
}
