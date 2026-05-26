package com.example.pethealth.dto;

import java.time.LocalDateTime;

public class CaptureEventResponse {

    private String captureId;
    private String imageKey;
    private int savedSensorCount;
    private LocalDateTime capturedAt;

    public CaptureEventResponse() {
    }

    public CaptureEventResponse(
            String captureId,
            String imageKey,
            int savedSensorCount,
            LocalDateTime capturedAt
    ) {
        this.captureId = captureId;
        this.imageKey = imageKey;
        this.savedSensorCount = savedSensorCount;
        this.capturedAt = capturedAt;
    }

    public String getCaptureId() {
        return captureId;
    }

    public String getImageKey() {
        return imageKey;
    }

    public int getSavedSensorCount() {
        return savedSensorCount;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }
}