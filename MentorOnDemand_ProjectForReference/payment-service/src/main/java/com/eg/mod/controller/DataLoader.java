package com.eg.mod.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.eg.mod.entity.PaymentCommission;
import com.eg.mod.reprository.PaymentCommissionRepository;

@Component
public class DataLoader implements ApplicationRunner {

	private static Logger logger = LoggerFactory.getLogger(DataLoader.class);

	@Autowired
	private PaymentCommissionRepository paymentCommissionRepository;

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {

		paymentCommissionRepository.findById(1L).map(oldPaymentComm -> {
			logger.warn("Default payment commission value already exist");
			return oldPaymentComm;
		}).orElseGet(() -> {
			PaymentCommission newPaymentComm = new PaymentCommission();
			newPaymentComm.setId(1L);
			newPaymentComm.setCommissionPercent(12f);
			newPaymentComm.setPersistent(true); // to create created_date
			paymentCommissionRepository.save(newPaymentComm);
			logger.info("Default payment commission value "+ newPaymentComm.getCommissionPercent() +"% inserted");
			return newPaymentComm;
		});

	}
}
