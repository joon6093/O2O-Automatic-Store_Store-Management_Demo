package com.SJY.O2O_Automatic_Store_System_Demo.service.file;

import com.SJY.O2O_Automatic_Store_System_Demo.exception.FileDeleteFailureException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.FileUploadFailureException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocalFileServiceTest {
    LocalFileService localFileService = new LocalFileService();
    String testLocation = new File("src/test/resources/static").getAbsolutePath() + "/";

    @BeforeEach
    void beforeEach() throws IOException {
        ReflectionTestUtils.setField(localFileService, "location", testLocation);
        FileUtils.cleanDirectory(new File(testLocation));
    }

    @Test
    void postConstructTest() throws IOException {
        // given
        String dirPath = testLocation + "testDir/";
        ReflectionTestUtils.setField(localFileService, "location", dirPath);
        FileUtils.cleanDirectory(new File(testLocation));

        // when
        localFileService.postConstruct();

        // then
        assertThat(new File(dirPath).exists()).isTrue();
    }

    @Test
    void uploadTest() {
        // given
        MultipartFile file = new MockMultipartFile("myFile", "myFile.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        String filename = "testFile.txt";

        // when
        localFileService.upload(file, filename);

        // then
        assertThat(isExists(testLocation + filename)).isTrue();
    }

    @Test
    void uploadExceptionByFileUploadFailureException() throws IOException {
        // given
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenThrow(new IOException("Test exception"));

        // when & then
        assertThatThrownBy(() -> localFileService.upload(mockFile, "testFile.txt"))
                .isInstanceOf(FileUploadFailureException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    void deleteTest() {
        // given
        MultipartFile file = new MockMultipartFile("myFile", "myFile.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        String filename = "testFile.txt";
        localFileService.upload(file, filename);
        boolean before = isExists(testLocation + filename);

        // when
        localFileService.delete(filename);

        // then
        boolean after = isExists(testLocation + filename);
        assertThat(before).isTrue();
        assertThat(after).isFalse();
    }

    @Test
    void deleteExceptionByFileDeleteFailureException() {
        // given
        String nonExistentFilename = "nonExistentFile.txt";

        // when & then
        assertThatThrownBy(() -> localFileService.delete(nonExistentFilename))
                .isInstanceOf(FileDeleteFailureException.class);
    }

    boolean isExists(String filePath) {
        return new File(filePath).exists();
    }
}