package com.thanthu.recipeappmongo.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.thanthu.recipeappmongo.commands.RecipeCommand;
import com.thanthu.recipeappmongo.converters.RecipeCommandToRecipe;
import com.thanthu.recipeappmongo.converters.RecipeToRecipeCommand;
import com.thanthu.recipeappmongo.domain.Recipe;
import com.thanthu.recipeappmongo.exceptions.NotFoundException;
import com.thanthu.recipeappmongo.repositories.RecipeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

	private final RecipeRepository recipeRepository;
	private final RecipeCommandToRecipe recipeCommandToRecipe;
	private final RecipeToRecipeCommand recipeToRecipeCommand;

	public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeToRecipeCommand recipeToRecipeCommand,
			RecipeCommandToRecipe recipeCommandToRecipe) {
		this.recipeRepository = recipeRepository;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}

	@Override
	public Set<Recipe> getRecipes() {
		log.debug("I'm in getRecipes() in RecipeServiceImpl class");

		Set<Recipe> recipes = new HashSet<Recipe>();
		recipeRepository.findAll().forEach(recipes::add);
		return recipes;
	}

	@Override
	public Recipe findById(String id) {
		return recipeRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found For ID Value: " + id));
	}

	@Override
	public RecipeCommand saveRecipeCommand(RecipeCommand command) {
		Recipe detachedRecipe = recipeCommandToRecipe.convert(command);

		Recipe savedRecipe = recipeRepository.save(detachedRecipe);
		log.debug("Saved RecipeId:" + savedRecipe.getId());
		return recipeToRecipeCommand.convert(savedRecipe);
	}

	@Override
	public RecipeCommand findCommandById(String id) {
		RecipeCommand recipeCommand = recipeToRecipeCommand.convert(findById(id));

        //enhance command object with id value
        if(recipeCommand.getIngredients() != null && recipeCommand.getIngredients().size() > 0){
            recipeCommand.getIngredients().forEach(rc -> {
                rc.setRecipeId(recipeCommand.getId());
            });
        }

        return recipeCommand;
	}

	@Override
	public void deleteById(String id) {
		recipeRepository.deleteById(id);
	}

}
