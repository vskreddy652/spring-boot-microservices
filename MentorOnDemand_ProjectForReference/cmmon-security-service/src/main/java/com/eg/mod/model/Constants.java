package com.eg.mod.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Constants {

	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
	public static final String SIGNING_KEY = "devglan123r";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";

	public static final String SMS_APK_KEY = "A10QG2Yc57I-ntFL1cT4zSEOmIhZYdCiFDvgiGyCC0";

	public static final List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("de"),
			new Locale("it"), new Locale("ja"), new Locale("ko"), new Locale("zh"), new Locale("de"), new Locale("de"));

	public enum UserRole {

		ROLE_ADMIN("ADMIN"), ROLE_MENTOR("MENTOR"), ROLE_USER("USER");
		private final String role;

		private UserRole(String role) {
			this.role = role;
		}

		public String getRole() {
			return role;
		}
	}

	public enum ProposoalStuts {

		ACCEPT("ACCEPT"), REJECT("REJECT");
		private final String status;

		private ProposoalStuts(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
	}

	private static ArrayList<String> inProgress = new ArrayList<String>(5);

	public static ArrayList<String> getInProgress() {
		Constants.inProgress.add(TrainingStatus.PROPOSED.getStatus());
		Constants.inProgress.add(TrainingStatus.CONFIRMED.getStatus());
		Constants.inProgress.add(TrainingStatus.TRAINING_STARTED.getStatus());
		Constants.inProgress.add(TrainingStatus.NOT_COMPLETED.getStatus());
		return inProgress;
	}

	public enum TrainingStatus {

		INPROGRESS("INPROGRESS"), PROPOSED("PROPOSED"), CONFIRMED("CONFIRMED"), REJECTED("REJECTED"), TRAINING_STARTED(
				"TRAINING-STARTED"), NOT_COMPLETED("NOT-COMPLETED"), COMPLETED("COMPLETED");
		private final String status;

		private TrainingStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
	}
}
