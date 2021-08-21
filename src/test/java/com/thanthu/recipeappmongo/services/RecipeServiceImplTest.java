package com.thanthu.recipeappmongo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thanthu.recipeappmongo.domain.Recipe;
import com.thanthu.recipeappmongo.repositories.RecipeRepository;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {
	
	@Mock
	RecipeRepository recipeRepository;
	
	@InjectMocks
	RecipeServiceImpl recipeService;
	
	final String RECIPE_ID = "1";
	Recipe recipe;

	@BeforeEach
	void setUp() throws Exception {
		recipe = new Recipe();
		recipe.setId(RECIPE_ID);
	}

	@Test
	void testGetRecipes() {
		Set<Recipe> expectedRecipes = new HashSet<Recipe>();
		expectedRecipes.add(recipe);
		expectedRecipes.add(new Recipe());
		
		when(recipeRepository.findAll()).thenReturn(expectedRecipes);
		
		Set<Recipe> recipes = recipeService.getRecipes();
		
		assertNotNull(recipes);
		assertEquals(2, recipes.size());
		verify(recipeRepository, times(1)).findAll();
		verify(recipeRepository, never()).findById(any());
	}

	@Test
	void testFindById() {
		when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.of(recipe));
		
		assertNotNull(recipeService.findById(RECIPE_ID));
		verify(recipeRepository, times(1)).findById(RECIPE_ID);
		verify(recipeRepository, never()).findAll();
	}
	
	@Test
	void testDeleteById() {
		recipeService.deleteById(RECIPE_ID);
		verify(recipeRepository, times(1)).deleteById(RECIPE_ID);
	}

}
