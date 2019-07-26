package com.eg.mod.model;

public class MentorSkillsDtls extends AuditModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer selfRating;
	private Float avgRating = 0.0f;
	private Float yearsOfExperience;
	private Integer tariningsDelivered;
	private String facilitiesOffered;
	private Long skillId;
	private String skillName;
	private Long mentorId;
	private String mentorName;
	private Float fees;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSelfRating() {
		return selfRating;
	}

	public void setSelfRating(Integer selfRating) {
		this.selfRating = selfRating;
	}

	public Float getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(Float avgRating) {
		this.avgRating = avgRating;
	}

	public Float getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(Float yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public Integer getTariningsDelivered() {
		return tariningsDelivered;
	}

	public void setTariningsDelivered(Integer tariningsDelivered) {
		this.tariningsDelivered = tariningsDelivered;
	}

	public String getFacilitiesOffered() {
		return facilitiesOffered;
	}

	public void setFacilitiesOffered(String facilitiesOffered) {
		this.facilitiesOffered = facilitiesOffered;
	}

	public Long getSkillId() {
		return skillId;
	}

	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public Long getMentorId() {
		return mentorId;
	}

	public void setMentorId(Long mentorId) {
		this.mentorId = mentorId;
	}

	public String getMentorName() {
		return mentorName;
	}

	public void setMentorName(String mentorName) {
		this.mentorName = mentorName;
	}

	public Float getFees() {
		return fees;
	}

	public void setFees(Float fees) {
		this.fees = fees;
	}

}
