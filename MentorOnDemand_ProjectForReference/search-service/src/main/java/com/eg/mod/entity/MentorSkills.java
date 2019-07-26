package com.eg.mod.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mentor_skills")
public class MentorSkills extends AuditModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "self_rating", nullable = false)
	private Integer selfRating;

	@Column(name = "years_of_experience", nullable = false)
	private Float yearsOfExperience;

	@Column(name = "tarinings_delivered", nullable = true)
	private Integer tariningsDelivered = 0;

	@Column(name = "facilities_offered", nullable = false)
	private String facilitiesOffered;

	@Column(name = "skill_id", nullable = false)
	private Long skillId;

	@Column(name = "mentor_id", nullable = false)
	private Long mentorId;

	@Column(name = "fees", nullable = false)
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

	public Long getMentorId() {
		return mentorId;
	}

	public void setMentorId(Long mentorId) {
		this.mentorId = mentorId;
	}

	public Float getFees() {
		return fees;
	}

	public void setFees(Float fees) {
		this.fees = fees;
	}

}
