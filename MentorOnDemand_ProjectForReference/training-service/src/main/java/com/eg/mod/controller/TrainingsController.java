package com.eg.mod.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eg.mod.entity.Training;
import com.eg.mod.exception.PaginationSortingException;
import com.eg.mod.model.Constants;
import com.eg.mod.model.TrainingDtls;
import com.eg.mod.model.Constants.TrainingStatus;
import com.eg.mod.pagination.Direction;
import com.eg.mod.pagination.OrderBy;
import com.eg.mod.service.TrainingService;
import com.eg.mod.util.Translator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/trainings")
@Api(value = "training-service", description = "Operations pertaining to training services")
public class TrainingsController {

	@Autowired
	private TrainingService trainingService;

	@ApiOperation(value = "View all registered trainings", response = Page.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 412, message = "Invalid Sort Direction"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/findAllTrainings")
	public Page<TrainingDtls> findAllTrainings(
			@ApiParam(value = "Ordring By search rerods", required = true) @RequestParam(value = "orderBy", required = true) String orderBy,
			@ApiParam(value = "Ordring search rerods (ASC/DESC)", required = true) @RequestParam(value = "direction", required = true) String direction,
			@ApiParam(value = "Offset value for pagination", required = true) @RequestParam(value = "page", required = true) int page,
			@ApiParam(value = "Per page record size in pagination", required = true) @RequestParam(value = "size", required = true) int size) {

		if (!(direction.equals(Direction.ASCENDING.getDirectionCode())
				|| direction.equals(Direction.DESCENDING.getDirectionCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.order"));
		}
		if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.NAME.getOrderByCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.orderby"));
		}

		return trainingService.findAllTrainings(orderBy, direction, page, size);
	}

	@ApiOperation(value = "View all registered trainings for specific mentor & skill id", response = Page.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 412, message = "Invalid Sort Direction"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@GetMapping("/findByMentorIdSkillId/{mentorId}/{skillId}")
	public Page<TrainingDtls> findByMentorIdSkillId(
			@ApiParam(value = "Mentor Id for which record needs to find", required = true) @PathVariable(value = "mentorId", required = true) Long mentorId,
			@ApiParam(value = "Skill Id for which record needs to find", required = true) @PathVariable(value = "skillId", required = true) Long skillId,
			@ApiParam(value = "Ordring By search rerods", required = true) @RequestParam(value = "orderBy", required = true) String orderBy,
			@ApiParam(value = "Ordring search rerods (ASC/DESC)", required = true) @RequestParam(value = "direction", required = true) String direction,
			@ApiParam(value = "Offset value for pagination", required = true) @RequestParam(value = "page", required = true) int page,
			@ApiParam(value = "Per page record size in pagination", required = true) @RequestParam(value = "size", required = true) int size) {

		if (!(direction.equals(Direction.ASCENDING.getDirectionCode())
				|| direction.equals(Direction.DESCENDING.getDirectionCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.order"));
		}
		if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.USERID.getOrderByCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.orderby"));
		}

		return trainingService.findByMentorIdSkillId(mentorId, skillId, orderBy, direction, page, size);
	}

	@ApiOperation(value = "View all registered trainings for specific mentor id", response = Page.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 412, message = "Invalid Sort Direction"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasAnyRole('MENTOR')")
	@GetMapping("/findProposedTrainings/{mentorId}")
	public Page<TrainingDtls> findProposedTrainings(
			@ApiParam(value = "Mentor Id for which record needs to find", required = true) @PathVariable(value = "mentorId", required = true) Long mentorId,
			@ApiParam(value = "Ordring By search rerods", required = true) @RequestParam(value = "orderBy", required = true) String orderBy,
			@ApiParam(value = "Ordring search rerods (ASC/DESC)", required = true) @RequestParam(value = "direction", required = true) String direction,
			@ApiParam(value = "Offset value for pagination", required = true) @RequestParam(value = "page", required = true) int page,
			@ApiParam(value = "Per page record size in pagination", required = true) @RequestParam(value = "size", required = true) int size) {

		if (!(direction.equals(Direction.ASCENDING.getDirectionCode())
				|| direction.equals(Direction.DESCENDING.getDirectionCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.order"));
		}
		if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.USERID.getOrderByCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.orderby"));
		}

		return trainingService.findProposedTrainings(mentorId, orderBy, direction, page, size);
	}

	/*public ResponseEntity<List<?>> getAll(
	@RequestParam(value = "page", required = false) Integer offset,
	@RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {
     Page<?> page = authorRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
     HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/authors", offset, limit);
     return new ResponseEntity<List<?>>(page.getContent(), headers, HttpStatus.OK); }
	 */

	@ApiOperation(value = "View all registered trainings for specific user id & training status", response = Page.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 412, message = "Invalid Sort Direction"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/findByUserIdAndStatus/{userId}/{status}")
	public Page<TrainingDtls> findTrainingByUserIdAndStatus(
			@ApiParam(value = "User Id for which record needs to find", required = true) @PathVariable(value = "userId", required = true) Long userId,
			@ApiParam(value = "Training status for which record needs to find", required = true) @PathVariable(value = "status", required = true) String status,
			@ApiParam(value = "Ordring By search rerods", required = true) @RequestParam(value = "orderBy", required = true) String orderBy,
			@ApiParam(value = "Ordring search rerods (ASC/DESC)", required = true) @RequestParam(value = "direction", required = true) String direction,
			@ApiParam(value = "Offset value for pagination", required = true) @RequestParam(value = "page", required = true) int page,
			@ApiParam(value = "Per page record size in pagination", required = true) @RequestParam(value = "size", required = true) int size) {

		if (!(direction.equals(Direction.ASCENDING.getDirectionCode())
				|| direction.equals(Direction.DESCENDING.getDirectionCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.order"));
		}
		if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.USERID.getOrderByCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.orderby"));
		}

		List<String> trainingStatus = new ArrayList<String>();
		if (status.equals(TrainingStatus.INPROGRESS.getStatus())) {
			for (String str : Constants.getInProgress())
				trainingStatus.add(str);
		} else if (status.indexOf(";") != -1) {
			for (String item : status.split(";"))
				trainingStatus.add(item);
		} else
			trainingStatus.add(status);

		return trainingService.findByUserIdAndStatus(userId, trainingStatus, orderBy, direction, page, size);
	}

	@ApiOperation(value = "View all registered trainings for specific mentor id & training status", response = Page.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 412, message = "Invalid Sort Direction"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('MENTOR')")
	@GetMapping("/findByMentorIdAndStatus/{mentorId}/{status}")
	public Page<TrainingDtls> findTrainingByMentorIdAndStatus(
			@ApiParam(value = "Mentor Id for which record needs to find", required = true) @PathVariable(value = "mentorId", required = true) Long mentorId,
			@ApiParam(value = "Training status for which record needs to find", required = true) @PathVariable(value = "status", required = true) String status,
			@ApiParam(value = "Ordring By search rerods", required = true) @RequestParam(value = "orderBy", required = true) String orderBy,
			@ApiParam(value = "Ordring search rerods (ASC/DESC)", required = true) @RequestParam(value = "direction", required = true) String direction,
			@ApiParam(value = "Offset value for pagination", required = true) @RequestParam(value = "page", required = true) int page,
			@ApiParam(value = "Per page record size in pagination", required = true) @RequestParam(value = "size", required = true) int size) {

		if (!(direction.equals(Direction.ASCENDING.getDirectionCode())
				|| direction.equals(Direction.DESCENDING.getDirectionCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.order"));
		}
		if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.USERID.getOrderByCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.orderby"));
		}

		List<String> trainingStatus = new ArrayList<String>();
		if (status.equals(TrainingStatus.INPROGRESS.getStatus())) {
			for (String str : Constants.getInProgress())
				trainingStatus.add(str);
		} else if (status.indexOf(";") != -1) {
			for (String item : status.split(";"))
				trainingStatus.add(item);
		} else
			trainingStatus.add(status);

		return trainingService.findByMentorIdAndStatus(mentorId, trainingStatus, orderBy, direction, page, size);
	}

	@ApiOperation(value = "Find average rating for mentor id & skill id combination", response = List.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PostMapping("/findAvgRating/{mentorId}/{skillId}")
	public TrainingDtls findAvgRating(
			@ApiParam(value = "Mentor Id for which record needs to find", required = true) @PathVariable(value = "mentorId", required = true) Long mentorId,
			@ApiParam(value = "Skill Id for which record needs to find", required = true) @PathVariable(value = "skillId", required = true) Long skillId) {
		return trainingService.findAvgRating(mentorId, skillId);
	}
	
	@ApiOperation(value = "Find training details for specific training id", response = Training.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 404, message = "Resource Not Found"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MENTOR')")
	@GetMapping("/findById/{id}")
	public Training findById(
			@ApiParam(value = "Training Id for which record needs to find", required = true) @PathVariable(value = "id", required = true) Long id) {
		return trainingService.findById(id);
	}

	// send Proposal
	@ApiOperation(value = "Add training details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 302, message = "Resource Exist"),
		@ApiResponse(code = 404, message = "Resource Not Found"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/proposeTraining")
	public com.eg.mod.model.ApiResponse<?> proposeTraining(
			@ApiParam(value = "Training details which needs to add", required = true) @Valid @RequestBody Training training) {

		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.training.proposal"),
				trainingService.proposeTraining(training));

	}

	@ApiOperation(value = "Approve/Reject training details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 404, message = "Resource Not Found"),
		@ApiResponse(code = 500, message = "Data Validation Error"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('MENTOR')")
	@PutMapping("/approveTraining/{id}/{status}")
	public com.eg.mod.model.ApiResponse<?> approveTraining(
			@ApiParam(value = "Training Id for which record needs to update", required = true) @PathVariable(value = "id", required = true) Long id,
			@ApiParam(value = "Training status i.e. CONFIRMED/REJECTED", required = true) @PathVariable(value = "status", required = true) String status,
			@ApiParam(value = "GWT authentication token", required = true) @RequestHeader("Authorization") String authToken) {

		Training training = new Training();
		training.setId(id);
		training.setStatus(status);
		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.training.approve", status),
				trainingService.updateTraining(training, authToken));

	}

	@ApiOperation(value = "Update training details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 302, message = "Resource Exist"),
		@ApiResponse(code = 404, message = "Resource Not Found"),
		@ApiResponse(code = 500, message = "Data Validation Error"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/updateTraining")
	public com.eg.mod.model.ApiResponse<?> updateTraining(
			@ApiParam(value = "Training details which needs to update", required = true) @Valid @RequestBody Training training, 
			@ApiParam(value = "GWT authentication token", required = true) @RequestHeader("Authorization") String authToken) {

		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.training.update"),
				trainingService.updateTraining(training, authToken));

	}

	@ApiOperation(value = "Delete training details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 404, message = "Resource Not Found"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteTraining/{id}")
	public com.eg.mod.model.ApiResponse<?> deleteTraining(
			@ApiParam(value = "Training Id for which record needs to delete", required = true) @PathVariable(value = "id", required = true) Long id) {

		trainingService.deleteTraining(id);
		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.training.delete"));
	}

}