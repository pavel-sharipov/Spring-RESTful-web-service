package com.sharipov.repository;

import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharipov.model.TObject;

@Repository
@Table(name = "objects")
public interface TObjectRepository extends JpaRepository<TObject, Integer> {

}
