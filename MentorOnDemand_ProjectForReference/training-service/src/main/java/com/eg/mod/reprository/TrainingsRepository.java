package com.eg.mod.reprository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.eg.mod.entity.Training;

public interface TrainingsRepository extends MongoRepository<Training, Long>, TrainingsRepositoryCustom {

	@Query(value = "{ 'userId':?0, 'mentorId':?1, 'skillId':?2, "
			+ "'startDate':{$gte:?3, $lte:?4 }, 'endDate':{$gte:?3, $lte:?4 }, "
			+ "'startTime':{$gte:?5, $lte:?6 }, 'endTime':{$gte:?5, $lte:?6 } }")
	Training findRegisteredTraining(Long userId, Long mentorId, Long skillId, String startDate, String endDate,
			String startTime, String endTime);

	Page<Training> findAll(Pageable pageable);

	@Query(value = "{ 'mentorId':?0, 'status':'PROPOSED'}")
	Page<Training> findProposedTrainings(Long mentorId, Pageable pageable);

	@Query(value = "{ 'mentorId':?0, 'skillId':?1 }")
	Page<Training> findByMentorIdSkillId(Long mentorId, Long skillId, Pageable pageable);

	@Query(value = "{ 'userId':?0, 'status': {$in:?1} }")
	Page<Training> findByUserIdAndStatus(Long userId, List<String> trainingStatus, Pageable pageable);

	@Query(value = "{ 'mentorId':?0, 'status': {$in:?1} }")
	Page<Training> findByMentorIdAndStatus(Long mentorId, List<String> trainingStatus, Pageable pageable);

	// @Query(value = "{ 'status':{$in : ?0} }")
	// List<Training> findByTrainingStatus(List<String> trainingStatus);

	// @Query(value = "{ 'mentorId':?0, 'skillId':?1 }", count = true)
	// Long countByMentorIdSkillId(Long mentorId, Long skillId);

	@Query(value = "{ 'status':{$in:?0}, 'endDate':{'$gt':'new Date()'} }")
	List<Training> findExpiredTrainings(List<String> trainingStatus);
	
	@Query(value = "{ 'amountReceived':{$ne:0}, 'status':{$in:?0}, 'startDate':{'$gte':'new Date()'} }")
	List<Training> findStartedTrainings(List<String> trainingStatus);
	
	@Query(value = "{ 'amountReceived':{$ne:0}, 'status':{$in:?0} }")
	List<Training> findByTrainingStatus(List<String> trainingStatus);
}
