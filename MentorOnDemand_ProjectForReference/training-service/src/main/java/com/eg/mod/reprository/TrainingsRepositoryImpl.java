package com.eg.mod.reprository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.eg.mod.entity.Training;
import com.eg.mod.model.TrainingDtls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TrainingsRepositoryImpl implements TrainingsRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

/*	@Override
	public List<TrainingDtls> findAvgRating(List<TrainingDtls> list) {

		List<Criteria> trainingCriterias = new ArrayList<>();
		for (TrainingDtls element : list) {
			trainingCriterias.add(Criteria.where("mentorId").is(element.getMentorId()).and("skillId").is(element.getSkillId()));
		}
		MatchOperation matchOperation = Aggregation.match(new Criteria().orOperator(trainingCriterias.toArray(new Criteria[trainingCriterias.size()])));
		GroupOperation groupOperation = Aggregation.group("mentorId", "skillId").last("mentorId").as("mentorId")
				.last("skillId").as("skillId").avg("rating").as("avgRating");
		ProjectionOperation projectionOperation = Aggregation.project("mentorId", "skillId", "avgRating");

		list =  mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation),
				Training.class, TrainingDtls.class).getMappedResults();

		ObjectMapper mapper = new ObjectMapper();
		try {
			for (TrainingDtls obj : list) {
				String json = mapper.writeValueAsString(obj);
				System.out.println("JSON = " + json);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return list;
	}*/

	@Override
	public TrainingDtls findAvgRating(Long mentorId, Long skillId) {

		TrainingDtls trainingDtls = null;
		MatchOperation matchOperation = Aggregation.match(Criteria.where("mentorId").is(mentorId).and("skillId").is(skillId));
		GroupOperation groupOperation = Aggregation.group("mentorId", "skillId").last("mentorId").as("mentorId")
				.last("skillId").as("skillId").avg("rating").as("avgRating");
		ProjectionOperation projectionOperation = Aggregation.project("mentorId", "skillId", "avgRating");

		trainingDtls =  mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation),
				Training.class, TrainingDtls.class).getUniqueMappedResult();

		ObjectMapper mapper = new ObjectMapper();		
		try {
			String json = mapper.writeValueAsString(trainingDtls);
			System.out.println("JSON = " + json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return trainingDtls;
	}
	
	/*
	 * @Override public List<WarehouseSummary> aggregate(float minPrice, float
	 * maxPrice) { MatchOperation matchOperation = getMatchOperation(minPrice,
	 * maxPrice); GroupOperation groupOperation = getGroupOperation();
	 * ProjectionOperation projectionOperation = getProjectOperation();
	 * 
	 * return mongoTemplate.aggregate(Aggregation.newAggregation( matchOperation,
	 * groupOperation, projectionOperation ), Product.class,
	 * WarehouseSummary.class).getMappedResults(); }
	 * 
	 * private MatchOperation getMatchOperation(float minPrice, float maxPrice) {
	 * Criteria priceCriteria =
	 * where("price").gt(minPrice).andOperator(where("price").lt(maxPrice)); return
	 * match(priceCriteria); }
	 * 
	 * private GroupOperation getGroupOperation() { return group("warehouse")
	 * .last("warehouse").as("warehouse") .addToSet("id").as("productIds")
	 * .avg("price").as("averagePrice") .sum("price").as("totalRevenue"); }
	 * 
	 * private ProjectionOperation getProjectOperation() { return
	 * project("productIds", "averagePrice",
	 * "totalRevenue").and("warehouse").previousOperation(); }
	 */
	// last: Returns the warehouse of the last document in the group.
	// addToSet: Collects all the unique product Ids of all the grouped documents,
	// resulting in an array.
	// avg: Calculates the average of all prices in the group.
	// sum: Sums all prices in the group.

}
