package org.akhl.netty.patterns.encoder_decoder;

import java.time.LocalDate;

public class Person {
	
	private String name;
	private LocalDate dateOfBirth;
	private Gender gender;
	private double weight;
	
	public Person(String name, LocalDate dateOfBirth, Gender gender, double weight) {
		super();
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", dateOfBirth=" + dateOfBirth
				+ ", gender=" + gender + ", weight=" + weight + "]";
	}
	
	

}
