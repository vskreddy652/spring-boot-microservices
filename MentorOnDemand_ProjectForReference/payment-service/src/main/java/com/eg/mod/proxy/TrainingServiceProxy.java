package com.eg.mod.proxy;

import java.util.List;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.eg.mod.model.TrainingDtls;

import java.util.ArrayList;

@FeignClient(value = "training-service", fallback = TrainingServiceFallback.class)
@RibbonClient(value = "training-service")
public interface TrainingServiceProxy {

	@GetMapping("/trainings/findById/{id}")
	public TrainingDtls findById(
			@RequestHeader("Authorization") String authToken,
			@PathVariable(value = "id", required = true) Long id);

	// GET http://localhost:8080/trainings/findByTrainingStatus/1,2,3,4
	@GetMapping("/trainings/findByTrainingStatus/{trainingStatus}")
	public List<TrainingDtls> findByTrainingStatus(
			@PathVariable(value = "trainingStatus", required = true) List<String> trainingStatus);
}

@Component
class TrainingServiceFallback implements TrainingServiceProxy {

	@Override
	public TrainingDtls findById(String authToken, Long id) {
		return new TrainingDtls();
	}

	@Override
	public List<TrainingDtls> findByTrainingStatus(List<String> trainingStatus) {
		return new ArrayList<TrainingDtls>();
	}

}