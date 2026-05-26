package com.example.pethealth.service;

import com.example.pethealth.domain.CaptureImage;
import com.example.pethealth.dto.CaptureDashboardResponse;
import com.example.pethealth.repository.CaptureImageRepository;
import com.example.pethealth.repository.SensorDataRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
public class CaptureDashboardService {

    private static final int MAX_LIMIT = 50;

    private final CaptureImageRepository captureImageRepository;
    private final SensorDataRepository sensorDataRepository;

    public CaptureDashboardService(
            CaptureImageRepository captureImageRepository,
            SensorDataRepository sensorDataRepository
    ) {
        this.captureImageRepository = captureImageRepository;
        this.sensorDataRepository = sensorDataRepository;
    }

    public List<CaptureDashboardResponse> getRecentCaptures(int limit) {
        int pageSize = Math.max(1, Math.min(limit, MAX_LIMIT));

        return captureImageRepository.findAllByOrderByCapturedAtDesc(PageRequest.of(0, pageSize))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CaptureImage getCaptureImage(String captureId) {
        return captureImageRepository.findByCaptureId(captureId)
                .orElseThrow(() -> new IllegalArgumentException("Capture not found: " + captureId));
    }

    private CaptureDashboardResponse toResponse(CaptureImage image) {
        return new CaptureDashboardResponse(
                image,
                buildImageUrl(image.getCaptureId()),
                sensorDataRepository.findByCaptureId(image.getCaptureId())
        );
    }

    private String buildImageUrl(String captureId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/captures/")
                .path(captureId)
                .path("/image")
                .toUriString();
    }
}
