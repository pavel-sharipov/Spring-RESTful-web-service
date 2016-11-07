package com.sharipov.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

import com.sharipov.model.TObject;
import com.sharipov.service.TObjectService;

@Service
@RestController
@RequestMapping("/")
public class TObjectController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TObjectController.class);

	private TObjectService tObjectService;

	@Autowired
	public void setTObjectService(TObjectService tObjectService) {
		this.tObjectService = tObjectService;
	}

	@RequestMapping(path = { "object", "object/{id}" }, method = { RequestMethod.POST, RequestMethod.PUT })
	private ResponseEntity<TObject> postOrPutBook(@PathVariable(required = false) Integer id,
			@RequestBody TObject tObject) {
		if (Objects.isNull(tObject.getTitle()) || (tObject.getValue() == 0)) {

			throw new TObjectBadRequestException();
		}
		if (Objects.nonNull(id) && !tObjectService.contains(id)) {

			throw new TObjectNotFoundException();
		}
		tObjectService.save(tObject, Optional.ofNullable(id).orElse(new Integer(0)));
		return new ResponseEntity<>(tObject, HttpStatus.OK);
	}

	@RequestMapping(path = "objects", method = RequestMethod.GET)
	public List<TObject> getAllTObjects() {
		LOGGER.info("Request to GET all TObjects...");
		return tObjectService.findAll();
	}

	@RequestMapping(path = "object/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteTObject(@PathVariable("id") Integer id) {
		LOGGER.info(String.format("Request to DELETE TObject with id: %d ...", id));
		if (Objects.nonNull(id) && !tObjectService.contains(id)) {
			throw new TObjectNotFoundException();
		}
		tObjectService.delete(id);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	@RequestMapping(path = "object/{id}", method = RequestMethod.GET)
	public ResponseEntity<TObject> getTObject(@PathVariable("id") Integer id) {
		LOGGER.info(String.format("Request to GET TObject with id: %d ...", id));
		if (Objects.nonNull(id) && !tObjectService.contains(id)) {
			throw new TObjectNotFoundException();
		}
		TObject tObject = tObjectService.findOne(id);
		return new ResponseEntity<>(tObject, HttpStatus.OK);
	}

}
