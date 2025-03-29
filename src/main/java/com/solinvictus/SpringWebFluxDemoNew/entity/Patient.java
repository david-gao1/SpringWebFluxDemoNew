package com.solinvictus.SpringWebFluxDemoNew.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Patient {
	
	private int id;
	private String name;
	private int age;
}
