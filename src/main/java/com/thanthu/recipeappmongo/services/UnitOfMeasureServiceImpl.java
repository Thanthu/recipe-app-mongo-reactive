package com.thanthu.recipeappmongo.services;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.thanthu.recipeappmongo.commands.UnitOfMeasureCommand;
import com.thanthu.recipeappmongo.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.thanthu.recipeappmongo.repositories.UnitOfMeasureRepository;
import com.thanthu.recipeappmongo.repositories.reactive.UnitOfMeasureReactiveRepository;

import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

	private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
	private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

	public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
			UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
		this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
	}

	@Override
	public Flux<UnitOfMeasureCommand> listAllUoms() {
		return unitOfMeasureReactiveRepository.findAll().map(unitOfMeasureToUnitOfMeasureCommand::convert);

//		return StreamSupport.stream(unitOfMeasureRepository.findAll().spliterator(), false)
//				.map(unitOfMeasureToUnitOfMeasureCommand::convert).collect(Collectors.toSet());
	}

}
