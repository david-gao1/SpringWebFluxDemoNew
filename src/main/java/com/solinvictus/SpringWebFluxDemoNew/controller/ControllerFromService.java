package com.solinvictus.SpringWebFluxDemoNew.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solinvictus.SpringWebFluxDemoNew.entity.Patient;
import com.solinvictus.SpringWebFluxDemoNew.service.FluxDataService;
import com.solinvictus.SpringWebFluxDemoNew.service.MonoDataService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ControllerFromService {

	@Autowired
	MonoDataService monoDataService;
	
	@Autowired
	FluxDataService fluxDataService;
	
	// mono technique
	@GetMapping("/monoIntegerFromDataService")
	public void monoIntegerFromDataService(){	
		monoDataService.monoIntegerDataSource().subscribe(System.out::println);
	}
	
	@GetMapping("/monoStringFromDataService")
	public void monoStringFromDataService(){
		monoDataService.monoStringDataSource().map(String::toUpperCase).subscribe(System.out::println);
	}
	
	@GetMapping("/monoPatientFromDataService")
	public void monoPatientFromDataService(){
		monoDataService.monoPatientDataSource().filter(p-> p.getAge()>10).subscribe(System.out::println);
	}
	
	
	// flux technique
	
	@GetMapping("/fluxIntegerFromDataService")
	public void fluxIntegerFromDataService(){	
		fluxDataService.fluxIntegerDataSource().subscribe(System.out::println);
	}
	
	@GetMapping("/fluxStringFromDataService")
	public void fluxStringFromDataService(){
		fluxDataService.fluxStringDataSource().map(name -> name.hashCode()).subscribe(System.out::println);
	}
	
	@GetMapping("/fluxPatientFromDataService")
	public void fluxPatientFromDataService(){
		fluxDataService.fluxPatientDataSource().filter(p-> p.getAge()>10).subscribe(System.out::println);
	}
	
	
	// crud operations on mono.
	
	@PostMapping("/monoPatientFromDataService/{id}")
	public void monoPatientFromDataService(@PathVariable int id){
		monoDataService.monoPatientDataSource().map(p -> { 
			p.setId(10);
			return p;
		}).subscribe(System.out::println);
	}
	
	//crud operations on flux.
	@PostMapping("/fluxPatientFromDataService/{id}")
	public void fluxPatientFromDataService(@PathVariable int id){
		fluxDataService.fluxPatientDataSource()
			.concatWith(Flux.just(new Patient(id, "Patient"+id , 18)))
			.subscribe(System.out::println);
	}
}
