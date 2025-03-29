package com.solinvictus.SpringWebFluxDemoNew.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class MonoController {
	
	@GetMapping("/monoInteger")
	public Mono<Integer> monoIntegerController(){
		Mono<Integer> monoInt = Mono.just(1);
		return monoInt;	
	}
	
	@GetMapping("/monoInteger2")
	public Mono<Integer> monoIntegerController2(){
		Mono<Integer> monoInt = Mono.just(1);
		monoInt.log().subscribe();	
		return monoInt;
	}
	
	@GetMapping("/monoString")
	public void monoStringController(){
		Mono.just("aayush").log().map(String::toUpperCase).subscribe(System.out::println);
	}
	
	
	
	
}
