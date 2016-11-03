package com.sharipov.service;

import java.util.List;

import com.sharipov.model.TObject;

public interface TObjectService {

	List<TObject> findAll();

	void save(TObject tObject, Integer id);

	boolean contains(Integer id);

	void delete(Integer id);

	TObject findOne(Integer id);

}
