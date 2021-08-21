package com.thanthu.recipeappmongo.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {

	Category category;

	@BeforeEach
	public void setUp() {
		category = new Category();
	}

	@Test
	public void getId() {
		String id = "4";
		category.setId(id);
		assertEquals(id, category.getId());
	}
	
	@Test
	public void getDescription() {
		String description = "Test description";
		category.setDescription(description);
		assertEquals(description, category.getDescription());
	}
	
}
