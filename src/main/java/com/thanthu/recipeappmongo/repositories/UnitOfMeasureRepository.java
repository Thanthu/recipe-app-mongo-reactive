package com.thanthu.recipeappmongo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.thanthu.recipeappmongo.domain.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

	Optional<UnitOfMeasure> findByUnit(String unit);

}
