package com.SJY.O2O_Automatic_Store_System_Demo.service.file;

import com.SJY.O2O_Automatic_Store_System_Demo.exception.FileDeleteFailureException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.FileUploadFailureException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Profile("dev")
@Service
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public void upload(MultipartFile file, String filename) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, file.getInputStream(), objectMetadata);

            amazonS3Client.putObject(putObjectRequest);
        } catch(Exception e) {
            throw new FileUploadFailureException(e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            amazonS3Client.deleteObject(bucketName, filename);
        } catch (Exception e) {
            throw new FileDeleteFailureException(e);
        }
    }
}