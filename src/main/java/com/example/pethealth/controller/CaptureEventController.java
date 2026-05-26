package com.example.pethealth.controller;

import com.example.pethealth.dto.CaptureEventResponse;
import com.example.pethealth.service.CaptureEventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/captures")
public class CaptureEventController {

    private final CaptureEventService captureEventService;

    public CaptureEventController(CaptureEventService captureEventService) {
        this.captureEventService = captureEventService;
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