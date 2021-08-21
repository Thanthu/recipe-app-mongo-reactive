package com.thanthu.recipeappmongo.services;

import com.thanthu.recipeappmongo.commands.IngredientCommand;

public interface IngredientService {

	IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
	
	IngredientCommand saveIngredientCommand(IngredientCommand command);

	void deleteById(String recipeId, String id);

}
