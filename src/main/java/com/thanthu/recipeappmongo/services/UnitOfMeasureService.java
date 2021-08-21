package com.thanthu.recipeappmongo.services;

import java.util.Set;

import com.thanthu.recipeappmongo.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {
	
	Set<UnitOfMeasureCommand> listAllUoms();

}
