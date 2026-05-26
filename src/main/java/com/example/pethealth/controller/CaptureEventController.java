package com.example.pethealth.controller;

import com.example.pethealth.domain.CaptureImage;
import com.example.pethealth.dto.CaptureDashboardResponse;
import com.example.pethealth.dto.CaptureEventResponse;
import com.example.pethealth.service.CaptureDashboardService;
import com.example.pethealth.service.CaptureEventService;
import com.example.pethealth.service.S3Service;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/captures")
public class CaptureEventController {

    private final CaptureEventService captureEventService;
    private final CaptureDashboardService captureDashboardService;
    private final S3Service s3Service;

    public CaptureEventController(
            CaptureEventService captureEventService,
            CaptureDashboardService captureDashboardService,
            S3Service s3Service
    ) {
        this.captureEventService = captureEventService;
        this.captureDashboardService = captureDashboardService;
        this.s3Service = s3Service;
    }

    @GetMapping
    public List<CaptureDashboardResponse> getRecentCaptures(
            @RequestParam(defaultValue = "12") int limit
    ) {
        return captureDashboardService.getRecentCaptures(limit);
    }

    @GetMapping("/{captureId}/image")
    public ResponseEntity<Resource> getCaptureImage(@PathVariable String captureId) {
        CaptureImage captureImage = captureDashboardService.getCaptureImage(captureId);

        String contentType = captureImage.getContentType() != null
                ? captureImage.getContentType()
                : MediaType.IMAGE_JPEG_VALUE;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(s3Service.getImage(captureImage.getImageKey()));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CaptureEventResponse uploadCaptureEvent(
            @RequestPart("image") MultipartFile image,
            @RequestPart("data") MultipartFile data,
            @RequestParam(defaultValue = "camera-001") String cameraDeviceId,
            @RequestParam(required = false) String captureId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime capturedAt
    ) {
        return captureEventService.saveCaptureEvent(
                image,
                data,
                cameraDeviceId,
                captureId,
                capturedAt
        );
    }
}
