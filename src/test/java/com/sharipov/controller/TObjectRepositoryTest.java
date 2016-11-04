package com.sharipov.controller;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.sharipov.model.TObject;
import com.sharipov.repository.TObjectRepository;

@RunWith(SpringRunner.class)
@DataJpaTest

public class TObjectRepositoryTest {

	@Autowired
	private TObjectRepository tObjectRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void findByTitle_CorrectString_Success() {
		entityManager.persist(new TObject("test title", 20L));
		List<TObject> list = this.tObjectRepository.findByTitle("test title");
		list.stream().forEach(x -> System.out.println(x.getTitle()));
		org.junit.Assert.assertEquals(list.size(), 1);
		
	}
}
