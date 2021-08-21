package com.thanthu.recipeappmongo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.thanthu.recipeappmongo.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

}
