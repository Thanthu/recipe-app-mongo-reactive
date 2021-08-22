package com.thanthu.recipeappmongo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thanthu.recipeappmongo.commands.UnitOfMeasureCommand;
import com.thanthu.recipeappmongo.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.thanthu.recipeappmongo.domain.UnitOfMeasure;
import com.thanthu.recipeappmongo.repositories.reactive.UnitOfMeasureReactiveRepository;

import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
public class UnitOfMeasureServiceImplTest {

	UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
	UnitOfMeasureService service;

	@Mock
	UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	@BeforeEach
	public void setUp() throws Exception {
		service = new UnitOfMeasureServiceImpl(unitOfMeasureReactiveRepository, unitOfMeasureToUnitOfMeasureCommand);
	}

	@Test
	public void listAllUoms() throws Exception {
		// given
		UnitOfMeasure uom1 = new UnitOfMeasure();
		uom1.setId("1");

		UnitOfMeasure uom2 = new UnitOfMeasure();
		uom2.setId("2");

		when(unitOfMeasureReactiveRepository.findAll()).thenReturn(Flux.just(uom1, uom2));

		// when
		List<UnitOfMeasureCommand> commands = service.listAllUoms().collectList().block();

		// then
		assertEquals(2, commands.size());
		verify(unitOfMeasureReactiveRepository, times(1)).findAll();
	}

}
