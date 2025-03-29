package com.solinvictus.SpringWebFluxDemoNew.service;

import org.springframework.stereotype.Service;

import com.solinvictus.SpringWebFluxDemoNew.entity.Patient;

import reactor.core.publisher.Flux;

@Service
public class FluxDataService {
	public Flux<Integer> fluxIntegerDataSource(){
		return Flux.just(100, 200, 3400, 1 , 0 , 8, 9);
	}
	
	public Flux<String> fluxStringDataSource(){
		return Flux.just("AdaM", "aaYush", "gautaM", "pAvan");
	}
	
	public Flux<Patient> fluxPatientDataSource(){
		return Flux.just(new Patient(1, "aayush", 25),
				new Patient(2, "adam", 5),
				new Patient(3, "pavan", 32),
				new Patient(4, "gautam", 80)
				);
	}
	
}
