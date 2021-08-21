package com.thanthu.recipeappmongo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.thanthu.recipeappmongo.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, String> {

	Optional<Category> findByDescription(String description);

}
