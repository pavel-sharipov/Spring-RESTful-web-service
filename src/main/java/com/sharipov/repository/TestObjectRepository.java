package com.sharipov.repository;

import java.util.List;

import javax.persistence.Table;

import com.sharipov.model.TestObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Table(name = "objects")
public interface TestObjectRepository extends JpaRepository<TestObject, Integer> {
	List<TestObject> findByTitle(String title);
}
