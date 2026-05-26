package com.example.pethealth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "capture_images")
public class CaptureImage {

    @Id
    private String id;

    private String captureId;
    private String cameraDeviceId;

    private String originalFilename;
    private String imageKey;
    private String contentType;

    private LocalDateTime capturedAt;
    private LocalDateTime uploadedAt;

    public CaptureImage() {
    }

    public String getId() {
        return id;
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

    public String getImageKey() {
        return imageKey;
    }

    public String getContentType() {
        return contentType;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCaptureId(String captureId) {
        this.captureId = captureId;
    }

    public void setCameraDeviceId(String cameraDeviceId) {
        this.cameraDeviceId = cameraDeviceId;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setCapturedAt(LocalDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}