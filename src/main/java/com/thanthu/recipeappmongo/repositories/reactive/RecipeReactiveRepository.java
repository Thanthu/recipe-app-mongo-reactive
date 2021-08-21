package com.thanthu.recipeappmongo.repositories.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.thanthu.recipeappmongo.domain.Recipe;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {

}
