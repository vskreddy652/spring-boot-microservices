package com.eg.mod.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eg.mod.entity.Training;
import com.eg.mod.exception.DataValidationException;
import com.eg.mod.exception.ResourceExistException;
import com.eg.mod.exception.ResourceNotFoundException;
import com.eg.mod.exception.ServiceUnavailableException;
import com.eg.mod.model.ApiResponse;
import com.eg.mod.model.Constants;
import com.eg.mod.model.PaymentCommDtls;
import com.eg.mod.model.PaymentDtls;
import com.eg.mod.model.SkillDtls;
import com.eg.mod.model.TrainingDtls;
import com.eg.mod.model.UserDtls;
import com.eg.mod.model.Constants.TrainingStatus;
import com.eg.mod.proxy.PaymentServiceProxy;
import com.eg.mod.proxy.SearchServiceProxy;
import com.eg.mod.proxy.SkillServiceProxy;
import com.eg.mod.proxy.UserServiceProxy;
import com.eg.mod.reprository.TrainingsRepository;
import com.eg.mod.util.Translator;

@Service
@Transactional(readOnly = true)
public class TrainingService {

	private static Logger logger = LoggerFactory.getLogger(TrainingService.class);
	
	@Autowired
	private TrainingsRepository trainingRepository;

	@Autowired
	private SequenceGeneratorService sequencegenerator;

	@Autowired
	private UserServiceProxy userProxy;

	@Autowired
	private SearchServiceProxy searchProxy;

	@Autowired
	private SkillServiceProxy skillProxy;

	@Autowired
	private PaymentServiceProxy paymentProxy;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SMSService smsService;
	
	@SuppressWarnings("deprecation")
	/*@HystrixCommand(fallbackMethod = "fallback_findAllTrainings", commandKey = "findAllTrainings", groupKey = "findAllTrainings", ignoreExceptions = ServiceUnavailableException.class)
	//@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")*/
	public Page<TrainingDtls> findAllTrainings(String orderBy, String direction, int page, int size) {

		List<TrainingDtls> trainingDtlsList = new ArrayList<>();
		TrainingDtls trainingDtlObj = null;
		UserDtls user = null, mentor = null;
		SkillDtls skill = null;
		Sort sort = null;
		if (direction.equals("ASC")) {
			sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
		} else if (direction.equals("DESC")) {
			sort = new Sort(new Sort.Order(Direction.DESC, orderBy));
		}
		Pageable pageable = new PageRequest(page, size, sort);

		List<Training> trainingList = trainingRepository.findAll(pageable).getContent();
		for (Training trainingObj : trainingList) {
			user = userProxy.findById(trainingObj.getUserId());
			mentor = userProxy.findById(trainingObj.getMentorId());
			skill = skillProxy.findById(trainingObj.getSkillId());
			if (user != null && mentor != null && skill != null) {
				trainingDtlObj = new TrainingDtls();
				BeanUtils.copyProperties(trainingObj, trainingDtlObj);
				trainingDtlObj.setUserName(user.getFirstName() + " " + user.getLastName());
				trainingDtlObj.setMentorName(mentor.getFirstName() + " " + mentor.getLastName());
				trainingDtlObj.setSkillName(skill.getName());
				trainingDtlsList.add(trainingDtlObj);
			}
		}
		return new PageImpl<>(trainingDtlsList, pageable, trainingDtlsList.size());
	}

	@SuppressWarnings("deprecation")
	/*@HystrixCommand(fallbackMethod = "fallback_findProposedTrainings", commandKey = "findProposedTrainings", groupKey = "findProposedTrainings", ignoreExceptions = ServiceUnavailableException.class)
	//@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")*/
	public Page<TrainingDtls> findProposedTrainings(Long mentorId, String orderBy, String direction, int page,
			int size) {

		List<TrainingDtls> trainingDtlsList = new ArrayList<>();
		TrainingDtls trainingDtlObj = null;
		UserDtls user = null;
		SkillDtls skill = null;
		Sort sort = null;
		if (direction.equals("ASC")) {
			sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
		} else if (direction.equals("DESC")) {
			sort = new Sort(new Sort.Order(Direction.DESC, orderBy));
		}
		Pageable pageable = new PageRequest(page, size, sort);
		List<Training> trainingList = trainingRepository.findProposedTrainings(mentorId, pageable).getContent();
		
		for (Training trainingObj : trainingList) {
			user = userProxy.findById(trainingObj.getUserId());
			skill = skillProxy.findById(trainingObj.getSkillId());
			if (user != null && skill != null) {
				trainingDtlObj = new TrainingDtls();
				BeanUtils.copyProperties(trainingObj, trainingDtlObj);
				trainingDtlObj.setUserName(user.getFirstName() + " " + user.getLastName());
				trainingDtlObj.setSkillName(skill.getName());
				trainingDtlsList.add(trainingDtlObj);
			}
		}
		return new PageImpl<>(trainingDtlsList, pageable, trainingDtlsList.size());
	}

	@SuppressWarnings("deprecation")
	/*@HystrixCommand(fallbackMethod = "fallback_findByMentorIdSkillId", commandKey = "findByMentorIdSkillId", groupKey = "findByMentorIdSkillId", ignoreExceptions = ServiceUnavailableException.class)
	//@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")*/
	public Page<TrainingDtls> findByMentorIdSkillId(Long mentorId, Long skillId, String orderBy, String direction, int page,
			int size) {

		List<TrainingDtls> trainingDtlsList = new ArrayList<>();
		TrainingDtls trainingDtlObj = null;
		UserDtls user = null;
		SkillDtls skill = null;
		Sort sort = null;
		if (direction.equals("ASC")) {
			sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
		} else if (direction.equals("DESC")) {
			sort = new Sort(new Sort.Order(Direction.DESC, orderBy));
		}
		Pageable pageable = new PageRequest(page, size, sort);

		List<Training> trainingList = trainingRepository.findByMentorIdSkillId(mentorId, skillId, pageable).getContent();
		for (Training trainingObj : trainingList) {
			user = userProxy.findById(trainingObj.getUserId());
			skill = skillProxy.findById(trainingObj.getSkillId());
			if (user != null && skill != null) {
				trainingDtlObj = new TrainingDtls();
				BeanUtils.copyProperties(trainingObj, trainingDtlObj);
				trainingDtlObj.setUserName(user.getFirstName() + " " + user.getLastName());
				trainingDtlObj.setSkillName(skill.getName());
				trainingDtlsList.add(trainingDtlObj);
			}
		}
		return new PageImpl<>(trainingDtlsList, pageable, trainingDtlsList.size());
	}

	@SuppressWarnings("deprecation")
	/*@HystrixCommand(fallbackMethod = "fallback_findByUserIdAndStatus", commandKey = "findByUserIdAndStatus", groupKey = "findByUserIdAndStatus", ignoreExceptions = ServiceUnavailableException.class)
	//@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")*/
	public Page<TrainingDtls> findByUserIdAndStatus(Long userId, List<String> trainingStatus, String orderBy,
			String direction, int page, int size) {

		List<TrainingDtls> trainingDtlsList = new ArrayList<>();
		TrainingDtls trainingDtlObj = null;
		UserDtls mentor = null;
		SkillDtls skill = null;
		Sort sort = null;
		if (direction.equals("ASC")) {
			sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
		} else if (direction.equals("DESC")) {
			sort = new Sort(new Sort.Order(Direction.DESC, orderBy));
		}
		Pageable pageable = new PageRequest(page, size, sort);

		List<Training> trainingList = trainingRepository.findByUserIdAndStatus(userId, trainingStatus, pageable).getContent();
		for (Training trainingObj : trainingList) {
			mentor = userProxy.findById(trainingObj.getMentorId());
			skill = skillProxy.findById(trainingObj.getSkillId());
			if (mentor != null && skill != null) {
				trainingDtlObj = new TrainingDtls();
				BeanUtils.copyProperties(trainingObj, trainingDtlObj);
				trainingDtlObj.setMentorName(mentor.getFirstName() + " " + mentor.getLastName());
				trainingDtlObj.setSkillName(skill.getName());
				trainingDtlsList.add(trainingDtlObj);
			}
		}

		return new PageImpl<>(trainingDtlsList, pageable, trainingDtlsList.size());
	}

	@SuppressWarnings("deprecation")
	/*@HystrixCommand(fallbackMethod = "fallback_findByMentorIdAndStatus", commandKey = "findByMentorIdAndStatus", groupKey = "findByMentorIdAndStatus", ignoreExceptions = ServiceUnavailableException.class)
	//@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")*/
	public Page<TrainingDtls> findByMentorIdAndStatus(Long mentorId, List<String> trainingStatus, String orderBy,
			String direction, int page, int size) {

		List<TrainingDtls> trainingDtlsList = new ArrayList<>();
		TrainingDtls trainingDtlObj = null;
		UserDtls user = null;
		SkillDtls skill = null;
		Sort sort = null;
		if (direction.equals("ASC")) {
			sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
		} else if (direction.equals("DESC")) {
			sort = new Sort(new Sort.Order(Direction.DESC, orderBy));
		}
		Pageable pageable = new PageRequest(page, size, sort);

		List<Training> trainingList = trainingRepository.findByMentorIdAndStatus(mentorId, trainingStatus, pageable).getContent();
		for (Training trainingObj : trainingList) {
			user = userProxy.findById(trainingObj.getUserId());
			skill = skillProxy.findById(trainingObj.getSkillId());
			if (user != null && skill != null) {
				trainingDtlObj = new TrainingDtls();
				BeanUtils.copyProperties(trainingObj, trainingDtlObj);
				trainingDtlObj.setUserName(user.getFirstName() + " " + user.getLastName());
				trainingDtlObj.setSkillName(skill.getName());
				trainingDtlsList.add(trainingDtlObj);
			}
		}

		return new PageImpl<>(trainingDtlsList, pageable, trainingDtlsList.size());
	}
	
	//@HystrixCommand(fallbackMethod = "fallback_findAvgRating", commandKey = "findAvgRating", groupKey = "findAvgRating")
	public TrainingDtls findAvgRating(Long mentorId, Long skillId) {	
		
		return trainingRepository.findAvgRating(mentorId, skillId);
	}
	
	/*@HystrixCommand(fallbackMethod = "fallback_findById", commandKey = "findById", groupKey = "findById", ignoreExceptions = {
			ResourceNotFoundException.class })*/
	public Training findById(Long id) {
		return Optional.ofNullable(trainingRepository.findById(id).get())
				.orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.trainingId", id)));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	/*@HystrixCommand(fallbackMethod = "fallback_proposeTraining", commandKey = "proposeTraining", groupKey = "proposeTraining", ignoreExceptions = {
			ResourceNotFoundException.class, ResourceExistException.class, ServiceUnavailableException.class })*/
	//@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")
	public Training proposeTraining(Training training) {

		UserDtls user = userProxy.findById(training.getUserId());
		UserDtls mentor = userProxy.findById(training.getMentorId());
		SkillDtls skill = skillProxy.findById(training.getSkillId());

		if (user == null)
			throw new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.userId", training.getUserId()));
		else if (mentor == null)
			throw new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.mentorId", training.getMentorId()));
		else if (skill == null)
			throw new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.skillId", training.getSkillId()));
		else {
			Training OldTraining = trainingRepository.findRegisteredTraining(training.getUserId(), training.getMentorId(), training.getSkillId(),
					training.getStartDate(), training.getEndDate(), training.getStartTime(), training.getEndTime());
			if (OldTraining != null)
				throw new ResourceExistException(
						Translator.toLocale("error.resource.found.proposal", skill.getName(), training.getStartDate(),
								training.getEndDate(), training.getStartTime(), training.getEndTime()));
			if (skill.getName().length() > 0 && mentor.getFirstName().length() > 0) {
				training.setId(sequencegenerator.generateSequence(Training.SEQUENCE_NAME));
				training.setPersistent(true);
				trainingRepository.save(training);

				String mailSubject = Translator.toLocale("email.subject.proposal", skill.getName());
				String mailBody = Translator.toLocale("email.body.proposal", mentor.getFirstName(), skill.getName(),
						training.getStartDate(), training.getEndDate(), training.getStartTime(), training.getEndTime());
				emailService.sendMail(user.getUserName(), mentor.getUserName(), mailSubject, mailBody);

				String smsText = Translator.toLocale("sms.training.proposal", user.getFirstName(), skill.getName());
				smsService.sendSms(smsText, user.getContactNumber() + "", mentor.getContactNumber() + "");
			}
			return training;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	/*@HystrixCommand(fallbackMethod = "fallback_updateTraining", commandKey = "updateTraining", groupKey = "updateTraining", ignoreExceptions = {
			ResourceNotFoundException.class, DataValidationException.class, ServiceUnavailableException.class })*/
	//@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")
	public Training updateTraining(Training training, String authToken) {

		Training oldTraining = trainingRepository.findById(training.getId()).get();
		if (oldTraining == null)
			throw new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.trainingId", training.getId()));

		UserDtls user = userProxy.findById(oldTraining.getUserId());
		UserDtls mentor = userProxy.findById(oldTraining.getMentorId());
		SkillDtls skill = skillProxy.findById(oldTraining.getSkillId());

		if(user == null || mentor == null || skill == null)
			throw new ServiceUnavailableException(Translator.toLocale("error.service.unavailable", "user-service or skill-service"));
		
		boolean updateFlag = false;
        boolean mailFlag = false;
        
		// sent mail notification to ACCEPT/REJECT proposal by mentor
		if (oldTraining.getStatus().equals(TrainingStatus.PROPOSED.getStatus())
				&& (training.getStatus().equals(TrainingStatus.CONFIRMED.getStatus())
						|| training.getStatus().equals(TrainingStatus.REJECTED.getStatus()))) {
			mailFlag = true;

			if (training.getStatus().equals(TrainingStatus.CONFIRMED.getStatus())
					|| training.getStatus().equals(TrainingStatus.REJECTED.getStatus())) {
				oldTraining.setStatus(training.getStatus());
				updateFlag = true;
			}
			if (training.getStatus().equals(TrainingStatus.CONFIRMED.getStatus())) {
				ApiResponse<?> response = searchProxy.addCalendarEntry(authToken, oldTraining.getMentorId(),
						oldTraining.getSkillId(), oldTraining.getStartDate(), oldTraining.getEndDate(),
						oldTraining.getStartTime(), oldTraining.getEndTime());
				if (response.getStatus() == 200) {
					response = searchProxy.updateMentorTrainingCount(authToken, oldTraining.getMentorId(), oldTraining.getSkillId());
					if (response.getStatus() == 200)
						updateFlag = true;
					else
						throw new ServiceUnavailableException(response.getMessage());
				} else
					throw new ServiceUnavailableException(response.getMessage());
			}
		} else if ((training.getStatus().equals(TrainingStatus.TRAINING_STARTED.getStatus())
				|| training.getStatus().equals(TrainingStatus.COMPLETED.getStatus()))
				&& !oldTraining.getStatus().equals(training.getStatus())) {	
			if(oldTraining.getAmountReceived() == 0.0f)
				throw new DataValidationException(Translator.toLocale("error.validate.statusChange"));
			if(!oldTraining.getStatus().equals(TrainingStatus.TRAINING_STARTED.getStatus()))
				throw new DataValidationException(Translator.toLocale("error.validate.statusOldstatus"));
			if (training.getStatus().equals(TrainingStatus.TRAINING_STARTED.getStatus())) {
				oldTraining.setStatus(training.getStatus());
				updateFlag = true;
			}else {
				oldTraining.setProgress(100);
				logger.info(updateTrainingProgress(oldTraining));
			}
		} else if (training.getAmountReceived() != 0.0f) { // training amount validation
			if (!oldTraining.getStatus().equals(TrainingStatus.CONFIRMED.getStatus()))
				throw new DataValidationException(
						Translator.toLocale("error.validate.trainingStatus", TrainingStatus.TRAINING_STARTED.getStatus()));			
			PaymentCommDtls paymtComm = paymentProxy.findPaymentCommission(new Long(1));
			if (paymtComm == null || (paymtComm != null && paymtComm.getCommissionPercent().intValue() == 0))
				 throw new ServiceUnavailableException(Translator.toLocale("error.service.unavailable", "payment-service"));
			if (paymtComm.getCommissionPercent().intValue() != 0) {
				float commission = oldTraining.getFees() * ((float) paymtComm.getCommissionPercent() / 100);
				DecimalFormat df = new DecimalFormat("#.##"); 
				float truncCommission = Float.valueOf(df.format(commission));
				System.out.println("truncCommission "+truncCommission);
				oldTraining.setAmountReceived(training.getAmountReceived());
				oldTraining.setRazorpayPaymentId(training.getRazorpayPaymentId());
				oldTraining.setCommissionAmount(truncCommission);
				updateFlag = true;
			}
		} else if (training.getRating() != 0.0f) {
			if (!Constants.getInProgress().contains(oldTraining.getStatus()))
				throw new DataValidationException(Translator.toLocale("error.validate.rating", Constants.getInProgress().toString()));
			oldTraining.setRating(training.getRating());
			updateFlag = true;
		} else if (training.getStatus() != null && Constants.getInProgress().contains(training.getStatus())) {
			oldTraining.setStatus(training.getStatus());
			updateFlag = true;
		}

		if (updateFlag) {
			trainingRepository.save(oldTraining);
			if (mailFlag && skill.getName().length() > 0 && user.getFirstName().length() > 0) {
				String mailSubject = Translator.toLocale("email.subject.approve", skill.getName());
				String mailBody = Translator.toLocale("email.body.approve", user.getFirstName(), skill.getName(),
						oldTraining.getStartDate(), oldTraining.getEndDate(), oldTraining.getStartTime(),
						oldTraining.getEndTime(), training.getStatus());
				emailService.sendMail(mentor.getUserName(), user.getUserName(), mailSubject, mailBody);
				
				String smsText = Translator.toLocale("sms.training.approve", mentor.getFirstName()+" "+mentor.getLastName(), training.getStatus(), skill.getName());
				smsService.sendSms(smsText, mentor.getContactNumber() + "", user.getContactNumber() + "");
			}
		}

		return oldTraining;

	}

	@Transactional(propagation = Propagation.REQUIRED)
	/*@HystrixCommand(fallbackMethod = "fallback_deleteTraining", commandKey = "deleteTraining", groupKey = "deleteTraining", ignoreExceptions = {
			ResourceNotFoundException.class })*/
	public void deleteTraining(Long id) {

		trainingRepository.findById(id).map(training -> {
			trainingRepository.delete(training);
			return true;
		}).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.trainingId", id)));
	}

	//@HystrixCommand(fallbackMethod = "fallback_findExpiredTrainings", commandKey = "findExpiredTrainings", groupKey = "findExpiredTrainings")
	public List<Training> findExpiredTrainings(List<String> trainingStatus) {
		return trainingRepository.findExpiredTrainings(trainingStatus);
	}

	public List<Training> findStartedTrainings(List<String> trainingStatus) {
		return trainingRepository.findStartedTrainings(trainingStatus);
	}
	
	//@HystrixCommand(fallbackMethod = "fallback_findByTrainingStatus", commandKey = "findByTrainingStatus", groupKey = "findByTrainingStatus")
	public List<Training> findByTrainingStatus(List<String> trainingStatus) {
		
		return trainingRepository.findByTrainingStatus(trainingStatus);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_updateTrainingToNotCompleted", commandKey = "updateTrainingToNotCompleted", groupKey = "updateTrainingToNotCompleted", ignoreExceptions = { ResourceNotFoundException.class })
	public void updateTrainingToNotCompleted(Training trainingObj) {

		trainingObj.setStatus(TrainingStatus.NOT_COMPLETED.getStatus());
		trainingRepository.save(trainingObj);
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_updateTrainingToStarted", commandKey = "updateTrainingToNotCompleted", groupKey = "updateTrainingToNotCompleted", ignoreExceptions = { ResourceNotFoundException.class })
	public void updateTrainingToStarted(Training trainingObj) {

		trainingObj.setStatus(TrainingStatus.TRAINING_STARTED.getStatus());
		trainingRepository.save(trainingObj);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	/*@HystrixCommand(fallbackMethod = "fallback_updateTrainingProgress", commandKey = "updateTrainingProgress", groupKey = "updateTrainingProgress")
	//@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")*/
	public String updateTrainingProgress(Training trainingObj) {
	
		float totalPaidAmtToMentor = paymentProxy.findTotalPaidAmountByMentorId(trainingObj.getMentorId(), trainingObj.getId()).getTotalAmountToMentor();
		float amtToMentor = (trainingObj.getAmountReceived() - trainingObj.getCommissionAmount()) * ((float) trainingObj.getProgress() / 100);
		if(totalPaidAmtToMentor == amtToMentor)
			return "Total Paid Amount " + totalPaidAmtToMentor + " is equal with Amount needs to pay " + amtToMentor;
		
		if (trainingObj.getProgress().equals(100))
			trainingObj.setStatus(TrainingStatus.COMPLETED.getStatus());

		// add payment information
		PaymentDtls payment = new PaymentDtls();
		payment.setTxnType("CR");
		Float amountToMentor = amtToMentor - totalPaidAmtToMentor;
		payment.setAmount(amountToMentor);
		payment.setRemarks(amountToMentor + " paid to mentor");
		payment.setMentorId(trainingObj.getMentorId());
		payment.setTrainingId(trainingObj.getId());
		ApiResponse<?> response = paymentProxy.addPayment(payment);
		if (response.getStatus() != 200)
			throw new ServiceUnavailableException(response.getMessage());
		trainingRepository.save(trainingObj);
		return "Amount paid to mentor is " + amtToMentor;
	}

	
	// list of fallback method for @HystrixCommand
	public Page<TrainingDtls> fallback_findAllTrainings(String orderBy, String direction, int page, int size) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public Page<TrainingDtls> fallback_findProposedTrainings(Long mentorId, String orderBy, String direction, int page,
			int size) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public Page<TrainingDtls> fallback_findByMentorIdSkillId(Long mentorId, Long skillId, String orderBy,
			String direction, int page, int size) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public Page<TrainingDtls> fallback_findByUserIdAndStatus(Long userId, List<String> trainingStatus, String orderBy,
			String direction, int page, int size) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public Page<TrainingDtls> fallback_findByMentorIdAndStatus(Long mentorId, List<String> trainingStatus,
			String orderBy, String direction, int page, int size) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public TrainingDtls fallback_findAvgRating(Long mentorId, Long skillId) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public Training fallback_findById(Long id) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public Training fallback_proposeTraining(Training training) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public Training fallback_updateTraining(Long id, Training training, String authToken) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public void fallback_deleteTraining(Long id) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public List<Training> fallback_findExpiredTrainings(List<String> trainingStatus) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public List<Training> fallback_findByTrainingStatus(List<String> trainingStatus) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public void fallback_updateTrainingToNotCompleted(Long id) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

	public void fallback_updateTrainingProgress(Long id, int progress) {
		throw new ServiceUnavailableException(Translator.toLocale("error.training.service"));
	}

}
