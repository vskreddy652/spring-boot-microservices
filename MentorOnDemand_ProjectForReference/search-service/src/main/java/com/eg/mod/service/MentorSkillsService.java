package com.eg.mod.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eg.mod.entity.MentorSkills;
import com.eg.mod.exception.ResourceExistException;
import com.eg.mod.exception.ResourceNotFoundException;
import com.eg.mod.exception.ServiceUnavailableException;
import com.eg.mod.model.MentorSkillsDtls;
import com.eg.mod.model.SkillDtls;
import com.eg.mod.model.TrainingDtls;
import com.eg.mod.model.UserDtls;
import com.eg.mod.proxy.SkillServiceProxy;
import com.eg.mod.proxy.TrainingServiceProxy;
import com.eg.mod.proxy.UserServiceProxy;
import com.eg.mod.reprository.MentorSkillsRepository;
import com.eg.mod.util.Translator;

@Service
@Transactional(readOnly = true)
public class MentorSkillsService {

	@Autowired
	private MentorSkillsRepository mentorSkillsRepository;

	@Autowired
	private SkillServiceProxy skillProxy;

	@Autowired
	private UserServiceProxy userProxy;
	
	@Autowired
	private TrainingServiceProxy trainingProxy;
	
	@SuppressWarnings("deprecation")
	/*@HystrixCommand(fallbackMethod = "fallback_findBySkillIdDateRange", commandKey = "findBySkillIdDateRange", groupKey = "findBySkillIdDateRange", ignoreExceptions = ServiceUnavailableException.class)
	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")*/
	public Page<MentorSkillsDtls> findBySkillIdDateRange(Long skillId, String startDate, String endDate,
			String startTime, String endTime, String orderBy, String direction, int page, int size) {

		List<MentorSkillsDtls> mentorSkillDtlsList = new ArrayList<>();
		MentorSkillsDtls mentorSkillDtlsObj = null;
		UserDtls mentorDtls = null;
		TrainingDtls trainingDtls = null;
		Sort sort = null;
		if (direction.equals("ASC")) {
			sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
		} else if (direction.equals("DESC")) {
			sort = new Sort(new Sort.Order(Direction.DESC, orderBy));
		}
		Pageable pageable = new PageRequest(page, size, sort);
		
		List<MentorSkills> mentorSkillList = mentorSkillsRepository.findBySkillIdDateRange(skillId, startDate, endDate, startTime, endTime, pageable).getContent();
		for (MentorSkills mentorSkillObj : mentorSkillList) {
			mentorDtls = userProxy.findById(mentorSkillObj.getMentorId());
			trainingDtls = trainingProxy.findAvgRating(mentorSkillObj.getMentorId(), skillId);
			if (mentorDtls != null) {		
				mentorSkillDtlsObj = new MentorSkillsDtls();
				BeanUtils.copyProperties(mentorSkillObj, mentorSkillDtlsObj);
				mentorSkillDtlsObj.setMentorName(mentorDtls.getFirstName() + " " + mentorDtls.getLastName());
				mentorSkillDtlsObj.setAvgRating(trainingDtls == null ? 0 : trainingDtls.getAvgRating());
				float totalFees = mentorSkillDtlsObj.getFees() + mentorSkillDtlsObj.getFees()
						* ((mentorSkillDtlsObj.getYearsOfExperience() / 100) + mentorSkillDtlsObj.getAvgRating() / 5);
				mentorSkillDtlsObj.setFees(totalFees);
				mentorSkillDtlsList.add(mentorSkillDtlsObj);
			}
		}
		return new PageImpl<>(mentorSkillDtlsList, pageable, mentorSkillDtlsList.size());
	}
	
	@SuppressWarnings("deprecation")
	/*@HystrixCommand(fallbackMethod = "fallback_findByMentorId", commandKey = "findByMentorId", groupKey = "findByMentorId", ignoreExceptions = ServiceUnavailableException.class)
	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")*/
	@Cacheable(value= "allMentorSkillsCache")
	public Page<MentorSkillsDtls> findByMentorId(Long mentorId, String orderBy, String direction, int page,
			int size) {
		
		List<MentorSkillsDtls> mentorSkillsDtlsList = new ArrayList<>();
		MentorSkillsDtls mentorSkillDtlObj = null;
		SkillDtls skill = null;
		Sort sort = null;
		if (direction.equals("ASC")) {
			sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
		} else if (direction.equals("DESC")) {
			sort = new Sort(new Sort.Order(Direction.DESC, orderBy));
		}
		Pageable pageable = new PageRequest(page, size, sort);
		List<MentorSkills> mentorSkillsList =  mentorSkillsRepository.findByMentorId(mentorId, pageable).getContent();
		for (MentorSkills mentorSkillObj : mentorSkillsList) {
			skill = skillProxy.findById(mentorSkillObj.getSkillId());
			if (skill != null) {
				mentorSkillDtlObj = new MentorSkillsDtls();
				BeanUtils.copyProperties(mentorSkillObj, mentorSkillDtlObj);
				mentorSkillDtlObj.setSkillName(skill.getName());
				mentorSkillsDtlsList.add(mentorSkillDtlObj);
			}
		}
		return new PageImpl<>(mentorSkillsDtlsList, pageable, mentorSkillsDtlsList.size());
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	/*@HystrixCommand(fallbackMethod = "fallback_addMentorSkill", commandKey = "addMentorSkill", groupKey = "addMentorSkill", ignoreExceptions = { ResourceNotFoundException.class,
			ResourceExistException.class, ServiceUnavailableException.class })*/
	@Caching(
			put = { @CachePut(value = "mentorSkillCache", key = "#mentorSkill.id") }, 
			evict = { @CacheEvict(value = "allMentorSkillsCache", allEntries = true) }
	)
	public MentorSkills addMentorSkill(MentorSkills mentorSkill) {

		UserDtls mentor = userProxy.findById(mentorSkill.getMentorId());
		SkillDtls skill = skillProxy.findById(mentorSkill.getSkillId());
		if (mentor == null)
			throw new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.mentorId", mentorSkill.getMentorId()));
		else if (skill == null)
			throw new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.skillId", mentorSkill.getSkillId()));

		if (mentorSkillsRepository.findByMentorIdSkillId(mentorSkill.getMentorId(), mentorSkill.getSkillId()) != null)
			throw new ResourceExistException(Translator.toLocale("error.resource.found.skill"));
		mentorSkillsRepository.save(mentorSkill);
		return mentorSkill;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_updateMentorSkill", commandKey = "updateMentorSkill", groupKey = "updateMentorSkill", ignoreExceptions = { ResourceNotFoundException.class })
	@Caching(
			put = { @CachePut(value = "mentorSkillCache", key = "#mentorSkill.id") }, 
			evict = { @CacheEvict(value = "allMentorSkillsCache", allEntries = true) }
	)
	public MentorSkills updateMentorSkill(MentorSkills mentorSkill) {

		return mentorSkillsRepository.findById(mentorSkill.getId()).map(oldMentorSkill -> {
			if (mentorSkill.getSelfRating().floatValue() != 0.0f)
				oldMentorSkill.setSelfRating(mentorSkill.getSelfRating());
			if (mentorSkill.getYearsOfExperience().floatValue() != 0.0f)
				oldMentorSkill.setYearsOfExperience(mentorSkill.getYearsOfExperience());
			if (mentorSkill.getTariningsDelivered() != null)
				oldMentorSkill.setTariningsDelivered(mentorSkill.getTariningsDelivered());
			if (mentorSkill.getFacilitiesOffered() != null)
				oldMentorSkill.setFacilitiesOffered(mentorSkill.getFacilitiesOffered());
			return mentorSkillsRepository.save(mentorSkill);
		}).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.mentorSkillId", mentorSkill.getId())));

	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_updateMentorTrainingCount", commandKey = "updateMentorTrainingCount", groupKey = "updateMentorTrainingCount")
	@Caching(
			put = { @CachePut(value = "mentorSkillCache", key = "#skillId") }, 
			evict = { @CacheEvict(value = "allMentorSkillsCache", allEntries = true) }
	)
	public MentorSkills updateMentorTrainingCount(Long mentorId, Long skillId) {

		MentorSkills mentorSkillsObj = mentorSkillsRepository.findByMentorIdSkillId(mentorId, skillId);
		if (mentorSkillsObj != null) {
			mentorSkillsObj.setTariningsDelivered(mentorSkillsObj.getTariningsDelivered() + 1);
			mentorSkillsRepository.save(mentorSkillsObj);
		}
		return mentorSkillsObj;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_deleteMentorSkill", commandKey = "deleteMentorSkill", groupKey = "deleteMentorSkill", ignoreExceptions = { ResourceNotFoundException.class })
	@Caching(evict = { 
			@CacheEvict(value = "mentorSkillCache", key = "#id"),
			@CacheEvict(value = "allMentorSkillsCache", allEntries = true) 
	})	
	public void deleteMentorSkill(Long id) {

		mentorSkillsRepository.findById(id).map(mentorSkill -> {
			mentorSkillsRepository.delete(mentorSkill);
			return true;
		}).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.mentorSkillId", id)));
	}


	public Page<MentorSkillsDtls> fallback_findBySkillIdDateRange(Long skillId, String startDate, String endDate,
			String startTime, String endTime, String orderBy, String direction, int page, int size) {
		throw new ServiceUnavailableException(Translator.toLocale("error.search.service"));
	}

	public Page<MentorSkillsDtls> fallback_findByMentorId(Long mentorId, String orderBy, String direction, int page,
			int size) {
		throw new ServiceUnavailableException(Translator.toLocale("error.search.service"));
	}

	public MentorSkills fallback_findByMentorIdSkillId(Long mentorId, Long skillId) {
		throw new ServiceUnavailableException(Translator.toLocale("error.search.service"));
	}

	public MentorSkills fallback_addMentorSkill(MentorSkills mentorSkill) {
		throw new ServiceUnavailableException(Translator.toLocale("error.search.service"));
	}

	public MentorSkills fallback_updateMentorSkill(Long id, MentorSkills mentorSkill) {
		throw new ServiceUnavailableException(Translator.toLocale("error.search.service"));
	}

	public void fallback_updateMentorTrainingCount(Long mentorId, Long skillId) {
		throw new ServiceUnavailableException(Translator.toLocale("error.search.service"));
	}

	public void fallback_deleteMentorSkill(Long id) {
		throw new ServiceUnavailableException(Translator.toLocale("error.search.service"));
	}
	
}
