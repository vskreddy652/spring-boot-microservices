package com.eg.mod.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eg.mod.entity.MentorSkills;
import com.eg.mod.exception.PaginationSortingException;
import com.eg.mod.model.MentorSkillsDtls;
import com.eg.mod.pagination.Direction;
import com.eg.mod.pagination.OrderBy;
import com.eg.mod.service.MentorSkillsService;
import com.eg.mod.util.Translator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/search")
@Api(value = "search-service", description = "Operations pertaining to mentor skill services")
public class MentorSkillsController {

	@Autowired
	private MentorSkillsService mentorSkillService;

	@ApiOperation(value = "View list of mentors available within date-time range", response = Page.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 412, message = "Invalid Sort Direction"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@GetMapping("/findBySkillIdDateRange/{skillId}/{startDate}/{endDate}/{startTime}/{endTime}")
	public Page<MentorSkillsDtls> findBySkillIdDateRange(@PathVariable(value = "skillId", required = true) Long skillId,
			@ApiParam(value = "Start Date for which record needs to find", required = true) @PathVariable(value = "startDate", required = true) String startDate,
			@ApiParam(value = "End Date for which record needs to find", required = true) @PathVariable(value = "endDate", required = true) String endDate,
			@ApiParam(value = "Start Time for which record needs to find", required = true) @PathVariable(value = "startTime", required = true) String startTime,
			@ApiParam(value = "End Time for which record needs to find", required = true) @PathVariable(value = "endTime", required = true) String endTime,
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

		return mentorSkillService.findBySkillIdDateRange(skillId, startDate, endDate, startTime, endTime, orderBy,
				direction, page, size);
	}

	@ApiOperation(value = "View list of skills for specific mentor", response = Page.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 412, message = "Invalid Sort Direction"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@GetMapping("/findByMentorId/{mentorId}")
	public Page<MentorSkillsDtls> findByMentorId(
			@ApiParam(value = "Mentor Id for which record needs to find", required = true) @PathVariable(value = "mentorId", required = true) Long mentorId,
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

		return mentorSkillService.findByMentorId(mentorId, orderBy, direction, page, size);
	}

	@ApiOperation(value = "Add mentor skill details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 302, message = "Resource Exist"),
			@ApiResponse(code = 404, message = "Resource Not Found"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('MENTOR')")
	@PostMapping("/addMentorSkill")
	public com.eg.mod.model.ApiResponse<?> addMentorSkill(
			@ApiParam(value = "Mentor Skill details which needs to add", required = true) @Valid @RequestBody MentorSkills mentorSkill) {

		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.mentor.skill.add"),
				mentorSkillService.addMentorSkill(mentorSkill));

	}

	@ApiOperation(value = "Update mentor skill details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 404, message = "Resource Not Found"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('MENTOR')")
	@PutMapping("/updateMentorSkill")
	public com.eg.mod.model.ApiResponse<?> updateMentorSkill(
			@ApiParam(value = "Mentor Skill details which needs to update", required = true) @Valid @RequestBody MentorSkills mentorSkill) {

		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.mentor.skill.update"),
				mentorSkillService.updateMentorSkill(mentorSkill));

	}
	
	@ApiOperation(value = "Update training count")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('MENTOR')")
	@PutMapping("/updateMentorTrainingCount/{mentorId}/{skillId}")
	public com.eg.mod.model.ApiResponse<?> updateMentorTrainingCount(
			@ApiParam(value = "Mentor Id for which record needs to update", required = true) @PathVariable(value = "mentorId", required = true) Long mentorId,
			@ApiParam(value = "Skill Id for which record needs to update", required = true) @PathVariable(value = "skillId", required = true) Long skillId) {

		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), null,
				mentorSkillService.updateMentorTrainingCount(mentorId, skillId));

	}

	@ApiOperation(value = "Delete mentor skill details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 404, message = "Resource Not Found"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('MENTOR')")
	@DeleteMapping("/deleteMentorSkill/{id}")
	public com.eg.mod.model.ApiResponse<?> deleteMentorSkill(
			@ApiParam(value = "Mentor Skill Id for which record needs to delete", required = true) @PathVariable(value = "id", required = true) Long id) {

		mentorSkillService.deleteMentorSkill(id);
		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.mentor.skill.delete"));

	}

}