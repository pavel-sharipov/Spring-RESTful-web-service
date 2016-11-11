package com.sharipov.repository;

import java.util.List;

import com.sharipov.model.TestObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest

public class TestObjectRepositoryTest {

	@Autowired
	private TestObjectRepository testObjectRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void findByTitle_CorrectString_Success() {
		entityManager.persist(new TestObject("test title", 20L));
		List<TestObject> list = this.testObjectRepository.findByTitle("test title");
		list.stream().forEach(x -> System.out.println(x.getTitle()));
		org.junit.Assert.assertEquals(list.size(), 1);

	}
}
