package com.eg.mod.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eg.mod.entity.Training;
import com.eg.mod.model.Constants.TrainingStatus;
import com.eg.mod.service.TrainingService;

@Component
public class ScheduledJobs {

	private static Logger logger = LoggerFactory.getLogger(ScheduledJobs.class);

	@Autowired
	private TrainingService trainingService;

	@Scheduled(cron = "${notcompleted_training.job.cron}")
	public synchronized void updateTrainingToNotCompleted() {

		List<String> trainingStatus = new ArrayList<String>();
		trainingStatus.add(TrainingStatus.PROPOSED.getStatus());
		trainingStatus.add(TrainingStatus.CONFIRMED.getStatus());
		trainingStatus.add(TrainingStatus.TRAINING_STARTED.getStatus());

		List<Training> trainingList = trainingService.findExpiredTrainings(trainingStatus);
		logger.info("Number of records found " + trainingList.size());
		for (Training trainingObj : trainingList) {
			trainingService.updateTrainingToNotCompleted(trainingObj);
		}
	}

	@Scheduled(cron = "${training_started.job.cron}")
	public synchronized void updateTrainingToStarted() {

		List<String> trainingStatus = new ArrayList<String>();
		trainingStatus.add(TrainingStatus.CONFIRMED.getStatus());

		List<Training> trainingList = trainingService.findStartedTrainings(trainingStatus);
		logger.info("Number of records found " + trainingList.size());
		for (Training trainingObj : trainingList) {
			if(trainingObj.getAmountReceived().floatValue() != 0.0f)
			    trainingService.updateTrainingToStarted(trainingObj);
		}
	}
	
	
	@Scheduled(cron = "${training_progress.job.cron}")
	public synchronized void updateTrainingProgressJob() {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
		List<String> trainingStatus = new ArrayList<>();
		trainingStatus.add(TrainingStatus.CONFIRMED.getStatus());
		trainingStatus.add(TrainingStatus.TRAINING_STARTED.getStatus());
		List<Training> trainingList = trainingService.findByTrainingStatus(trainingStatus);
		logger.info("Number of records found " + trainingList.size());

		for (Training trainingObj : trainingList) {
			int days, hours, extraHours = 0;
			float progressPercent = 0.0f;
			int progress = 0;
			String message = null;

			try {
				days = ((int) (df.parse(trainingObj.getEndDate()).getTime() - df.parse(trainingObj.getStartDate()).getTime()) / 1000 / 60 / 60 / 24) + 1;				
				hours = (int) (tf.parse(trainingObj.getEndTime()).getTime() - tf.parse(trainingObj.getStartTime()).getTime()) / 1000 / 60 / 60;
				int totalTrainingHours = days * hours;
				logger.info("Total Training Period "+ days + " days and " + hours + " hours per day\nTotal Training Hours " + totalTrainingHours);

				Calendar cal = Calendar.getInstance();
				String curDateStr = df.format(cal.getTime());
				String curTimeStr = tf.format(cal.getTime());
				days = (int) (df.parse(curDateStr).getTime() - df.parse(trainingObj.getStartDate()).getTime()) / 1000 / 60 / 60 / 24;
				if (tf.parse(curTimeStr).getTime() > tf.parse(trainingObj.getEndTime()).getTime())
					extraHours = hours;
				else
					extraHours = (int) (tf.parse(curTimeStr).getTime() - tf.parse(trainingObj.getStartTime()).getTime()) / 1000 / 60 / 60;
				int totalTrainingHoursSpent = (days * hours) + extraHours;
				logger.info("Total Training Hours completed " + totalTrainingHoursSpent);
				
				progressPercent = (totalTrainingHoursSpent * 100) / totalTrainingHours;
				if (progressPercent >= 25.0 && progressPercent < 26.0)
					progress = 25;
				else if (progressPercent >= 50.0 && progressPercent < 51.0)
					progress = 50;
				else if (progressPercent >= 75.0 && progressPercent < 76.0)
					progress = 75;
				else if (progressPercent == 100.0)
					progress = 100;
				logger.info("Training Progress in percentage " + progress);
				if (trainingObj.getProgress().intValue() != progress
						&& (progress == 25 || progress == 50 || progress == 75 || progress == 100)) {
					trainingObj.setProgress(progress);
					message = trainingService.updateTrainingProgress(trainingObj);
					logger.info(message);
				}
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}

		}
	}

	public static void main(String[] args) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
		List<String> trainingStatus = new ArrayList<>();
		trainingStatus.add(TrainingStatus.CONFIRMED.getStatus());
		trainingStatus.add(TrainingStatus.TRAINING_STARTED.getStatus());
		List<Training> trainingList = new ArrayList<>();
		Training trainingObj = new Training();
		trainingObj.setId(1L); 
		trainingObj.setStartDate("01-03-2019");  trainingObj.setStartTime("07:00");
		trainingObj.setEndDate("08-03-2019");  trainingObj.setEndTime("09:00");
		trainingList.add(trainingObj);
		
		for (Training t : trainingList) {
			int days, hours, extraHours = 0;
			float progressPercent = 0.0f;
			int progress = 0;

			try {
				days = ((int) (df.parse(t.getEndDate()).getTime() - df.parse(t.getStartDate()).getTime()) / 1000 / 60 / 60 / 24) + 1;				
				hours = (int) (tf.parse(t.getEndTime()).getTime() - tf.parse(t.getStartTime()).getTime()) / 1000 / 60 / 60;
				int totalTrainingHours = days * hours;
				System.out.println("Total Training Period "+ days + " days and " + hours + " hours per day\nTotal Training Hours " + totalTrainingHours);

				Calendar cal = Calendar.getInstance();
				String curDateStr = df.format(cal.getTime());
				String curTimeStr = tf.format(cal.getTime());
				days = (int) (df.parse(curDateStr).getTime() - df.parse(t.getStartDate()).getTime()) / 1000 / 60 / 60 / 24;
				if (tf.parse(curTimeStr).getTime() > tf.parse(t.getEndTime()).getTime())
					extraHours = hours;
				else
					extraHours = (int) (tf.parse(curTimeStr).getTime() - tf.parse(t.getStartTime()).getTime()) / 1000 / 60 / 60;
				int totalTrainingHoursSpent = (days * hours) + extraHours;
				System.out.println("Total Training Hours completed " + totalTrainingHoursSpent);
				
				progressPercent = (totalTrainingHoursSpent * 100) / totalTrainingHours;
				if (progressPercent >= 25.0 && progressPercent <= 25.0)
					progress = 25;
				else if (progressPercent >= 50.0 && progressPercent <= 50.0)
					progress = 50;
				else if (progressPercent >= 75.0 && progressPercent <= 75.0)
					progress = 75;
				else if (progressPercent == 100.0)
					progress = 100;
				System.out.println("Training Progress in percentage " + progress);
				if (t.getProgress().intValue() != progress && (progress == 25 || progress == 50 || progress == 75 || progress == 100))
                   System.out.println("Update payment option");
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}

		}
		
	}
}
