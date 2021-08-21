package com.thanthu.recipeappmongo.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.thanthu.recipeappmongo.commands.RecipeCommand;
import com.thanthu.recipeappmongo.domain.Recipe;
import com.thanthu.recipeappmongo.exceptions.NotFoundException;
import com.thanthu.recipeappmongo.services.RecipeService;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

	@Mock
	RecipeService recipeService;

	@InjectMocks
	RecipeController recipeController;

	MockMvc mockMvc;
	final String RECIPE_ID = "1";
	Recipe recipe;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
				.setControllerAdvice(new ControllerExceptionHandler()).build();
		recipe = new Recipe();
		recipe.setId(RECIPE_ID);
	}

	@Test
	void testGetRecipe() throws Exception {

		when(recipeService.findById(RECIPE_ID)).thenReturn(recipe);

		mockMvc.perform(get("/recipe/" + RECIPE_ID + "/show")).andExpect(status().isOk())
				.andExpect(view().name("recipe/show"));
	}

	@Test
	public void testGetRecipeNotFound() throws Exception {
		when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);
		mockMvc.perform(get("/recipe/1/show")).andExpect(status().isNotFound()).andExpect(view().name("404error"));
	}

	@Test
	@Disabled
	public void testGetRecipeNumberFormatException() throws Exception {
		mockMvc.perform(get("/recipe/asdf/show")).andExpect(status().isBadRequest()).andExpect(view().name("400error"));
	}

	@Test
	public void testGetNewRecipeForm() throws Exception {
		mockMvc.perform(get("/recipe/new")).andExpect(status().isOk()).andExpect(view().name("recipe/recipeform"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testPostNewRecipeForm() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId("2");
		when(recipeService.saveRecipeCommand(any())).thenReturn(command);
		mockMvc.perform(post("/recipe").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", "")
				.param("description", "some string").param("directions", "some directions"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipe/2/show"));
	}

	@Test
	public void testPostNewRecipeFormValidationFail() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId("2");

		mockMvc.perform(post("/recipe").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", "")

		).andExpect(status().isOk()).andExpect(model().attributeExists("recipe"))
				.andExpect(view().name("recipe/recipeform"));
	}

	@Test
	public void testGetUpdateView() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId(RECIPE_ID);

		when(recipeService.findCommandById(anyString())).thenReturn(command);

		mockMvc.perform(get("/recipe/" + RECIPE_ID + "/update")).andExpect(status().isOk())
				.andExpect(view().name("recipe/recipeform")).andExpect(model().attributeExists("recipe"));
	}

	@Test
	void testDelete() throws Exception {
		mockMvc.perform(get("/recipe/" + RECIPE_ID + "/delete")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));

		verify(recipeService, times(1)).deleteById(RECIPE_ID);
	}

}
