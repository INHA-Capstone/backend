package com.example.pethealth.repository;

import com.example.pethealth.domain.CaptureImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CaptureImageRepository extends MongoRepository<CaptureImage, String> {

    Optional<CaptureImage> findByCaptureId(String captureId);

    List<CaptureImage> findAllByOrderByCapturedAtDesc(Pageable pageable);

    Optional<CaptureImage> findTopByOrderByCapturedAtDesc();
}
