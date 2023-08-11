package ru.flydrone.flydronejavatesttask.yandexcloud.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.DataNotFoundException;
import ru.flydrone.flydronejavatesttask.yandexcloud.dao.YandexCloudRepository;

import java.io.InputStream;

@Service
public class YandexCloudServiceImpl implements YandexCloudService {
    YandexCloudRepository yandexCloudRepository;

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
        yandexCloudRepository.saveObject(id, io, file.getContentType(), file.getOriginalFilename());
    }

    @Override
    public void deleteObject(String id) {
        yandexCloudRepository.deleteObject(id);
    }

    @Override
    public S3Object getObject(String id) {
        return yandexCloudRepository.getObject(id).orElseThrow(() -> new DataNotFoundException("File not found in repository", id));
    }
}
