package com.eg.mod.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eg.mod.model.UserDtls;

@FeignClient(value = "user-service", fallback = UserServiceFallback.class)
@RibbonClient(value = "user-service")
public interface UserServiceProxy {

	@GetMapping("/users/findByName/{userName}")
	public UserDtls findByName(
			@PathVariable(value = "userName", required = true) String userName);

}

@Component
class UserServiceFallback implements UserServiceProxy {

	@Override
	public UserDtls findByName(String userName) {
		return new UserDtls();
	}
}