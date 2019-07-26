package com.eg.mod.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import com.eg.mod.util.Translator;
import com.netflix.hystrix.exception.HystrixTimeoutException;

@Configuration
public class ZuulFallbackProvider implements FallbackProvider {

	@Override
	public String getRoute() {
		return "*";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, final Throwable cause) {
		System.out.println(cause.getMessage());
		if (cause instanceof HystrixTimeoutException) {
			return response(route, HttpStatus.GATEWAY_TIMEOUT);
		} else {
			return response(route, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ClientHttpResponse response(String route, final HttpStatus status) {
		return new ClientHttpResponse() {
			@Override
			public HttpStatus getStatusCode() throws IOException {
				return status;
			}

			@Override
			public int getRawStatusCode() throws IOException {
				return status.value();
			}

			@Override
			public String getStatusText() throws IOException {
				return status.toString();
			}

			@Override
			public void close() {
			}

			@Override
			public InputStream getBody() throws IOException {
				String message = "";
				if (status == HttpStatus.GATEWAY_TIMEOUT)
					message = Translator.toLocale("error.service.timeout", route);
				else if (status == HttpStatus.INTERNAL_SERVER_ERROR)
					message = Translator.toLocale("error.service.unavailable", route);
				return new ByteArrayInputStream(message.getBytes());
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				return headers;
			}
		};
	}
}
