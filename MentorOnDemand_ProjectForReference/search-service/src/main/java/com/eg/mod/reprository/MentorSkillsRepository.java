package com.eg.mod.reprository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.eg.mod.entity.MentorSkills;

public interface MentorSkillsRepository extends PagingAndSortingRepository<MentorSkills, Long> {
	
	//@Query("SELECT COUNT(ms) FROM MentorSkills ms WHERE ms.mentorId=?1 AND ms.skillId=?2")
    //Long countByMentorIdSkillId(Long mentorId, Long skillId);

	String findBySkillIdAndDateRange = "SELECT * FROM Mentor_Skills ms "
			+ "WHERE ms.skill_id=?1 AND ms.mentor_id NOT IN ("
			+ "  SELECT DISTINCT mc.mentor_id FROM Mentor_Calendar mc " + 
			"    WHERE (mc.start_date BETWEEN ?2 AND ?3 AND mc.end_date BETWEEN ?2 AND ?3) AND " + 
			"          (mc.start_time BETWEEN ?4 AND ?5 AND mc.end_time BETWEEN ?4 AND ?5) "
			+ ")";
	@Query(value = findBySkillIdAndDateRange, nativeQuery = true)
	Page<MentorSkills> findBySkillIdDateRange(Long skillId, String startDate, String endDate, String startTime, String endTime, Pageable pageable);
	
	@Query(value = "SELECT ms FROM MentorSkills ms WHERE ms.mentorId=?1")
	Page<MentorSkills> findByMentorId(Long mentorId, Pageable pageable);
	
	@Query(value = "SELECT ms FROM MentorSkills ms WHERE ms.mentorId=?1 AND ms.skillId=?2")
	MentorSkills findByMentorIdSkillId(Long mentorId, Long skillId);
}
