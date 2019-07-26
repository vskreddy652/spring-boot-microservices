package com.eg.mod.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eg.mod.exception.ResourceNotFoundException;
import com.eg.mod.exception.ServiceUnavailableException;
import com.eg.mod.model.UserDtls;
import com.eg.mod.proxy.UserServiceProxy;
import com.eg.mod.util.Translator;

@Service(value = "userService")
public class UserService implements UserDetailsService {

	@Autowired
	private UserServiceProxy userProxy;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		UserDtls user = userProxy.findByName(userName);
		if (user == null)
			throw new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.userName", userName));
		else if (user != null && user.getUserName().length() == 0)
			throw new ServiceUnavailableException(Translator.toLocale("error.service.unavailable", "user-service"));
		else if (user != null && (user.getConfirmedSignUp().booleanValue() == false
				|| user.getActive().booleanValue() == false || user.getResetPassword().booleanValue() == true))
			throw new UsernameNotFoundException(Translator.toLocale("error.invalid.user"));
		else
			return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthority(user.getRole()));
	}

	private Set<SimpleGrantedAuthority> getAuthority(String role) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		return authorities;
	}

}
