package com.sharipov.controller;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.sharipov.model.TObject;
import com.sharipov.repository.TObjectRepository;
import com.sharipov.service.TObjectService;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TObjectServiceTest {

	@Autowired
	private TObjectService tObjectService;

	@Autowired
	private TObjectRepository tObjectRepository;
	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void contains_NotValidId_Success() {
		testEntityManager.persist(new TObject("test title 1", 1));
		testEntityManager.persist(new TObject("test title 2", 2));
		boolean isContains = tObjectService.contains(3);
		assertEquals(false, isContains);
	}

	@Test
	public void contains_ValidId_Success() {
		TObject object = new TObject("test title 1", 1);
		testEntityManager.persist(object);
		boolean isContains = tObjectService.contains((int) testEntityManager.getId(object));
		assertEquals(true, isContains);
	}

	@Test
	public void finadAll_Success() {
		testEntityManager.persist(new TObject("test title 1", 1));
		testEntityManager.persist(new TObject("test title 2", 2));
		List<TObject> list = tObjectService.findAll();
		List<TObject> repositoryList = tObjectRepository.findAll();
		assertArrayEquals(Arrays.array(repositoryList), Arrays.array(list));
	}

	@Test
	public void finadAll_SuccessEmptyList() {
		List<TObject> list = tObjectService.findAll();
		assertEquals(true, list.isEmpty());
	}

	@Test
	public void save_IdIsEqualsZero_Success() {
		TObject object = new TObject(0, "test title save", 1);
		tObjectService.save(object, 0);
		int lastIndex = (int) testEntityManager.getId(object);
		TObject repositoryObject = testEntityManager.find(TObject.class, lastIndex);
		assertEquals("test title save", repositoryObject.getTitle());
		assertNotEquals(0, repositoryObject.getId());
	}

	@Test
	public void save_IdIsNotEqualsZero_Success() {
		TObject object = new TObject("test title update", 1);
		testEntityManager.persist(object);
		int objectId = object.getId();
		tObjectService.save(object, objectId);
		int lastIndex = (int) testEntityManager.getId(object);
		TObject repositoryObject = testEntityManager.find(TObject.class, lastIndex);
		assertEquals("test title update", repositoryObject.getTitle());
		assertNotEquals(0, repositoryObject.getId());
	}

	@Test
	public void findOne_ValidId_Success() {
		TObject object = new TObject("test title findOne", 5);
		testEntityManager.persist(object);
		int objectId = object.getId();
		TObject repositoryObject = tObjectService.findOne(objectId);
		assertEquals(object, repositoryObject);
	}

	@Test
	public void findOne_NotValidId_Success() {
		TObject object = new TObject("test title findOne", 5);
		testEntityManager.persist(object);
		int objectId = 100;
		TObject repositoryObject = tObjectService.findOne(objectId);
		assertNull(repositoryObject);
		assertNotEquals(object, repositoryObject);
	}

	@Test
	public void delete_ValidId_Success() {
		TObject object1 = new TObject("test title delete1", 1);
		TObject object2 = new TObject("test title delete2", 2);
		testEntityManager.persist(object1);
		testEntityManager.persist(object2);
		int object1Id = object1.getId();
		int object2Id = object2.getId();
		TObject repositoryObject = tObjectService.findOne(object1Id);
		tObjectService.delete(object2Id);
		assertNull(tObjectService.findOne(object2Id));
		assertNotNull(repositoryObject);

	}

}
