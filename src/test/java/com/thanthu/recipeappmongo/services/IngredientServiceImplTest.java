package com.thanthu.recipeappmongo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thanthu.recipeappmongo.commands.IngredientCommand;
import com.thanthu.recipeappmongo.converters.IngredientCommandToIngredient;
import com.thanthu.recipeappmongo.converters.IngredientToIngredientCommand;
import com.thanthu.recipeappmongo.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.thanthu.recipeappmongo.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.thanthu.recipeappmongo.domain.Ingredient;
import com.thanthu.recipeappmongo.domain.Recipe;
import com.thanthu.recipeappmongo.repositories.RecipeRepository;
import com.thanthu.recipeappmongo.repositories.UnitOfMeasureRepository;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;

	@Mock
	RecipeRepository recipeRepository;

	@Mock
	UnitOfMeasureRepository unitOfMeasureRepository;

	IngredientServiceImpl ingredientService;

	final String RECIPE_ID = "1";
	final String INGREDIENT_ID = "3";

	// init converters
	public IngredientServiceImplTest() {
		this.ingredientToIngredientCommand = new IngredientToIngredientCommand(
				new UnitOfMeasureToUnitOfMeasureCommand());
		this.ingredientCommandToIngredient = new IngredientCommandToIngredient(
				new UnitOfMeasureCommandToUnitOfMeasure());
	}

	@BeforeEach
	public void setUp() {
		ingredientService = new IngredientServiceImpl(recipeRepository, ingredientToIngredientCommand,
				unitOfMeasureRepository, ingredientCommandToIngredient);
	}

	@Test
	public void findByRecipeIdAndIngredientIdHappyPath() throws Exception {
		// given
		Recipe recipe = new Recipe();
		recipe.setId(RECIPE_ID);

		Ingredient ingredient1 = new Ingredient();
		ingredient1.setId("1");

		Ingredient ingredient2 = new Ingredient();
		ingredient2.setId("1");

		Ingredient ingredient3 = new Ingredient();
		ingredient3.setId(INGREDIENT_ID);

		recipe.addIngredient(ingredient1);
		recipe.addIngredient(ingredient2);
		recipe.addIngredient(ingredient3);
		Optional<Recipe> recipeOptional = Optional.of(recipe);

		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

		// then
		IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(RECIPE_ID, INGREDIENT_ID);

		// when
		assertEquals(INGREDIENT_ID, ingredientCommand.getId());
		assertEquals(RECIPE_ID, ingredientCommand.getRecipeId());
		verify(recipeRepository, times(1)).findById(anyString());
	}

	@Test
	public void testSaveRecipeCommand() throws Exception {
		// given
		IngredientCommand command = new IngredientCommand();
		command.setId("3");
		command.setRecipeId("2");

		Optional<Recipe> recipeOptional = Optional.of(new Recipe());

		Recipe savedRecipe = new Recipe();
		savedRecipe.addIngredient(new Ingredient());
		savedRecipe.getIngredients().iterator().next().setId("3");

		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
		when(recipeRepository.save(any())).thenReturn(savedRecipe);

		// when
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

		// then
		assertEquals("3", savedCommand.getId());
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, times(1)).save(any(Recipe.class));

	}

	@Test
	public void testDeleteById() throws Exception {
		// given
		Recipe recipe = new Recipe();
		Ingredient ingredient = new Ingredient();
		ingredient.setId("3");
		recipe.addIngredient(ingredient);
		//ingredient.setRecipe(recipe);
		Optional<Recipe> recipeOptional = Optional.of(recipe);

		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

		// when
		ingredientService.deleteById("1", "3");

		// then
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, times(1)).save(any(Recipe.class));
	}

}
