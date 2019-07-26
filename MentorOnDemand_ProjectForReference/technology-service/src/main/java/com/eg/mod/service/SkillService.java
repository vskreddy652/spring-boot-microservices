package com.eg.mod.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eg.mod.entity.Skill;
import com.eg.mod.exception.ResourceExistException;
import com.eg.mod.exception.ResourceNotFoundException;
import com.eg.mod.exception.ServiceUnavailableException;
import com.eg.mod.reprository.SkillRepository;
import com.eg.mod.util.Translator;

@Service
@Transactional(readOnly = true)
public class SkillService {

	@Autowired
	private SkillRepository skillRepository;

	@SuppressWarnings("deprecation")
	//@HystrixCommand(fallbackMethod = "fallback_findAllSkills", commandKey = "findAllSkills", groupKey = "findAllSkills")
	@Cacheable(value= "allSkillsCache")
	public Page<Skill> findAllSkills(String orderBy, String direction, int page, int size) {

		Sort sort = null;
		if (direction.equals("ASC")) {
			sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
		} else if (direction.equals("DESC")) {
			sort = new Sort(new Sort.Order(Direction.DESC, orderBy));
		}
		Pageable pageable = new PageRequest(page, size, sort);
		return skillRepository.findAll(pageable);
	}

	//@HystrixCommand(fallbackMethod = "fallback_findById", commandKey = "findById", groupKey = "findById", ignoreExceptions = { ResourceNotFoundException.class })
	@Cacheable(value= "skillCache", key= "#id")
	public Skill findById(Long id) {
		return Optional.ofNullable(skillRepository.findById(id).get())
				.orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.skillId", id)));
	}

	//@HystrixCommand(fallbackMethod = "fallback_findByName", commandKey = "findByName", groupKey = "findByName", ignoreExceptions = { ResourceNotFoundException.class })
	@Cacheable(value= "skillCache", key= "#skillName")
	public Skill findByName(String skillName) {
		return Optional.ofNullable(skillRepository.findByName(skillName.toLowerCase()))
				.orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.skillName", skillName)));
	}

	//@HystrixCommand(fallbackMethod = "fallback_findByLikeName", commandKey = "findByLikeName", groupKey = "findByLikeName")
	@Cacheable(value= "allSkillsCache")	
	public List<Skill> findByLikeName(String skillName) {
		return skillRepository.findByLikeName(skillName.toLowerCase());
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_addSkill", commandKey = "addSkill", groupKey = "addSkill", ignoreExceptions = { ResourceExistException.class })
	@Caching(
			put = { @CachePut(value = "skillCache", key = "#skill.id") }, 
			evict = { @CacheEvict(value = "allSkillsCache", allEntries = true) }
	)
	public Skill addSkill(Skill skill) {

		if (skillRepository.findByName(skill.getName()) != null)
			throw new ResourceExistException(Translator.toLocale("error.resource.found.skillName", skill.getName()));

		skillRepository.save(skill);
		return skill;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_updateSkill", commandKey = "updateSkill", groupKey = "updateSkill", ignoreExceptions = { ResourceNotFoundException.class })
	@Caching(
			put = { @CachePut(value = "skillCache", key = "#skill.id") }, 
			evict = { @CacheEvict(value = "allSkillsCache", allEntries = true) }
	)
	public Skill updateSkill(Skill skill) {

		return skillRepository.findById(skill.getId()).map(oldSkill -> {
			oldSkill.setName(skill.getName());
			oldSkill.setToc(skill.getToc());
			oldSkill.setPrerequisites(skill.getPrerequisites());
			return skillRepository.save(oldSkill);
		}).orElseThrow(() -> new ResourceNotFoundException(
				Translator.toLocale("error.resource.notfound.skillId", skill.getId())));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_deleteSkills", commandKey = "deleteSkills", groupKey = "deleteSkills")
	@Caching(evict = { 
			@CacheEvict(value = "skillCache", key = "#id"),
			@CacheEvict(value = "allSkillsCache", allEntries = true) 
	})
	public void deleteSkill(Long id) {

		skillRepository.deleteById(id);
	}

	
	// list of fallback method for @HystrixCommand
	public Page<Skill> fallback_findAllSkills(String orderBy, String direction, int page, int size) {
		throw new ServiceUnavailableException(Translator.toLocale("error.technology.service"));
	}

	public Skill fallback_findById(Long skillId) {
		throw new ServiceUnavailableException(Translator.toLocale("error.technology.service"));
	}

	public Skill fallback_findByName(String skillName) {
		throw new ServiceUnavailableException(Translator.toLocale("error.technology.service"));
	}

	public List<Skill> fallback_findByLikeName(String skillName) {
		throw new ServiceUnavailableException(Translator.toLocale("error.technology.service"));
	}

	public Skill fallback_addSkill(Skill skill) {
		throw new ServiceUnavailableException(Translator.toLocale("error.technology.service"));
	}

	public Skill fallback_updateSkill(Long skillId, Skill skill) {
		throw new ServiceUnavailableException(Translator.toLocale("error.technology.service"));
	}

	public void fallback_deleteSkills(Long skillId) {
		throw new ServiceUnavailableException(Translator.toLocale("error.service", "Technology"));
	}
}
