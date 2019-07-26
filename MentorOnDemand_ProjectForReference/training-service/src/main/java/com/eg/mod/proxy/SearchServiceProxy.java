package com.eg.mod.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.eg.mod.model.ApiResponse;

@FeignClient(value = "search-service", fallback = SearchServiceFallback.class)
@RibbonClient(value = "search-service")
public interface SearchServiceProxy {

	@PostMapping("/search/addCalendarEntry/{mentorId}/{skillId}/{startDate}/{endDate}/{startTime}/{endTime}")
	public ApiResponse<?> addCalendarEntry(
			@RequestHeader("Authorization") String authorizationToken,
			@PathVariable(value = "mentorId", required = true) Long mentorId,
			@PathVariable(value = "skillId", required = true) Long skillId,
			@PathVariable(value = "startDate", required = true) String startDate,
			@PathVariable(value = "endDate", required = true) String endDate,
			@PathVariable(value = "startTime", required = true) String startTime,
			@PathVariable(value = "endTime", required = true) String endTime);

	@PutMapping("/search/updateMentorTrainingCount/{mentorId}/{skillId}")
	public ApiResponse<?> updateMentorTrainingCount(
			@RequestHeader("Authorization") String authorizationToken,
			@PathVariable(value = "mentorId", required = true) Long mentorId,
			@PathVariable(value = "skillId", required = true) Long skillId);
}

@Component
class SearchServiceFallback implements SearchServiceProxy {

	@Override
	public ApiResponse<?> addCalendarEntry(String authorizationToken, Long mentorId, Long skillId, String startDate,
			String endDate, String startTime, String endTime) {
		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), null, null);
	}

	@Override
	public ApiResponse<?> updateMentorTrainingCount(String authorizationToken, Long mentorId, Long skillId) {
		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), null, null);
	}
}
