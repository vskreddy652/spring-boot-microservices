package com.eg.mod.controller;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eg.mod.model.UserDtls;

@Controller
public class UserOperationsController {

	@RequestMapping(value = "/confirmUser", method = RequestMethod.GET)
	public String confirmUserPage(
			@RequestParam(value = "un", required = true) String un,
			@RequestParam(value = "tn", required = true) String tn, 
			ModelMap model, 
			HttpServletRequest request)
			throws Exception {

		UserDtls userDtls = new UserDtls();
		userDtls.setUserName(un);
		userDtls.setToken(tn);
		model.put("userDtls", userDtls);
		getHostPort(model, request);

		return "confirmUser";
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public String resetPasswordPage(
			@RequestParam(value = "un", required = true) String un,
			@RequestParam(value = "tn", required = true) String tn, 
			ModelMap model, 
			HttpServletRequest request)
			throws Exception {

		UserDtls userDtls = new UserDtls();
		userDtls.setUserName(un);
		userDtls.setToken(tn);
		model.put("userDtls", userDtls);
		getHostPort(model, request);

		return "resetPassword";
	}

	private void getHostPort(ModelMap model, HttpServletRequest request) throws Exception {

		URI uri = new URI(request.getRequestURL().toString());
		String hostName = uri.getHost();
		int port = uri.getPort();
		model.put("hostName", hostName);
		model.put("port", port);
	}
}
