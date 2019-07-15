package com.eg.cassandra.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table
public class Customer {
	
	@PrimaryKey
	private int id;
	private String firstanme;
	private String lastname;
	private int age;
	
	public Customer(){}
	
	public Customer(int id, String firstname, String lastname, int age){
		this.id = id;
		this.firstanme = firstname;
		this.lastname = lastname;
		this.age = age;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setFirstname(String firstname){
		this.firstanme = firstname;
	}
	
	public String getFirstname(){
		return this.firstanme;
	}
	
	public void setLastname(String lastname){
		this.lastname = lastname;
	}
	
	public String getLastname(){
		return this.lastname;
	}
	
	public void setAge(int age){
		this.age = age;
	}
	
	public int getAge(){
		return this.age;
	}
	
	@Override
	public String toString() {
		return String.format("Customer[id=%d, firstName='%s', lastName='%s', age=%d]", this.id,
				this.firstanme, this.lastname, this.age);
	}
}
