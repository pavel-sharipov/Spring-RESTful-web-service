package com.sharipov.service;

import java.util.List;

import com.sharipov.model.TestObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharipov.repository.TestObjectRepository;

@Service
public class TestObjectServiceBean implements TestObjectService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestObjectServiceBean.class);

	private TestObjectRepository testObjectRepository;

	@Autowired
	public void setObjectsRepository(TestObjectRepository testObjectRepository) {
		this.testObjectRepository = testObjectRepository;
	}

	@Override
	public boolean contains(Integer id) {
		return testObjectRepository.exists(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TestObject> findAll() {
		List<TestObject> list = testObjectRepository.findAll();
		LOGGER.debug("All TestObject retrieved. TObjects: " + list);
		return list;

	}

	@Override
	public void save(TestObject testObject, Integer id) {
		if (id != 0) {
			testObject.setId(id);
		}
		testObjectRepository.saveAndFlush(testObject);
		LOGGER.debug(String.format("TestObject with id: %d created/updated. TestObject: %s ", id, testObject));
	}

	@Override
	public void delete(Integer id) {
		testObjectRepository.delete(id);
		LOGGER.debug(String.format("TestObject with id: %d deleted", id));
	}

	@Override
	@Transactional(readOnly = true)
	public TestObject findOne(Integer id) {
		TestObject testObject = testObjectRepository.findOne(id);
		LOGGER.debug(String.format("TestObject with id: %d retrieved. TestObject: %s", id, testObject));
		return testObject;
	}

}
