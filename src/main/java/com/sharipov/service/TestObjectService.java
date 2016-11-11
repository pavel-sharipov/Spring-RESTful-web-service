package com.sharipov.service;

import java.util.List;

import com.sharipov.model.TestObject;

public interface TestObjectService {

	List<TestObject> findAll();

	void save(TestObject testObject, Integer id);

	boolean contains(Integer id);

	void delete(Integer id);

	TestObject findOne(Integer id);

}
