package com.eg.cassandra.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.eg.cassandra.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String> {
	
	@Query(value="SELECT * FROM customer WHERE firstanme=?0")
	public List<Customer> findByFirstname(String firstname);

	@Query("SELECT * FROM customer WHERE age > ?0 ALLOW FILTERING")
	public List<Customer> findCustomerHasAgeGreaterThan(int age);
}