package com.cj.spring.repository;

import org.springframework.data.repository.CrudRepository;

import com.cj.spring.bean.User;
public interface UserRepository extends CrudRepository<User, Long>
{
	

}
