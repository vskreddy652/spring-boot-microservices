package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
public class HelloClientApplication {
	@Autowired
	HelloClient client;

	@RequestMapping("/")
	public String hello() {
		return client.hello();
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloClientApplication.class, args);
	}

	@FeignClient(value = "HelloServer", fallback = HelloFallback.class)
	interface HelloClient {
		@RequestMapping(value = "/", method = GET)
		String hello();
	}
	
	@Component
	class HelloFallback implements HelloClient {

		@Override
		public String hello() {
			return "NO Hello, get lost";
		}
		
	}
}

