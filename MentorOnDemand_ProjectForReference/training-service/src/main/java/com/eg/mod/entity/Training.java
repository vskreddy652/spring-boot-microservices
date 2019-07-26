package com.eg.mod.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.eg.mod.model.Constants.TrainingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author ibm
 *
 */
@Document(collection = "trainings")
public class Training extends AuditModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	public static final String SEQUENCE_NAME = "training_sequence";

	@Id
	@Field(value = "_id")
	private Long id;

	@Field(value = "status")
	private String status = TrainingStatus.PROPOSED.getStatus();

	@Field(value = "progress")
	private Integer progress = 0;

	@Field(value = "fees")
	private Float fees = 0.0f;

	@Field(value = "commission_amount")
	private Float commissionAmount = 0.0f;

	@Field(value = "rating")
	private Integer rating = 0;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Field(value = "start_date")
	private String startDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Field(value = "end_date")
	private String endDate;

	@JsonFormat(pattern = "HH:mm:ss")
	@Field(value = "start_time")
	private String startTime;

	@JsonFormat(pattern = "HH:mm:ss")
	@Field(value = "end_time")
	private String endTime;

	@Field(value = "amount_received")
	private Float amountReceived = 0.0f;

	@Field(value = "user_id")
	private Long userId;

	@Field(value = "mentor_id")
	private Long mentorId;

	@Field(value = "skill_id")
	private Long skillId;

	@Field(value = "razorpay_payment_id")
	private String razorpayPaymentId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Float getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(Float amountReceived) {
		this.amountReceived = amountReceived;
	}

	public Float getFees() {
		return fees;
	}

	public void setFees(Float fees) {
		this.fees = fees;
	}

	public Float getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(Float commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMentorId() {
		return mentorId;
	}

	public void setMentorId(Long mentorId) {
		this.mentorId = mentorId;
	}

	public Long getSkillId() {
		return skillId;
	}

	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}

	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}

	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}

}
