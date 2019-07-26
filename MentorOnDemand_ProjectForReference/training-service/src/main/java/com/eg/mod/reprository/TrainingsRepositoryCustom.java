package com.eg.mod.reprository;

import com.eg.mod.model.TrainingDtls;

public interface TrainingsRepositoryCustom {
	
	TrainingDtls findAvgRating(Long mentorId, Long skillId);

}
