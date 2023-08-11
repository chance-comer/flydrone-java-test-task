package ru.flydrone.flydronejavatesttask.yandexcloud.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.DataNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

@Repository
public class YandexCloudRepositoryImpl implements YandexCloudRepository {
    @Autowired
    private AmazonS3 yandexCloudS3Client;

    @Override
    public void saveObject(String bucketName, String id, InputStream file, String contentType, String fileName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setUserMetadata(Map.ofEntries(
                entry("fileName", fileName)
        ));

        yandexCloudS3Client.putObject(bucketName, id, file, objectMetadata);
    }

    @Override
    public void deleteObject(String bucketName, String id) {
            yandexCloudS3Client.deleteObject(bucketName, id);
    }

    @Override
    public Optional<S3Object> getObject(String bucketName, String id) throws DataNotFoundException {
        if (yandexCloudS3Client.doesObjectExist(bucketName, id)) {
            return Optional.of(yandexCloudS3Client.getObject(bucketName, id));
        }
        return Optional.empty();
    }
}
