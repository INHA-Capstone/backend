package com.example.pethealth.dto;

import com.example.pethealth.domain.CaptureImage;
import com.example.pethealth.domain.SensorData;

import java.time.LocalDateTime;
import java.util.List;

public class CaptureDashboardResponse {

    private String captureId;
    private String cameraDeviceId;
    private String originalFilename;
    private String imageUrl;
    private LocalDateTime capturedAt;
    private LocalDateTime uploadedAt;
    private List<SensorData> sensors;

    public CaptureDashboardResponse() {
    }

    public CaptureDashboardResponse(
            CaptureImage image,
            String imageUrl,
            List<SensorData> sensors
    ) {
        this.captureId = image.getCaptureId();
        this.cameraDeviceId = image.getCameraDeviceId();
        this.originalFilename = image.getOriginalFilename();
        this.imageUrl = imageUrl;
        this.capturedAt = image.getCapturedAt();
        this.uploadedAt = image.getUploadedAt();
        this.sensors = sensors;
    }

    public String getCaptureId() {
        return captureId;
    }

    public String getCameraDeviceId() {
        return cameraDeviceId;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public List<SensorData> getSensors() {
        return sensors;
    }
}
