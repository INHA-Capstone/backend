package com.example.pethealth.service;

import com.example.pethealth.domain.CaptureImage;
import com.example.pethealth.domain.SensorData;
import com.example.pethealth.dto.CaptureEventResponse;
import com.example.pethealth.dto.SensorDataRequest;
import com.example.pethealth.repository.CaptureImageRepository;
import com.example.pethealth.repository.SensorDataRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CaptureEventService {

    private final ObjectMapper objectMapper;
    private final S3Service s3Service;
    private final SensorDataRepository sensorDataRepository;
    private final CaptureImageRepository captureImageRepository;

    public CaptureEventService(
            ObjectMapper objectMapper,
            S3Service s3Service,
            SensorDataRepository sensorDataRepository,
            CaptureImageRepository captureImageRepository
    ) {
        this.objectMapper = objectMapper;
        this.s3Service = s3Service;
        this.sensorDataRepository = sensorDataRepository;
        this.captureImageRepository = captureImageRepository;
    }

    public CaptureEventResponse saveCaptureEvent(
            MultipartFile image,
            MultipartFile data,
            String cameraDeviceId,
            String captureId,
            LocalDateTime capturedAt
    ) {
        LocalDateTime now = LocalDateTime.now();

        String tempCaptureId = hasText(captureId)
                ? captureId
                : extractCaptureIdFromFilename(image.getOriginalFilename());

        if (!hasText(tempCaptureId)) {
            tempCaptureId = UUID.randomUUID().toString();
        }

        final String resolvedCaptureId = tempCaptureId;

        LocalDateTime resolvedCapturedAt = capturedAt != null ? capturedAt : now;

        List<SensorDataRequest> sensorRequests = parseSensorDataFile(data);

        String imageKey = buildImageKey(
                cameraDeviceId,
                resolvedCaptureId,
                resolvedCapturedAt,
                image.getOriginalFilename()
        );

        s3Service.uploadImage(image, imageKey);

        CaptureImage captureImage = new CaptureImage();
        captureImage.setCaptureId(resolvedCaptureId);
        captureImage.setCameraDeviceId(cameraDeviceId);
        captureImage.setOriginalFilename(image.getOriginalFilename());
        captureImage.setImageKey(imageKey);
        captureImage.setContentType(image.getContentType());
        captureImage.setCapturedAt(resolvedCapturedAt);
        captureImage.setUploadedAt(now);

        captureImageRepository.save(captureImage);

        List<SensorData> sensorDataList = sensorRequests.stream()
                .map(request -> toSensorData(
                        request,
                        resolvedCaptureId,
                        resolvedCapturedAt,
                        now
                ))
                .toList();

        sensorDataRepository.saveAll(sensorDataList);

        return new CaptureEventResponse(
                resolvedCaptureId,
                imageKey,
                sensorDataList.size(),
                resolvedCapturedAt
        );
    }

    private List<SensorDataRequest> parseSensorDataFile(MultipartFile data) {
        try {
            return objectMapper.readValue(
                    data.getInputStream(),
                    new TypeReference<List<SensorDataRequest>>() {
                    }
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("센서 JSON 파일 파싱에 실패했습니다.");
        }
    }

    private SensorData toSensorData(
            SensorDataRequest request,
            String captureId,
            LocalDateTime capturedAt,
            LocalDateTime receivedAt
    ) {
        SensorData sensorData = new SensorData();

        sensorData.setCaptureId(captureId);
        sensorData.setDeviceId(request.getDeviceId());
        sensorData.setSensorType(request.getSensorType());
        sensorData.setValue(request.getValue());
        sensorData.setUnit(request.getUnit());
        sensorData.setMeasuredAt(
                request.getMeasuredAt() != null
                        ? request.getMeasuredAt()
                        : capturedAt
        );
        sensorData.setCapturedAt(capturedAt);
        sensorData.setReceivedAt(receivedAt);

        return sensorData;
    }

    private String buildImageKey(
            String cameraDeviceId,
            String captureId,
            LocalDateTime capturedAt,
            String originalFilename
    ) {
        String safeCameraDeviceId = hasText(cameraDeviceId)
                ? cameraDeviceId
                : "camera-001";

        String safeFilename = hasText(originalFilename)
                ? originalFilename
                : "capture_" + captureId + ".jpg";

        return String.format(
                "captures/%s/%04d/%02d/%02d/%s",
                safeCameraDeviceId,
                capturedAt.getYear(),
                capturedAt.getMonthValue(),
                capturedAt.getDayOfMonth(),
                safeFilename
        );
    }

    private String extractCaptureIdFromFilename(String filename) {
        if (!hasText(filename)) {
            return null;
        }

        String name = filename;

        int dotIndex = name.lastIndexOf(".");
        if (dotIndex > 0) {
            name = name.substring(0, dotIndex);
        }

        if (name.startsWith("capture_")) {
            name = name.substring("capture_".length());
        }

        name = name.replace("[", "").replace("]", "");

        return hasText(name) ? name : null;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}