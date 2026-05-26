package com.example.pethealth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SensorDataRequest {

    @NotBlank(message = "deviceId는 필수입니다.")
    private String deviceId;

    @NotBlank(message = "sensorType은 필수입니다.")
    private String sensorType;

    @NotNull(message = "value는 필수입니다.")
    private Object value;

    @NotBlank(message = "unit은 필수입니다.")
    private String unit;

    private LocalDateTime measuredAt;

    public SensorDataRequest() {
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
}