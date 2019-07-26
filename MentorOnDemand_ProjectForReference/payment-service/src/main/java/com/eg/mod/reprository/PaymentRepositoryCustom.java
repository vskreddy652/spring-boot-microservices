package com.eg.mod.reprository;

import com.eg.mod.model.PaymentDtls;

public interface PaymentRepositoryCustom {

    PaymentDtls aggregateByMentorId(Long mentorId, Long trainingId);
    
}
