package com.eg.mod.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eg.mod.entity.Skill;
import com.eg.mod.exception.PaginationSortingException;
import com.eg.mod.pagination.Direction;
import com.eg.mod.pagination.OrderBy;
import com.eg.mod.service.SkillService;
import com.eg.mod.util.Translator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/skills")
@Api(value = "technology-service", description = "Operations pertaining to technology services")
public class SkillsController {

	@Autowired
	private SkillService skillService;

	@ApiOperation(value = "Find skill details for specific skill id", response = Skill.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 404, message = "Resource Not Found"),			
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@GetMapping("/findById/{id}")
	public Skill findById(
			@ApiParam(value = "Skill Id for which record needs to find", required = true) @PathVariable(value = "id", required = true) Long id) {
		
		return skillService.findById(id);
	}

	@ApiOperation(value = "Find skill details for specific skill name", response = Skill.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 404, message = "Resource Not Found"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@GetMapping("/findByName/{skillName}")
	public Skill findByName(
			@ApiParam(value = "Mentor Id for which record needs to find", required = true) @PathVariable(value = "skillName", required = true) String skillName) {
		
		return skillService.findByName(skillName);
	}

	@ApiOperation(value = "View list of skill details where skill name matches", response = List.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@GetMapping("/findByLikeName/{skillName}")
	public List<Skill> findByLikeName(
			@ApiParam(value = "Mentor Id for which record needs to find", required = true) @PathVariable(value = "skillName", required = true) String skillName) {
		
		return skillService.findByLikeName("%"+skillName.toLowerCase()+"%");
	}
	
	@ApiOperation(value = "View list of all registered skills", response = Page.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 412, message = "Invalid Sort Direction"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@GetMapping("/findAllSkills")
	public Page<Skill> findAllSkills(
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

		return skillService.findAllSkills(orderBy, direction, page, size);
	}

	@ApiOperation(value = "Add skill details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 302, message = "Resource Exist"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addSkill")
	public com.eg.mod.model.ApiResponse<?> addSkill(
			@ApiParam(value = "Skill object which needs to add", required = true) @Valid @RequestBody Skill skill) {

		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.skill.add"), skillService.addSkill(skill));
		
	}

	@ApiOperation(value = "Update skill details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 404, message = "Resource Not Found"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updateSkill")
	public com.eg.mod.model.ApiResponse<?> updateSkill(
			@ApiParam(value = "Skill object which needs to update", required = true) @Valid @RequestBody Skill skill) {

		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.skill.update"), skillService.updateSkill(skill));
		
	}

	@ApiOperation(value = "Delete skill details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully performed the operation"),
		@ApiResponse(code = 404, message = "Resource Not Found"),
		@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteSkill/{id}")
	public com.eg.mod.model.ApiResponse<?> deleteSkill(
			@ApiParam(value = "Skill Id for which record needs to delete", required = true) @PathVariable(value = "id", required = true) Long id) {

		skillService.deleteSkill(id);
		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.skill.delete"));
	}
}