package com.eg.mod.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.eg.mod.entity.User;
import com.eg.mod.reprository.UserRepository;
import com.eg.mod.service.SequenceGeneratorService;

@Component
public class DataLoader implements ApplicationRunner {

	private static Logger logger = LoggerFactory.getLogger(DataLoader.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SequenceGeneratorService sequencegenerator;

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {

		userRepository.findByName("admin").map(oldUser -> {
			logger.warn("admin user already exist");
			return oldUser;
		}).orElseGet(() -> {
			User newUser = new User();
			newUser.setId(sequencegenerator.generateSequence(User.SEQUENCE_NAME));
			newUser.setUserName("admin");
			newUser.setPassword("$2a$04$j5Px7Uk5a/sgJcHMlBkGk.GH0tFSrPpfn5VqA5MByr8vBWdN4qU3a");
			newUser.setFirstName("Admin");
			newUser.setLastName("Admin");
			newUser.setContactNumber(9434580584L);
			newUser.setRole("ADMIN");
			newUser.setActive(true);
			newUser.setConfirmedSignUp(true);
			newUser.setPersistent(true); // to create created_date
			userRepository.save(newUser);
			logger.info("admin user inserted");
			return newUser;
		});

	}
}
