package com.SJY.O2O_Automatic_Store_System_Demo.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void upload(MultipartFile file, String filename);
    void delete(String filename);
}