package com.eg.mod.reprository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.eg.mod.entity.Payment;

public interface PaymentRepository extends MongoRepository<Payment, Long>, PaymentRepositoryCustom {
	
	@Query(value = "{ 'mentorId':?0, 'createdDate':{$gte:?0, $lte:?1 } }")
	Page<Payment> findPaymentDtlsByMentorid(Long mentorId, Date startDate, Date endDate, Pageable pageable);

	@Query(value = "{ 'createdDate':{$gte:?0, $lte:?1 } }")
	Page<Payment> findPaymentDtlsByDateRange(Date startDate, Date endDate, Pageable pageable);
	
}
