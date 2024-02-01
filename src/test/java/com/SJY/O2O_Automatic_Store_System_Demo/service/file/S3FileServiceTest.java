package com.SJY.O2O_Automatic_Store_System_Demo.service.file;

import com.SJY.O2O_Automatic_Store_System_Demo.exception.FileDeleteFailureException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.FileUploadFailureException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class S3FileServiceTest {
    @InjectMocks
    private S3FileService s3FileService;
    @Mock
    private AmazonS3 amazonS3Client;

    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(s3FileService, "bucketName", "my-bucket");
    }

    @Test
    void uploadTest() throws Exception {
        // given
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        String filename = "testFile.txt";

        // when
        s3FileService.upload(file, filename);

        // then
        verify(amazonS3Client).putObject(any(PutObjectRequest.class));
    }

    @Test
    void uploadExceptionByFileUploadFailureException() {
        // given
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]);

        // then
        doThrow(new RuntimeException("S3 Upload Failed")).when(amazonS3Client).putObject(any(PutObjectRequest.class));

        // when
        assertThrows(FileUploadFailureException.class, () -> {
            s3FileService.upload(file, "test.txt");
        });
    }

    @Test
    void deleteTest() {
        // given
        String filename = "testFile.txt";

        // when
        s3FileService.delete(filename);

        // then
        verify(amazonS3Client).deleteObject(any(String.class), eq(filename));
    }

    @Test
    void deleteExceptionByFileDeleteFailureException() {
        // given
        String filename = "nonExistentFile.txt";

        // then
        doThrow(new RuntimeException("S3 Delete Failed")).when(amazonS3Client).deleteObject(any(DeleteObjectRequest.class));

        // when
        assertThrows(FileDeleteFailureException.class, () -> {
            s3FileService.delete(filename);
        });
    }
}
