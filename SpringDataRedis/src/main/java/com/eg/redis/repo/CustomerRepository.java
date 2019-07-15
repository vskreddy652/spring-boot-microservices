package com.eg.redis.repo;

import java.util.Map;

import com.eg.redis.model.Customer;

public interface CustomerRepository {

	void save(Customer customer);
	Customer find(Long id);
	Map<Long, Customer> findAll();
	void update(Customer customer);
	void delete(Long id);
}
