package com.eg.mod.entity;

import java.util.Date;

import javax.validation.constraints.Positive;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User extends AuditModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	public static final String SEQUENCE_NAME = "users_sequence";

	@Id
	@Field(value = "_id")
	private Long id;

	// @NotNull(message = "User Name is compulsory")
	// @NotBlank(message = "User Name is compulsory")
	// @Email(message = "User Name should be email id")
	@Field(value = "user_name")
	private String userName;

	// @Min(value = 6, message="Password should be 6 character long")
	@Field(value = "password")
	private String password;

	// @NotNull(message = "First Name is compulsory")
	// @NotBlank(message = "First Name is compulsory")
	// @Pattern(regexp = "[a-z-A-Z]*", message = "First Name has invalid
	// characters")
	@Field(value = "first_name")
	private String firstName;

	// @NotNull(message = "Last Name is compulsory")
	// @NotBlank(message = "Last Name is compulsory")
	// @Pattern(regexp = "[a-z-A-Z]*", message = "Last Name has invalid characters")
	@Field(value = "last_name")
	private String lastName;

	@Positive(message = "Contact Number should be positive value")
	// @Length(min=10, max=10, message="Contact Number should be 10 digit long")
	@Field(value = "contact_number")
	private Long contactNumber;

	@Field(value = "reg_code")
	private String regCode = "";

	@Field(value = "role")
	private String role = null;

	@Field(value = "linkedin_url")
	private String linkedinUrl = null;

	@Field(value = "years_of_experience")
	private Float yearsOfExperience;

	@Field(value = "active")
	private Boolean active = false;

	@Field(value = "confirmed_signup")
	private Boolean confirmedSignUp = false;

	@Field(value = "reset_password")
	private Boolean resetPassword = false;
	
	@Field(value = "reset_password_date")
	private Date resetPasswordDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getRegCode() {
		return regCode;
	}

	public void setRegCode(String regCode) {
		this.regCode = regCode;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getLinkedinUrl() {
		return linkedinUrl;
	}

	public void setLinkedinUrl(String linkedinUrl) {
		this.linkedinUrl = linkedinUrl;
	}

	public Float getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(Float yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getConfirmedSignUp() {
		return confirmedSignUp;
	}

	public void setConfirmedSignUp(Boolean confirmedSignUp) {
		this.confirmedSignUp = confirmedSignUp;
	}

	public Date getResetPasswordDate() {
		return resetPasswordDate;
	}

	public void setResetPasswordDate(Date resetPasswordDate) {
		this.resetPasswordDate = resetPasswordDate;
	}

	public Boolean getResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(Boolean resetPassword) {
		this.resetPassword = resetPassword;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", contactNumber=" + contactNumber + ", regCode=" + regCode + ", role="
				+ role + ", linkedinUrl=" + linkedinUrl + ", yearsOfExperience=" + yearsOfExperience + ", active="
				+ active + ", confirmedSignUp=" + confirmedSignUp + ", resetPasswordDate=" + resetPasswordDate
				+ ", resetPassword=" + resetPassword + "]";
	}

}
