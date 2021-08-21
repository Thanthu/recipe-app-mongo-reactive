package com.thanthu.recipeappmongo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.thanthu.recipeappmongo.bootstrap.RecipeBootstrap;
import com.thanthu.recipeappmongo.domain.UnitOfMeasure;

@DataMongoTest
class UnitOfMeasureRepositoryIT {

	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	RecipeRepository recipeRepository;

	@BeforeEach
	void setUp() throws Exception {
		recipeRepository.deleteAll();
		unitOfMeasureRepository.deleteAll();
		categoryRepository.deleteAll();

		RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository,
				unitOfMeasureRepository);

		recipeBootstrap.onApplicationEvent(null);
	}

	@Test
	void testFindByUnit() {
		String expectedUnit = "Teaspoon";
		Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findByUnit(expectedUnit);
		assertEquals(expectedUnit, unitOfMeasure.get().getUnit());
	}

	@Test
	public void findByUnitCup() throws Exception {

		Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByUnit("Cup");

		assertEquals("Cup", uomOptional.get().getUnit());
	}

}
