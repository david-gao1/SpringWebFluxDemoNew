package com.solinvictus.SpringWebFluxDemoNew.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FluxController {
	
	@GetMapping("/fluxInteger")
	public Flux<Integer> monoIntegerController(){
		Flux<Integer> fluxInt = Flux.just(1, 2, 3, 4, 5);
		return fluxInt;	
	}
	
	@GetMapping("/fluxInteger2")
	public Flux<Integer> monoIntegerController2(){
		Flux<Integer> fluxInt = Flux.just(1, 2, 3, 4, 5);
		fluxInt.log().subscribe();	
		return fluxInt;
	}
	
	@GetMapping("/fluxString")
	public void fluxStringController(){
		Flux.just("aayush", "anurag", "devang").log().map(String::toUpperCase).subscribe(System.out::println);
	}
}
