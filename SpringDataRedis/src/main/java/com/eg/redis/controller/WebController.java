package com.eg.redis.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eg.redis.model.Customer;
import com.eg.redis.repo.CustomerRepository;

@RestController
public class WebController {

	@Autowired
	private CustomerRepository customerRepository;

	@RequestMapping("/save")
	public String save() {
		// save a single Customer
		customerRepository.save(new Customer(1, "Jack", "Smith"));
		customerRepository.save(new Customer(2, "cust222", "cust222last"));
		customerRepository.save(new Customer(3, "cust333", "cust33last"));
		customerRepository.save(new Customer(4, "cust444", "cust444last"));
		customerRepository.save(new Customer(5, "cust555", "cust555last"));

		return "Done";
	}

	@RequestMapping("/findall")
	public String findAll() {
		String result = "";
		Map<Long, Customer> customers = customerRepository.findAll();

		for (Customer customer : customers.values()) {
			result += customer.toString() + "<br>";
		}

		return result;
	}

	@RequestMapping("/find")
	public String findById(@RequestParam("id") Long id) {
		String result = "";
		result = customerRepository.find(id).toString();
		return result;
	}

	@RequestMapping(value = "/uppercase")
	public String postCustomer(@RequestParam("id") Long id) {
		Customer customer = customerRepository.find(id);
		customer.setFirstName(customer.getFirstName().toUpperCase());
		customer.setLastName(customer.getLastName().toUpperCase());

		customerRepository.update(customer);

		return "Done";
	}

	@RequestMapping("/delete")
	public String deleteById(@RequestParam("id") Long id) {
		customerRepository.delete(id);

		return "Done";
	}
}
