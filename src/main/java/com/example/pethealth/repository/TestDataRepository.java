package com.example.pethealth.repository;

import com.example.pethealth.domain.TestData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestDataRepository extends MongoRepository<TestData, String> {
}