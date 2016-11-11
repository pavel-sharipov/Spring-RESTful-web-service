package com.sharipov.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import com.sharipov.model.TestObject;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.sharipov.repository.TestObjectRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestObjectServiceTest {

	@Autowired
	private TestObjectService testObjectService;

	@Autowired
	private TestObjectRepository testObjectRepository;
	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void contains_NotValidId_Success() {
		testEntityManager.persist(new TestObject("test title 1", 1));
		testEntityManager.persist(new TestObject("test title 2", 2));
		boolean isContains = testObjectService.contains(3);
		assertEquals(false, isContains);
	}

	@Test
	public void contains_ValidId_Success() {
		TestObject object = new TestObject("test title 1", 1);
		testEntityManager.persist(object);
		boolean isContains = testObjectService.contains((int) testEntityManager.getId(object));
		assertEquals(true, isContains);
	}

	@Test
	public void finadAll_Success() {
		testEntityManager.persist(new TestObject("test title 1", 1));
		testEntityManager.persist(new TestObject("test title 2", 2));
		List<TestObject> list = testObjectService.findAll();
		List<TestObject> repositoryList = testObjectRepository.findAll();
		assertArrayEquals(Arrays.array(repositoryList), Arrays.array(list));
	}

	@Test
	public void finadAll_SuccessEmptyList() {
		List<TestObject> list = testObjectService.findAll();
		assertEquals(true, list.isEmpty());
	}

	@Test
	public void save_IdIsEqualsZero_Success() {
		TestObject object = new TestObject(0, "test title save", 1);
		testObjectService.save(object, 0);
		int lastIndex = (int) testEntityManager.getId(object);
		TestObject repositoryObject = testEntityManager.find(TestObject.class, lastIndex);
		assertEquals("test title save", repositoryObject.getTitle());
		assertNotEquals(0, repositoryObject.getId());
	}

	@Test
	public void save_IdIsNotEqualsZero_Success() {
		TestObject object = new TestObject("test title update", 1);
		testEntityManager.persist(object);
		int objectId = object.getId();
		testObjectService.save(object, objectId);
		int lastIndex = (int) testEntityManager.getId(object);
		TestObject repositoryObject = testEntityManager.find(TestObject.class, lastIndex);
		assertEquals("test title update", repositoryObject.getTitle());
		assertNotEquals(0, repositoryObject.getId());
	}

	@Test
	public void findOne_ValidId_Success() {
		TestObject object = new TestObject("test title findOne", 5);
		testEntityManager.persist(object);
		int objectId = object.getId();
		TestObject repositoryObject = testObjectService.findOne(objectId);
		assertEquals(object, repositoryObject);
	}

	@Test
	public void findOne_NotValidId_Success() {
		TestObject object = new TestObject("test title findOne", 5);
		testEntityManager.persist(object);
		int objectId = 100;
		TestObject repositoryObject = testObjectService.findOne(objectId);
		assertNull(repositoryObject);
		assertNotEquals(object, repositoryObject);
	}

	@Test
	public void delete_ValidId_Success() {
		TestObject object1 = new TestObject("test title delete1", 1);
		TestObject object2 = new TestObject("test title delete2", 2);
		testEntityManager.persist(object1);
		testEntityManager.persist(object2);
		int object1Id = object1.getId();
		int object2Id = object2.getId();
		TestObject repositoryObject = testObjectService.findOne(object1Id);
		testObjectService.delete(object2Id);
		assertNull(testObjectService.findOne(object2Id));
		assertNotNull(repositoryObject);

	}

}
