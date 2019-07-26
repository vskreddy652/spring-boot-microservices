package com.eg.mod.reprository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eg.mod.entity.PaymentCommission;

public interface PaymentCommissionRepository extends MongoRepository<PaymentCommission, Long> {

}
