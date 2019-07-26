package com.eg.mod.reprository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.eg.mod.entity.Payment;
import com.eg.mod.model.PaymentDtls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public PaymentDtls aggregateByMentorId(Long mentorId, Long trainingId) {

		//MatchOperation matchOperation = Aggregation.match(Criteria.where("mentorId").is(mentorId).and("trainingId").is(trainingId));
		//GroupOperation groupOperation = Aggregation.group("mentorId").last("mentorId").as("mentorId").sum("amount").as("totalAmount");
		//ProjectionOperation projectionOperation = Aggregation.project("mentorId", "totalAmount").and("mentorId").previousOperation();
        //System.out.println("matchOperation "+matchOperation.toString());
        //System.out.println("groupOperation "+groupOperation.toString());
        //System.out.println("projectionOperation "+projectionOperation.toString());
		MatchOperation matchOperation = Aggregation.match(Criteria.where("mentorId").is(mentorId).and("trainingId").is(trainingId));
		GroupOperation groupOperation = Aggregation.group("mentorId", "trainingId").last("mentorId").as("mentorId")
				.last("trainingId").as("trainingId").sum("amount").as("totalAmount");
		ProjectionOperation projectionOperation = Aggregation.project("mentorId", "trainingId", "totalAmount");
		System.out.println("matchOperation "+matchOperation.toString());
        System.out.println("groupOperation "+groupOperation.toString());
        System.out.println("projectionOperation "+projectionOperation.toString());
		
		PaymentDtls obj = mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation),
				Payment.class, PaymentDtls.class).getUniqueMappedResult();

		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(obj);
		    System.out.println("JSON = " + json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return obj;
	}

	
	/*
	 * @Override
    public List<WarehouseSummary> aggregate(float minPrice, float maxPrice) {
        MatchOperation matchOperation = getMatchOperation(minPrice, maxPrice);
        GroupOperation groupOperation = getGroupOperation();
        ProjectionOperation projectionOperation = getProjectOperation();

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                matchOperation,
                groupOperation,
                projectionOperation
        ), Product.class, WarehouseSummary.class).getMappedResults();
    }
    
	 * private MatchOperation getMatchOperation(float minPrice, float maxPrice) {
        Criteria priceCriteria = where("price").gt(minPrice).andOperator(where("price").lt(maxPrice));
        return match(priceCriteria);
    }

    private GroupOperation getGroupOperation() {
        return group("warehouse")
                .last("warehouse").as("warehouse")
                .addToSet("id").as("productIds")
                .avg("price").as("averagePrice")
                .sum("price").as("totalRevenue");
    }

    private ProjectionOperation getProjectOperation() {
        return project("productIds", "averagePrice", "totalRevenue").and("warehouse").previousOperation();
    }
	 */
	// last: Returns the warehouse of the last document in the group.
	// addToSet: Collects all the unique product Ids of all the grouped documents,
	// resulting in an array.
	// avg: Calculates the average of all prices in the group.
	// sum: Sums all prices in the group.
	

}
