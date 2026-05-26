package com.example.pethealth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor_data")
public class SensorData {

    @Id
    private String id;

    private String captureId;

    private String deviceId;
    private String sensorType;
    private Object value;
    private String unit;

    private LocalDateTime measuredAt;
    private LocalDateTime capturedAt;
    private LocalDateTime receivedAt;

    public SensorData() {
    }

    public String getId() {
        return id;
    }

    public String getCaptureId() {
        return captureId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public Object getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCaptureId(String captureId) {
        this.captureId = captureId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setMeasuredAt(LocalDateTime measuredAt) {
        this.measuredAt = measuredAt;
    }

    public void setCapturedAt(LocalDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }
}