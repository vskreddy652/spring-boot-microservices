package com.eg.mod.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eg.mod.entity.Payment;
import com.eg.mod.entity.PaymentCommission;
import com.eg.mod.exception.PaginationSortingException;
import com.eg.mod.model.PaymentDtls;
import com.eg.mod.pagination.Direction;
import com.eg.mod.pagination.OrderBy;
import com.eg.mod.service.PaymentService;
import com.eg.mod.util.Translator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payments")
@Api(value = "payment-service", description = "Operations pertaining to payment services")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@ApiOperation(value = "View list of payment details within date range", response = Page.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 412, message = "Invalid Sort Direction"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasAnyRole('ADMIN', 'MENTOR')")
	@GetMapping("/findPaymentDtlsByDateRange/{mentorId}/{startDate}/{endDate}")
	public Page<PaymentDtls> findPaymentDtlsByDateRange(
			@ApiParam(value = "Mentor Id for which record needs to find", required = true) @PathVariable(value = "mentorId") Long mentorId,
			@ApiParam(value = "Start Date for which record needs to find", required = true) @PathVariable(value = "startDate", required = true) String startDate,
			@ApiParam(value = "End Date for which record needs to find", required = true) @PathVariable(value = "endDate", required = true) String endDate,
			@ApiParam(value = "Ordring By search rerods", required = true) @RequestParam(value = "orderBy", required = true) String orderBy,
			@ApiParam(value = "Ordring search rerods (ASC/DESC)", required = true) @RequestParam(value = "direction", required = true) String direction,
			@ApiParam(value = "Offset value for pagination", required = true) @RequestParam(value = "page", required = true) int page,
			@ApiParam(value = "Per page record size in pagination", required = true) @RequestParam(value = "size", required = true) int size,
			@ApiParam(value = "GWT authentication token", required = true) @RequestHeader("Authorization") String authToken) {

		if (!(direction.equals(Direction.ASCENDING.getDirectionCode())
				|| direction.equals(Direction.DESCENDING.getDirectionCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.order"));
		}
		if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.NAME.getOrderByCode()))) {
			throw new PaginationSortingException(Translator.toLocale("error.invalid.orderby"));
		}

		return paymentService.findPaymentDtlsByDateRange(mentorId, startDate, startDate, orderBy, direction, page, size, authToken);
	}
	
	@ApiOperation(value = "Find total paid amount for specific mentor", response = PaymentDtls.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@GetMapping("/findTotalPaidAmountByMentorId/{mentorId}/{trainingId}")
	public PaymentDtls findTotalPaidAmountByMentorId(
			@ApiParam(value = "Mentor Id for which record needs to find", required = true) @PathVariable(value = "mentorId", required = true) Long mentorId,
			@ApiParam(value = "Training Id for which record needs to find", required = true) @PathVariable(value = "trainingId", required = true) Long trainingId) {

		return paymentService.findTotalPaidAmountByMentorId(mentorId, trainingId);
	}
	
	@ApiOperation(value = "Add payment details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PostMapping("/addPayment")
	public com.eg.mod.model.ApiResponse<?> addPayment(
			@ApiParam(value = "Payment object which needs to add", required = true) @Valid @RequestBody Payment payment) {

		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.payment.add"),
				paymentService.addPayment(payment));
	}

	/*@PutMapping("/updatePayment/{paymentId}")
	public ResponseEntity<?> updatePayment(
			@PathVariable(value = "paymentId", required = true) Long paymentId,
			@Valid @RequestBody Payment payment) {

		paymentService.updatePayment(paymentId);
		return ResponseEntity.ok("Payment updated successfully");
	}
   */
	
	@ApiOperation(value = "Find payment commission details", response = PaymentCommission.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 404, message = "Resource Not Found"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@GetMapping("/findPaymentCommission/{id}")
	public PaymentCommission findPaymentCommission(
			@ApiParam(value = "Payment Commission Id for which record needs to find", required = true) @PathVariable(value = "id", required = true) Long id) {
		
		return paymentService.findPaymentCommission(id);
	}
	
	@ApiOperation(value = "Update payment commission details", response = com.ibm.mod.model.ApiResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully performed the operation"),
			@ApiResponse(code = 404, message = "Resource Not Found"),
			@ApiResponse(code = 503, message = "Service Unavailable")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatePaymentCommission/{commissionPercent}")
	public com.eg.mod.model.ApiResponse<?> updatePayment(
			@ApiParam(value = "Commission object which needs to update", required = true) @PathVariable(value = "commissionPercent", required = true) Float commissionPercent) {

		PaymentCommission paymentComm = new PaymentCommission();
		paymentComm.setCommissionPercent(commissionPercent);
		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), Translator.toLocale("success.payment.commission.update"),
				paymentService.updatePaymentCommission(paymentComm));
	}
   
}