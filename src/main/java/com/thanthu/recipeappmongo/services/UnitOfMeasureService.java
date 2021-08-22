package com.thanthu.recipeappmongo.services;

import com.thanthu.recipeappmongo.commands.UnitOfMeasureCommand;

import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {
	
	Flux<UnitOfMeasureCommand> listAllUoms();

}
