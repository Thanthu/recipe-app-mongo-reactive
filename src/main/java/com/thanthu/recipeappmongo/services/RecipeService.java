package com.thanthu.recipeappmongo.services;

import java.util.Set;

import com.thanthu.recipeappmongo.commands.RecipeCommand;
import com.thanthu.recipeappmongo.domain.Recipe;

public interface RecipeService {

	Set<Recipe> getRecipes();

	Recipe findById(String id);

	RecipeCommand saveRecipeCommand(RecipeCommand command);

	RecipeCommand findCommandById(String id);

	void deleteById(String id);

}
