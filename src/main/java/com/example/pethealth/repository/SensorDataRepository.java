package com.example.pethealth.repository;

import com.example.pethealth.domain.SensorData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SensorDataRepository extends MongoRepository<SensorData, String> {

    List<SensorData> findByCaptureId(String captureId);

    List<SensorData> findByDeviceId(String deviceId, Pageable pageable);

    List<SensorData> findBySensorType(String sensorType, Pageable pageable);

    List<SensorData> findByDeviceIdAndSensorType(String deviceId, String sensorType, Pageable pageable);

    Optional<SensorData> findTopByOrderByMeasuredAtDesc();

    Optional<SensorData> findTopByDeviceIdOrderByMeasuredAtDesc(String deviceId);

    Optional<SensorData> findTopBySensorTypeOrderByMeasuredAtDesc(String sensorType);

    Optional<SensorData> findTopByDeviceIdAndSensorTypeOrderByMeasuredAtDesc(String deviceId, String sensorType);
}