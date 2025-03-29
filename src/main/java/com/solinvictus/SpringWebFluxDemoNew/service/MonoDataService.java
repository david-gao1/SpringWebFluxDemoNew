package com.solinvictus.SpringWebFluxDemoNew.service;

import org.springframework.stereotype.Service;

import com.solinvictus.SpringWebFluxDemoNew.entity.Patient;

import reactor.core.publisher.Mono;

@Service
public class MonoDataService {
	public Mono<Integer> monoIntegerDataSource(){
		return Mono.just(100);
	}
	
	public Mono<String> monoStringDataSource(){
		return Mono.just("AdaM");
	}
	
	public Mono<Patient> monoPatientDataSource(){
		return Mono.just(new Patient(5, "aayush", 25));
	}
}
