package com.thanthu.recipeappmongo.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thanthu.recipeappmongo.commands.RecipeCommand;
import com.thanthu.recipeappmongo.services.RecipeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/recipe")
public class RecipeController {

	private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
	private final RecipeService recipeService;

	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@GetMapping("/{id}/show")
	public String getRecipe(@PathVariable String id, Model model) {
		log.debug("GET /recipe/" + id + "/show");
		model.addAttribute("recipe", recipeService.findById(id));
		return "recipe/show";
	}

	@GetMapping("/new")
	public String newRecipe(Model model) {
		log.debug("GET /recipe/new");
		model.addAttribute("recipe", new RecipeCommand());
		return "recipe/recipeform";
	}

	@PostMapping("")
	public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {
		log.debug("POST /recipe");
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> {
				log.debug(objectError.toString());
			});
			return RECIPE_RECIPEFORM_URL;
		}
		RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
		return "redirect:/recipe/" + savedCommand.getId() + "/show";
	}

	@GetMapping("/{id}/update")
	public String updateRecipe(@PathVariable String id, Model model) {
		log.debug("GET /recipe/" + id + "/update");
		model.addAttribute("recipe", recipeService.findCommandById(id));
		return RECIPE_RECIPEFORM_URL;
	}

	@GetMapping("/{id}/delete")
	public String deleteRecipe(@PathVariable String id) {
		log.debug("GET /recipe/" + id + "/delete");
		recipeService.deleteById(id);
		return "redirect:/";
	}

}
