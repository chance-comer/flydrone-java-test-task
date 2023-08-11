package ru.flydrone.flydronejavatesttask.yandexcloud.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.DataNotFoundException;
import ru.flydrone.flydronejavatesttask.yandexcloud.dao.YandexCloudRepository;

import java.io.InputStream;

@Service
public class YandexCloudServiceImpl implements YandexCloudService {
    @Autowired
    private AmazonS3 yandexCloudS3Client;
    YandexCloudRepository yandexCloudRepository;
    final String bucketName = "bucket-user-profile-avatar";

    public YandexCloudServiceImpl(YandexCloudRepository yandexCloudRepository) {
        this.yandexCloudRepository = yandexCloudRepository;
    }

    @Override
    public void saveObject(String id, MultipartFile file) {
        final InputStream io;
        try {
           io = file.getInputStream();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        yandexCloudRepository.saveObject(bucketName, id, io, file.getContentType(), file.getOriginalFilename());
    }

    @Override
    public void deleteObject(String id) {
        if (id != null && yandexCloudS3Client.doesObjectExist(bucketName, id)) {
            yandexCloudRepository.deleteObject(bucketName, id);
        }
    }

    @Override
    public S3Object getObject(String id) {
        return yandexCloudRepository.getObject(bucketName, id).orElseThrow(() -> new DataNotFoundException("File not found in repository", id));
    }
}
