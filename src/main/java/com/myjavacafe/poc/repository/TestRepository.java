package com.myjavacafe.poc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myjavacafe.poc.model.TestModel;

public interface TestRepository extends JpaRepository<TestModel, Long> {
  List<TestModel> findByPublished(boolean published);

  List<TestModel> findByTitleContaining(String title);
}
