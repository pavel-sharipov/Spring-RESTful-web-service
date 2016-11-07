package com.sharipov.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharipov.model.TObject;
import com.sharipov.repository.TObjectRepository;

@Service
public class TObjectServiceBean implements TObjectService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TObjectServiceBean.class);

	private TObjectRepository tObjectRepository;

	@Autowired
	public void setObjectsRepository(TObjectRepository tObjectRepository) {
		this.tObjectRepository = tObjectRepository;
	}

	@Override
	public boolean contains(Integer id) {
		return tObjectRepository.exists(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TObject> findAll() {
		List<TObject> list = tObjectRepository.findAll();
		LOGGER.debug("All TObject retrieved. TObjects: " + list);
		return list;

	}

	@Override
	public void save(TObject tObject, Integer id) {
		if (id != 0) {
			tObject.setId(id);
		}
		tObjectRepository.saveAndFlush(tObject);
		LOGGER.debug(String.format("TObject with id: %d created/updated. TObject: %s ", id, tObject));
	}

	@Override
	public void delete(Integer id) {
		tObjectRepository.delete(id);
		LOGGER.debug(String.format("TObject with id: %d deleted", id));
	}

	@Override
	@Transactional(readOnly = true)
	public TObject findOne(Integer id) {
		TObject tObject = tObjectRepository.findOne(id);
		LOGGER.debug(String.format("TObject with id: %d retrieved. TObject: %s", id, tObject));
		return tObject;
	}

}
