package com.SJY.O2O_Automatic_Store_System_Demo.service.file;

import com.SJY.O2O_Automatic_Store_System_Demo.exception.FileDeleteFailureException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.FileUploadFailureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Profile({"local", "test"})
@Service
public class LocalFileService implements FileService{

    @Value("${upload.image.location}")
    private String location;

    @PostConstruct
    void postConstruct() {
        File dir = new File(location);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    @Override
    public void upload(MultipartFile file, String filename) {
        try {
            Path targetLocation = new File(location + filename).toPath();
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch(Exception e) {
            throw new FileUploadFailureException(e);
        }
    }
    @Override
    public void delete(String filename) {
        File file = new File(location + filename);
        boolean deleted = file.delete();
        if (!deleted) {
            throw new FileDeleteFailureException();
        }
    }
}