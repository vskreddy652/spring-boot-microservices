package com.eg.mod.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eg.mod.entity.Payment;
import com.eg.mod.entity.PaymentCommission;
import com.eg.mod.exception.ResourceNotFoundException;
import com.eg.mod.exception.ServiceUnavailableException;
import com.eg.mod.model.PaymentDtls;
import com.eg.mod.model.TrainingDtls;
import com.eg.mod.model.UserDtls;
import com.eg.mod.proxy.SkillServiceProxy;
import com.eg.mod.proxy.TrainingServiceProxy;
import com.eg.mod.proxy.UserServiceProxy;
import com.eg.mod.reprository.PaymentCommissionRepository;
import com.eg.mod.reprository.PaymentRepository;
import com.eg.mod.util.Translator;

@Service(value = "paymentService")
@Transactional(readOnly = true)
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private PaymentCommissionRepository paymentCommissionRepository;
	
	@Autowired
	private SequenceGeneratorService sequencegenerator;

	@Autowired
	private UserServiceProxy userProxy;

	@Autowired
	private TrainingServiceProxy trainingProxy;

	@Autowired
	private SkillServiceProxy skillProxy;

	@SuppressWarnings("deprecation")
	//@HystrixCommand(fallbackMethod = "fallback_findPaymentDtlsByDateRange", commandKey = "findPaymentDtlsByDateRange", groupKey = "findPaymentDtlsByDateRange", ignoreExceptions = ServiceUnavailableException.class)
	//@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")
	public Page<PaymentDtls> findPaymentDtlsByDateRange(Long mentorId, String startDateStr, String endDateStr, String orderBy, String direction, int page, int size, String authToken) {

		Date startDate = null, endDate = null;
		List<PaymentDtls> paymentDtlsList = new ArrayList<>();
		PaymentDtls paymentDtlsObj = null;
		UserDtls mentor = null;
		TrainingDtls trainingDtls = null;
		Page<Payment> paymentDtlList = null;
		Sort sort = null;
		if (direction.equals("ASC")) {
			sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
		} else if (direction.equals("DESC")) {
			sort = new Sort(new Sort.Order(Direction.DESC, orderBy));
		}
		Pageable pageable = new PageRequest(page, size, sort);
		
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(startDateStr);
			endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(endDateStr);
		} catch (Exception e) {
		}
		if(startDate == null || endDate == null)
			return new PageImpl<>(paymentDtlsList, pageable, paymentDtlsList.size());
		
		if (mentorId.longValue() != 0)
			paymentDtlList = paymentRepository.findPaymentDtlsByMentorid(mentorId, startDate, endDate, pageable);
		else
			paymentDtlList = paymentRepository.findPaymentDtlsByDateRange(startDate, endDate, pageable);
		List<Payment> paymentList = paymentDtlList.getContent();
		
		for (Payment paymentObj : paymentList) {
			mentor = userProxy.findById(paymentObj.getMentorId());
			trainingDtls = trainingProxy.findById(authToken, paymentObj.getTrainingId());
			if(mentor != null && trainingDtls != null) {
			paymentDtlsObj = new PaymentDtls();
				BeanUtils.copyProperties(paymentObj, paymentDtlsObj);
				paymentDtlsObj.setMentorName(mentor.getFirstName() + " " + mentor.getLastName());
				paymentDtlsObj.setSkillName(skillProxy.findById(trainingDtls.getSkillId()).getName());
				paymentDtlsList.add(paymentDtlsObj);
			}
		}
		
		return new PageImpl<>(paymentDtlsList, pageable, paymentDtlsList.size());
	}

	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_addPayment", commandKey = "addPayment", groupKey = "addPayment")
	public Payment addPayment(Payment payment) {

		payment.setId(sequencegenerator.generateSequence(Payment.SEQUENCE_NAME));
		payment.setPersistent(true);
		paymentRepository.save(payment);
		return payment;

	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_updatePayment", commandKey = "updatePayment", groupKey = "updatePayment", ignoreExceptions = { ResourceNotFoundException.class })
	public Payment updatePayment(Payment payment) {

		return paymentRepository.findById(payment.getId()).map(oldPayment -> {
			if (payment.getTxnType() != null)
				oldPayment.setTxnType(payment.getTxnType());
			if(payment.getRazorpayPaymentId() != null)
				oldPayment.setRazorpayPaymentId(payment.getRazorpayPaymentId());
			if (payment.getAmount() != null)
				oldPayment.setAmount(payment.getAmount());
			if (payment.getRemarks() != null)
				oldPayment.setRemarks(payment.getRemarks());
			return paymentRepository.save(oldPayment);
		}).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.pmtId", payment.getId())));

	}
	
	//@HystrixCommand(fallbackMethod = "fallback_findTotalPaidAmountByMentorId", commandKey = "findTotalPaidAmountByMentorId", groupKey = "findTotalPaidAmountByMentorId")
	public PaymentDtls findTotalPaidAmountByMentorId(Long mentorId, Long trainingId) {
		
		PaymentDtls paymentDtls = paymentRepository.aggregateByMentorId(mentorId, trainingId);
		if(paymentDtls == null) {
			paymentDtls = new PaymentDtls();
			paymentDtls.setMentorId(mentorId);
			paymentDtls.setTrainingId(trainingId);
		}
		return paymentDtls;
	}
	
	//@HystrixCommand(fallbackMethod = "fallback_findPaymentCommission", commandKey = "findPaymentCommission", groupKey = "findPaymentCommission", ignoreExceptions = { ResourceNotFoundException.class })
	@Cacheable(value= "paymentCommCache", key= "#id")
    public PaymentCommission findPaymentCommission(Long id) {
		
    	return Optional.ofNullable(paymentCommissionRepository.findById(id).get())
				.orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.pmtCommId", id)));
	
	}
	
	//@HystrixCommand(fallbackMethod = "fallback_updatePaymentCommission", commandKey = "updatePaymentCommission", groupKey = "updatePaymentCommission", ignoreExceptions = { ResourceNotFoundException.class })
	@Caching(
			put = { @CachePut(value = "paymentCommCache", key = "#paymentComm.id") }
	)
	public PaymentCommission updatePaymentCommission(PaymentCommission paymentComm) {
		
		return paymentCommissionRepository.findById(paymentComm.getId()).map(oldPaymentComm -> {
			if (paymentComm.getCommissionPercent().intValue() != 0)
				oldPaymentComm.setCommissionPercent(paymentComm.getCommissionPercent());
			return paymentCommissionRepository.save(oldPaymentComm);
		}).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.pmtCommId", paymentComm.getId())));
	
	}
	
	/*public List<PendingPaymentDtls> findPendingPayment(List<String> trainingStatus) {

		List<TrainingDtls> trainingList = trainingProxy.findByTrainingStatus(trainingStatus);
		System.out.println("trainingList.size() " + trainingList.size());
		List<Long> mentorIds = new ArrayList<>();
		for (TrainingDtls obj : trainingList)
			mentorIds.add(obj.getMentorId());

		PendingPaymentDtls obj = null;
		List<PendingPaymentDtls> pendingPmtList = paymentRepository.aggregateByMentorId(mentorIds);
		for (int index = 0; index < pendingPmtList.size(); index++) {
			obj = pendingPmtList.get(index);
			for (TrainingDtls trainingObj : trainingList) {
				if (trainingObj.getMentorId().equals(obj.getMentorId())) {
					obj.setTrainingId(trainingObj.getId());
					obj.setAmountReceived(trainingObj.getAmountReceived());
					obj.setCommissionAmount(trainingObj.getCommissionAmount());
					obj.setProgress(trainingObj.getProgress());
					break;
				}
			}
		}
		System.out.println("pendingPmtList " + pendingPmtList);

		return pendingPmtList;

	}*/

	
	// list of fallback method for @HystrixCommand
	public Page<PaymentDtls> fallback_findPaymentDtlsByDateRange(Long mentorId, String startDateStr, String endDateStr,
			String orderBy, String direction, int page, int size, String authToken) {
		throw new ServiceUnavailableException(Translator.toLocale("error.payment.service"));
	}

	public Payment fallback_addPayment(Long mentorId, Long trainingId, Payment payment) {
		throw new ServiceUnavailableException(Translator.toLocale("error.payment.service"));
	}

	public Payment fallback_updatePayment(Long paymentId, Payment payment) {
		throw new ServiceUnavailableException(Translator.toLocale("error.payment.service"));
	}

	public PaymentDtls fallback_findTotalPaidAmountByMentorId(Long mentorId, Long trainingId) {
		throw new ServiceUnavailableException(Translator.toLocale("error.payment.service"));
	}

	public PaymentCommission fallback_findPaymentCommission(Long id) {
		throw new ServiceUnavailableException(Translator.toLocale("error.payment.service"));
	}

	public PaymentCommission fallback_updatePaymentCommission(Long id, PaymentCommission paymentComm) {
		throw new ServiceUnavailableException(Translator.toLocale("error.payment.service"));
	}
			
}
