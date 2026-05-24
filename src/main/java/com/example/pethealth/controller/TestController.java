package com.example.pethealth.controller;

import com.example.pethealth.domain.TestData;
import com.example.pethealth.repository.TestDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {

    private final TestDataRepository testDataRepository;

    @PostMapping
    public TestData createTestData() {
        TestData testData = TestData.builder()
                .message("MongoDB Atlas connection success")
                .build();

        return testDataRepository.save(testData);
    }

    @GetMapping
    public List<TestData> getTestDataList() {
        return testDataRepository.findAll();
    }
}