package com.eg.mod.proxy;

import javax.validation.Valid;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eg.mod.model.ApiResponse;
import com.eg.mod.model.PaymentCommDtls;
import com.eg.mod.model.PaymentDtls;

@FeignClient(value = "payment-service", fallback = PaymentServiceFallback.class)
@RibbonClient(value = "payment-service")
public interface PaymentServiceProxy {

	@GetMapping("/payments/findTotalPaidAmountByMentorId/{mentorId}")
	public PaymentDtls findTotalPaidAmountByMentorId(
			@PathVariable(value = "mentorId", required = true) Long mentorId,
			@PathVariable(value = "trainingId", required = true) Long trainingId);

	@PostMapping("/payments/addPayment")
	public ApiResponse<?> addPayment(
			@Valid @RequestBody PaymentDtls payment);

	@GetMapping("payments/findPaymentCommission/{id}")
	public PaymentCommDtls findPaymentCommission(
			@PathVariable(value = "id", required = true) Long id);

}

@Component
class PaymentServiceFallback implements PaymentServiceProxy {

	@Override
	public PaymentDtls findTotalPaidAmountByMentorId(Long mentorId, Long trainingId) {
		return new PaymentDtls();
	}

	@Override
	public ApiResponse<?> addPayment(PaymentDtls payment) {
		return new com.eg.mod.model.ApiResponse<>(HttpStatus.OK.value(), null, null);
	}

	@Override
	public PaymentCommDtls findPaymentCommission(Long id) {
		return new PaymentCommDtls();
	}
}