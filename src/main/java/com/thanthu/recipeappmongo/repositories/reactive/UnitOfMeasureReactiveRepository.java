package com.thanthu.recipeappmongo.repositories.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.thanthu.recipeappmongo.domain.UnitOfMeasure;

import reactor.core.publisher.Mono;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {

	Mono<UnitOfMeasure> findByUnit(String each);

}
