package com.eg.mod.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eg.mod.entity.User;
import com.eg.mod.exception.DataValidationException;
import com.eg.mod.exception.ResourceExistException;
import com.eg.mod.exception.ResourceNotFoundException;
import com.eg.mod.exception.ServiceUnavailableException;
import com.eg.mod.model.UserDtls;
import com.eg.mod.model.Constants.UserRole;
import com.eg.mod.reprository.UserRepository;
import com.eg.mod.util.Translator;

@Service(value = "userService")
@Transactional(readOnly = true)
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SequenceGeneratorService sequencegenerator;

	@Autowired
	private EmailService emailService;
	
	@Value("${server.host}")
	private String serverHost;
	
	@Value("${server.port}")
	private String serverPort;
	
	//@HystrixCommand(fallbackMethod = "fallback_loadUserByUsername", commandKey = "findByUserIdPassword", groupKey = "findByUserIdPassword", ignoreExceptions = { UsernameNotFoundException.class })
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		UserDetails userDetails = null;
		User user = userRepository.findByActiveNonResetPasswdUser(userName);
		if (user != null)
			userDetails = new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthority(user.getRole()));
		else 
			throw new UsernameNotFoundException(Translator.toLocale("error.invalid.user"));
		return userDetails;
	}
	
	public Set<SimpleGrantedAuthority> getAuthority(String role) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		return authorities;
		//return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_signup", commandKey = "signup", groupKey = "signup", ignoreExceptions = { DataValidationException.class, ResourceExistException.class })
	@Caching(
			put = { @CachePut(value = "userCache", key = "#user.id") }, 
			evict = { @CacheEvict(value = "allUsersCache", allEntries = true) }
	)
	public User signup(User user, HttpServletRequest request) {

		if (user.getRole() == null || (user.getRole() != null && user.getRole().length() == 0))
			throw new DataValidationException(Translator.toLocale("error.required.role"));

		if (!(user.getRole().toUpperCase().equals(UserRole.ROLE_ADMIN.getRole())
				|| user.getRole().toUpperCase().equals(UserRole.ROLE_USER.getRole())
				|| user.getRole().toUpperCase().equals(UserRole.ROLE_MENTOR.getRole()))) {
			throw new DataValidationException(Translator.toLocale("error.invalid.role", user.getRole()));
		}

		if (user.getRole().toUpperCase().equals(UserRole.ROLE_ADMIN.getRole())
				|| user.getRole().toUpperCase().equals(UserRole.ROLE_USER.getRole())) {
			user.setLinkedinUrl(null);
			user.setYearsOfExperience(null);
		} else if (user.getRole().toUpperCase().equals(UserRole.ROLE_MENTOR.getRole())) {
			if (user.getLinkedinUrl() == null
					|| (user.getLinkedinUrl() != null && user.getLinkedinUrl().trim().length() == 0))
				throw new DataValidationException(Translator.toLocale("error.required.linkedlin"));
			if (user.getYearsOfExperience() == null
					|| (user.getYearsOfExperience() != null && user.getYearsOfExperience().floatValue() == 0.0f))
				throw new DataValidationException(Translator.toLocale("error.required.yoe"));
		}

		if (userRepository.findByName(user.getUserName()) != null)
			throw new ResourceExistException(Translator.toLocale("error.resource.found.userName", user.getUserName()));

		Random r = new Random();
		String token = Long.toString(Math.abs(r.nextLong()), 36);
		user.setId(sequencegenerator.generateSequence(User.SEQUENCE_NAME));
		PasswordEncoder pencoder = new BCryptPasswordEncoder(4);
		user.setPassword(pencoder.encode(user.getPassword()));
		user.setRegCode(token);
		user.setPersistent(true); // to create created_date
		userRepository.save(user);
		
		String subject = Translator.toLocale("email.subject.signup");
		String text = Translator.toLocale("email.body.signup", user.getFirstName()) + "http://" + serverHost + ":"
				+ serverPort + "/confirmUser?un=" + user.getUserName() + "&tn=" + token + "&lang="
				+ request.getHeader("Accept-Language");
		emailService.sendMail("MentorOnDemand@test.com", user.getUserName(), subject, text);
		
		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_confirmUser", commandKey = "confirmUser", groupKey = "confirmUser")
	@Caching(
			put = { @CachePut(value = "userCache", key = "#user.id") }, 
			evict = { @CacheEvict(value = "allUsersCache", allEntries = true) }
	)
	public String confirmUser(UserDtls user) {

		String message = "";
		User user1 = userRepository.findByActiveUser(user.getUserName());
		if (user1 != null)
			message = Translator.toLocale("message.confirm.user", user.getUserName());
		else {			
			user1 = userRepository.findUserByNameRegCodeForSignUp(user.getUserName(), user.getToken());
			if (user1 == null)
				message = Translator.toLocale("error.invalid.user-token");
			else {
				user1.setRegCode("");
				user1.setActive(true);
				user1.setConfirmedSignUp(true);
				userRepository.save(user1);
				message = Translator.toLocale("success.user.confirm");
			}
		}
		return message;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_forgetPassword", commandKey = "forgetPassword", groupKey = "forgetPassword", ignoreExceptions = { ResourceNotFoundException.class })
	public void forgetPassword(String userName, HttpServletRequest request) {

		User user = userRepository.findByActiveUser(userName);
		if (user == null)
			throw new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.userName", userName));
		
		Random r = new Random();
		String token = Long.toString(Math.abs(r.nextLong()), 36);
		user.setRegCode(token);
		user.setResetPasswordDate(new Date());
		user.setResetPassword(true);
		userRepository.save(user);

		String subject = Translator.toLocale("email.subject.resetpwd");
		String text = Translator.toLocale("email.body.resetpwd", user.getFirstName()) + "http://" + serverHost + ":"
				+ serverPort + "/resetPassword?un=" + user.getUserName() + "&tn=" + token + "&lang="
				+ request.getHeader("Accept-Language");
		emailService.sendMail("MentorOnDemand@test.com", user.getUserName(), subject, text);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_resetPassword", commandKey = "resetPassword", groupKey = "resetPassword", ignoreExceptions = { ResourceNotFoundException.class })
	@Caching(
			put = { @CachePut(value = "userCache", key = "#user.id") }, 
			evict = { @CacheEvict(value = "allUsersCache", allEntries = true) }
	)
	public String resetPassword(UserDtls user) {

		String message = "";
		User user1 = userRepository.findByActiveNonResetPasswdUser(user.getUserName());
		if (user1 != null)
			message = Translator.toLocale("message.reset.password", user.getUserName());
		else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -15);
			user1 = userRepository.findUserByNameRegCodeForPwdReset(user.getUserName(), user.getToken(), cal.getTime());
			if (user1 == null)
				message = Translator.toLocale("error.invalid.resetpwd-token");
			else {
				PasswordEncoder pencoder = new BCryptPasswordEncoder(4);
				user1.setPassword(pencoder.encode(user.getPassword()));
				user1.setRegCode("");
				user1.setResetPasswordDate(null);
				user1.setResetPassword(false);
				userRepository.save(user1);
				message = Translator.toLocale("success.reset.password");
			}
		}
		return message;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_updateProfile", commandKey = "updateProfile", groupKey = "updateProfile", ignoreExceptions = { ResourceNotFoundException.class })
	@Caching(
			put = { @CachePut(value = "userCache", key = "#user.id") }, 
			evict = { @CacheEvict(value = "allUsersCache", allEntries = true) }
	)
	public User updateProfile(User user) {
		
		return userRepository.findById(user.getId()).map(oldUser -> {
			// block or unblock user
			if (!user.getActive().equals(oldUser.getActive()))
				oldUser.setActive(user.getActive());
			else {
				if (user.getPassword() != null && user.getPassword().trim().length() > 0) {
					PasswordEncoder pencoder = new BCryptPasswordEncoder(4);
					oldUser.setPassword(pencoder.encode(user.getPassword()));
				}
				if (user.getFirstName() != null && user.getFirstName().trim().length() > 0)
					oldUser.setFirstName(user.getFirstName());
				if (user.getLastName() != null && user.getLastName().trim().length() > 0)
					oldUser.setLastName(user.getLastName());
				if (user.getContactNumber() != null && user.getContactNumber().longValue() != 0L)
					oldUser.setContactNumber(user.getContactNumber());
				if (oldUser.getRole().toUpperCase().equals(UserRole.ROLE_MENTOR.getRole())) {
					if (user.getLinkedinUrl() != null && user.getLinkedinUrl().trim().length() > 0)
						oldUser.setLinkedinUrl(user.getLinkedinUrl());
					if (user.getRegCode() != null)
						oldUser.setRegCode(user.getRegCode());
					if (user.getYearsOfExperience() != null && user.getYearsOfExperience() != 0.0f)
						oldUser.setYearsOfExperience(user.getYearsOfExperience());
				}
			}
			return userRepository.save(oldUser);
		}).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.userId", user.getId())));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	//@HystrixCommand(fallbackMethod = "fallback_deleteProfile", commandKey = "deleteProfile", groupKey = "deleteProfile", ignoreExceptions = { ResourceNotFoundException.class })
	@Caching(evict = { 
			@CacheEvict(value = "userCache", key = "#id"),
			@CacheEvict(value = "allUsersCache", allEntries = true) 
	})	
	public void deleteProfile(Long id) {

		userRepository.findById(id).map(user -> {
			userRepository.delete(user);
			return true;
		}).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.userId", id)));
	}

	@SuppressWarnings("deprecation")
	//@HystrixCommand(fallbackMethod = "fallback_findAllUsers", commandKey = "findAllUsers", groupKey = "findAllUsers", ignoreExceptions = { ResourceNotFoundException.class })
	@Cacheable(value= "allUsersCache")//, unless= "#result.content.size() == 0")
	public Page<User> findAllUsers(String orderBy, String direction, int page, int size) {
		
		Sort sort = null;

		if (direction.equals("ASC")) {
			sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
		} else if (direction.equals("DESC")) {
			sort = new Sort(new Sort.Order(Direction.DESC, orderBy));
		}
		Pageable pageable = new PageRequest(page, size, sort);
		return userRepository.findAll(pageable);
	}

	//@HystrixCommand(fallbackMethod = "fallback_findById", commandKey = "findById", groupKey = "findById", ignoreExceptions = { ResourceNotFoundException.class })
	@Cacheable(value= "userCache", key= "#id")
	public User findById(Long id) {

		return Optional.ofNullable(userRepository.findById(id).get())
					.orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.userId", id)));
	}

	//@HystrixCommand(fallbackMethod = "fallback_findByName", commandKey = "findByName", groupKey = "findByName", ignoreExceptions = { ResourceNotFoundException.class })
	public User findByName(String userName) {
		return Optional.ofNullable(userRepository.findByName(userName).get())
					.orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("error.resource.notfound.userName" + userName)));
	}

	
	
	// list of fallback method for @HystrixCommand
	public UserDetails fallback_loadUserByUsername(String userName) {
		throw new ServiceUnavailableException(Translator.toLocale("error.user.service"));
	}

	public User fallback_signup(User user, HttpServletRequest request) {
		throw new ServiceUnavailableException(Translator.toLocale("error.user.service"));
	}

	public String fallback_confirmUser(UserDtls userDtls) {
		throw new ServiceUnavailableException(Translator.toLocale("error.user.service"));
	}

	public void fallback_forgetPassword(String userName, HttpServletRequest request) {
		throw new ServiceUnavailableException(Translator.toLocale("error.user.service"));
	}

	public String fallback_resetPassword(UserDtls userDtls) {
		throw new ServiceUnavailableException(Translator.toLocale("error.user.service"));
	}

	public User fallback_updateProfile(Long userId, User user) {
		throw new ServiceUnavailableException(Translator.toLocale("error.user.service"));
	}

	public void fallback_deleteProfile(Long userId) {
		throw new ServiceUnavailableException(Translator.toLocale("error.user.service"));
	}

	public Page<User> fallback_findAllUsers(String orderBy, String direction, int page, int size) {
		throw new ServiceUnavailableException(Translator.toLocale("error.user.service"));
	}

	public User fallback_findById(Long userId) {
		throw new ServiceUnavailableException(Translator.toLocale("error.user.service"));
	}

	public User fallback_findByName(String userName) {
		throw new ServiceUnavailableException(Translator.toLocale("error.user.service"));
	}
}
