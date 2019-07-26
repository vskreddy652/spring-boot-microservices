package com.eg.mod.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eg.mod.entity.MentorCalendar;
import com.eg.mod.exception.ResourceExistException;
import com.eg.mod.exception.ResourceNotFoundException;
import com.eg.mod.exception.ServiceUnavailableException;
import com.eg.mod.model.MentorCalendarDtls;
import com.eg.mod.model.SkillDtls;
import com.eg.mod.model.UserDtls;
import com.eg.mod.proxy.SkillServiceProxy;
import com.eg.mod.proxy.UserServiceProxy;
import com.eg.mod.reprository.MentorCalendarRepository;
import com.eg.mod.util.Translator;

@Service
@Transactional(readOnly = true)
public class MentorCalendarService {

	@Autowired
	private MentorCalendarRepository mentorCalendarRepository;

	@Autowired
	private SkillServiceProxy skillProxy;

	@Autowired
	private UserServiceProxy userProxy;
	
	@Transactional(propagation = Propagation.REQUIRED)
	/*@HystrixCommand(fallbackMethod = "fallback_addCalendarEntry", commandKey = "addCalendarEntry", groupKey = "addCalendarEntry", ignoreExceptions = {
			ServiceUnavailableException.class, ResourceNotFoundException.class, ResourceExistException.class })
	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")*/
	public MentorCalendar addCalendarEntry(Long mentorId, Long skillId, String startDateStr, String endDateStr,
			String startTimeStr, String endTimeStr) {

		UserDtls mentor = userProxy.findById(mentorId);
		SkillDtls skill = skillProxy.findById(skillId);

		if (mentor == null)
			throw new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.mentorId", mentorId));

		if (skill == null)
			throw new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.skillId", skillId));

		List<MentorCalendar> calendarEntries = mentorCalendarRepository.findCalendarEntry(mentorId, startDateStr,
				endDateStr, startTimeStr, endTimeStr);
		if (calendarEntries.size() > 0)
			throw new ResourceExistException(Translator.toLocale("error.resource.found.calendar", mentorId,
					startDateStr, endDateStr, startTimeStr, endTimeStr));

		MentorCalendar mentorCalendar = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = LocalDate.parse(startDateStr, formatter);
		LocalDate endDate = LocalDate.parse(endDateStr, formatter).plusDays(1);
		for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
			mentorCalendar = new MentorCalendar();
			mentorCalendar.setStartDate(date.toString());
			mentorCalendar.setEndDate(date.toString());
			mentorCalendar.setStartTime(startTimeStr);
			mentorCalendar.setEndTime(endTimeStr);
			mentorCalendar.setMentorId(mentor.getId());
			mentorCalendar.setSkillId(skillId);
			mentorCalendarRepository.save(mentorCalendar);
		}

		return mentorCalendar;

	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	/*@HystrixCommand(fallbackMethod = "fallback_findMentorCalendarByMentorId", commandKey = "findMentorCalendarByMentorId", groupKey = "findMentorCalendarByMentorId",
	ignoreExceptions = ServiceUnavailableException.class)
	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")*/
	public List<MentorCalendarDtls> findMentorCalendarByMentorId(Long mentorId, String startDate, String endDate) {
		/*if (startDate == null || endDate == null) {
			LocalDate today = LocalDate.now();
			startDate = today.withDayOfMonth(1).toString();
			endDate = today.withDayOfMonth(today.lengthOfMonth()).toString();
		}*/
		
		List<MentorCalendarDtls> mentorCalendarDtlsList = new ArrayList<>();
		MentorCalendar mentorCalendarObj = null;
		MentorCalendarDtls mentorCalendarDtlObj = null;
		SkillDtls skill = null;
		List<MentorCalendar> mentorCalendarList = mentorCalendarRepository.findMentorCalendarByMentorId(mentorId, startDate, endDate);
		for (int index = 0; index < mentorCalendarList.size(); index++) {
			mentorCalendarObj = mentorCalendarList.get(index);
			skill = skillProxy.findById(mentorCalendarObj.getSkillId());
			if (skill != null) {
				mentorCalendarDtlObj = new MentorCalendarDtls();
				BeanUtils.copyProperties(mentorCalendarObj, mentorCalendarDtlObj);
				mentorCalendarDtlObj.setSkillName(skill.getName());
				mentorCalendarDtlsList.add(mentorCalendarDtlObj);
			}
		}

		return mentorCalendarDtlsList;
	}

	
	// list of fallback method for @HystrixCommand
	public MentorCalendar fallback_addCalendarEntry(Long userId, Long skillId, String startDateStr, String endDateStr,
			String startTimeStr, String endTimeStr) {
		throw new ServiceUnavailableException(Translator.toLocale("error.search.service"));
	}

	public List<MentorCalendarDtls> fallback_findMentorCalendarByMentorId(Long mentorId, String startDate,
			String endDate) {
		throw new ServiceUnavailableException(Translator.toLocale("error.search.service"));
	}

}
