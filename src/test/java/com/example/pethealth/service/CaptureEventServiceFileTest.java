package com.example.pethealth.service;

import com.example.pethealth.domain.CaptureImage;
import com.example.pethealth.domain.SensorData;
import com.example.pethealth.dto.CaptureEventResponse;
import com.example.pethealth.repository.CaptureImageRepository;
import com.example.pethealth.repository.SensorDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CaptureEventServiceFileTest {

    @Test
    void saveCaptureEventReadsDesktopCaptureTestFiles() throws Exception {
        Path captureTestDir = Path.of(System.getProperty("user.home"), "Desktop", "capture-test");
        Path imagePath = captureTestDir.resolve("capture_05262000.jpg");
        Path dataPath = captureTestDir.resolve("data_05262000.json");

        Assumptions.assumeTrue(Files.exists(imagePath), "capture-test image file is required");
        Assumptions.assumeTrue(Files.exists(dataPath), "capture-test data file is required");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        S3Service s3Service = mock(S3Service.class);
        SensorDataRepository sensorDataRepository = mock(SensorDataRepository.class);
        CaptureImageRepository captureImageRepository = mock(CaptureImageRepository.class);

        when(s3Service.uploadImage(any(), any())).thenAnswer(invocation -> invocation.getArgument(1));
        when(captureImageRepository.save(any(CaptureImage.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sensorDataRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        CaptureEventService service = new CaptureEventService(
                objectMapper,
                s3Service,
                sensorDataRepository,
                captureImageRepository
        );

        MockMultipartFile image = new MockMultipartFile(
                "image",
                imagePath.getFileName().toString(),
                "image/jpeg",
                Files.readAllBytes(imagePath)
        );
        MockMultipartFile data = new MockMultipartFile(
                "data",
                dataPath.getFileName().toString(),
                "application/json",
                Files.readAllBytes(dataPath)
        );

        CaptureEventResponse response = service.saveCaptureEvent(
                image,
                data,
                "camera-001",
                null,
                LocalDateTime.of(2026, 5, 26, 20, 0)
        );

        assertThat(response.getCaptureId()).isEqualTo("05262000");
        assertThat(response.getImageKey()).isEqualTo("captures/camera-001/2026/05/26/capture_05262000.jpg");
        assertThat(response.getSavedSensorCount()).isEqualTo(5);
        assertThat(response.getCapturedAt()).isEqualTo(LocalDateTime.of(2026, 5, 26, 20, 0));

        verify(s3Service).uploadImage(eq(image), eq("captures/camera-001/2026/05/26/capture_05262000.jpg"));
        verify(captureImageRepository).save(any(CaptureImage.class));
        verify(sensorDataRepository).saveAll(any(List.class));
    }
}
