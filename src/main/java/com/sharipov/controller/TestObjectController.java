package com.sharipov.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.sharipov.model.TestObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sharipov.service.TestObjectService;

@Service
@RestController
@RequestMapping("/")
public class TestObjectController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestObjectController.class);

	private TestObjectService testObjectService;

	@Autowired
	public void setTObjectService(TestObjectService testObjectService) {
		this.testObjectService = testObjectService;
	}

	@RequestMapping(path = { "object", "object/{id}" }, method = { RequestMethod.POST, RequestMethod.PUT })
	private ResponseEntity<TestObject> postOrPutBook(@PathVariable(required = false) Integer id,
			@RequestBody TestObject testObject) {
		if (Objects.isNull(testObject.getTitle()) || (testObject.getValue() == 0)) {

			throw new TestObjectBadRequestException();
		}
		if (Objects.nonNull(id) && !testObjectService.contains(id)) {

			throw new TestObjectNotFoundException();
		}
		testObjectService.save(testObject, Optional.ofNullable(id).orElse(new Integer(0)));
		return new ResponseEntity<>(testObject, HttpStatus.OK);
	}

	@RequestMapping(path = "objects", method = RequestMethod.GET)
	public List<TestObject> getAllTObjects() {
		LOGGER.info("Request to GET all TObjects...");
		return testObjectService.findAll();
	}

	@RequestMapping(path = "object/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteTObject(@PathVariable("id") Integer id) {
		LOGGER.info(String.format("Request to DELETE TestObject with id: %d ...", id));
		if (Objects.nonNull(id) && !testObjectService.contains(id)) {
			throw new TestObjectNotFoundException();
		}
		testObjectService.delete(id);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	@RequestMapping(path = "object/{id}", method = RequestMethod.GET)
	public ResponseEntity<TestObject> getTObject(@PathVariable("id") Integer id) {
		LOGGER.info(String.format("Request to GET TestObject with id: %d ...", id));
		if (Objects.nonNull(id) && !testObjectService.contains(id)) {
			throw new TestObjectNotFoundException();
		}
		TestObject testObject = testObjectService.findOne(id);
		return new ResponseEntity<>(testObject, HttpStatus.OK);
	}

}
