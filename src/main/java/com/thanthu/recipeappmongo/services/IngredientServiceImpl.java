package com.thanthu.recipeappmongo.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thanthu.recipeappmongo.commands.IngredientCommand;
import com.thanthu.recipeappmongo.converters.IngredientCommandToIngredient;
import com.thanthu.recipeappmongo.converters.IngredientToIngredientCommand;
import com.thanthu.recipeappmongo.domain.Ingredient;
import com.thanthu.recipeappmongo.domain.Recipe;
import com.thanthu.recipeappmongo.repositories.RecipeRepository;
import com.thanthu.recipeappmongo.repositories.UnitOfMeasureRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private final RecipeRepository recipeRepository;

	private final IngredientToIngredientCommand ingredientToIngredientCommand;

	private final UnitOfMeasureRepository unitOfMeasureRepository;

	private final IngredientCommandToIngredient ingredientCommandToIngredient;

	public IngredientServiceImpl(RecipeRepository recipeRepository,
			IngredientToIngredientCommand ingredientToIngredientCommand,
			UnitOfMeasureRepository unitOfMeasureRepository,
			IngredientCommandToIngredient ingredientCommandToIngredient) {
		this.recipeRepository = recipeRepository;
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.unitOfMeasureRepository = unitOfMeasureRepository;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new RuntimeException("Recipe not found for ID: " + recipeId));

		IngredientCommand ingredientCommand = recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(ingredientId))
				.map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst()
				.orElseThrow(() -> new RuntimeException("Ingredient not found for ID: " + ingredientId));

		//enhance command object with recipe id
        ingredientCommand.setRecipeId(recipe.getId());
		
		return ingredientCommand;
	}

	@Override
	@Transactional
	public IngredientCommand saveIngredientCommand(IngredientCommand command) {
		Recipe recipe = recipeRepository.findById(command.getRecipeId())
				.orElseThrow(() -> new RuntimeException("Recipe not found for ID: " + command.getRecipeId()));

		Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(command.getId())).findFirst();

		if (ingredientOptional.isPresent()) {
			Ingredient ingredientFound = ingredientOptional.get();
			ingredientFound.setDescription(command.getDescription());
			ingredientFound.setAmount(command.getAmount());
			ingredientFound.setUnitOfMeasure(unitOfMeasureRepository.findById(command.getUnitOfMeasure().getId())
					.orElseThrow(() -> new RuntimeException(
							"Unit Of Measure not found for ID: " + command.getUnitOfMeasure().getId())));
		} else {
			// add new Ingredient
			recipe.addIngredient(ingredientCommandToIngredient.convert(command));
		}

		Recipe savedRecipe = recipeRepository.save(recipe);

		Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
				.filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId())).findFirst();

		// check by description
		if (!savedIngredientOptional.isPresent()) {
			// not totally safe... But best guess
			savedIngredientOptional = savedRecipe.getIngredients().stream()
					.filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
					.filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
					.filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId()
							.equals(command.getUnitOfMeasure().getId()))
					.findFirst();
		}

		IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        ingredientCommandSaved.setRecipeId(recipe.getId());
		
		return ingredientCommandSaved;

	}

	@Override
	public void deleteById(String recipeId, String id) {
		log.debug("Deleting ingredient: " + recipeId + ":" + id);

		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));

		Ingredient ingredientToDelete = recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(id)).findFirst()
				.orElseThrow(() -> new RuntimeException("Ingredient not found with ID: " + id));

		if (ingredientToDelete != null) {
			//ingredientToDelete.setRecipe(null);
			recipe.getIngredients().remove(ingredientToDelete);
			recipeRepository.save(recipe);
		}
	}

}
