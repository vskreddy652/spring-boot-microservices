package com.eg.mod.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.eg.mod.model.TrainingDtls;

@FeignClient(value = "training-service", fallback = TrainingServiceFallback.class)
@RibbonClient(value = "training-service")
public interface TrainingServiceProxy {

	@PostMapping("/trainings/findAvgRating/{mentorId}/{skillId}")
	public TrainingDtls findAvgRating(
			@PathVariable(value = "mentorId", required = true) Long mentorId,
			@PathVariable(value = "skillId", required = true) Long skillId);

}

@Component
class TrainingServiceFallback implements TrainingServiceProxy {

	@Override
	public TrainingDtls findAvgRating(Long mentorId, Long skillId) {
		return new TrainingDtls();
	}

}