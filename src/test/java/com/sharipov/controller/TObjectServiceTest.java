package com.sharipov.controller;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

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
	public void contains_NonCorrectIdNumber_Success() {
		testEntityManager.persist(new TObject("test title 1", 1L));
		testEntityManager.persist(new TObject("test title 2", 2L));
		boolean isContains = tObjectService.contains(3);
		assertEquals(false, isContains);
	}
	@Test
	public void contains_CorrectIdNumber_Success() {
		TObject object = new TObject("test title 1", 1L);
		testEntityManager.persist(object);
		boolean isContains = tObjectService.contains((int)testEntityManager.getId(object));
		assertEquals(true, isContains);
	}
	@Test
	public void finadAll_Success() {
		testEntityManager.persist(new TObject("test title 1", 1L));
		testEntityManager.persist(new TObject("test title 2", 2L));
		List<TObject> list = tObjectService.findAll();
		List<TObject> repositoryList = tObjectRepository.findAll();
		assertArrayEquals(Arrays.array(repositoryList),Arrays.array(list));
	}
	@Test
	public void finadAll_SuccessEmptyList() {
		List<TObject> list = tObjectService.findAll();
		assertEquals(true, list.isEmpty());
	}
	
	@Test
	public void save_IdIsEqualsZero_Success() {
		TObject object = new TObject(0, "test title save", 1L);
		tObjectService.save(object, 0);
		int lastIndex = (int) testEntityManager.getId(object);
		TObject repositoryObject = testEntityManager.find(TObject.class, lastIndex);
		assertEquals("test title save", repositoryObject.getTitle());
		assertNotEquals(0, repositoryObject.getId());
	}
	@Test
	public void save_IdIsNotEqualsZero_Success() {
		testEntityManager.persist(new TObject("test title 1", 1L));
		testEntityManager.persist(new TObject("test title 2", 2L));
		TObject object = new TObject(2, "test title update", 1L);
		tObjectService.save(object, 2);
		int lastIndex = (int) testEntityManager.getId(object);
		TObject repositoryObject = testEntityManager.find(TObject.class, lastIndex);
		assertEquals("test title update", repositoryObject.getTitle());
		assertNotEquals(0, repositoryObject.getId());
	}
	
	
	
	

	

}
