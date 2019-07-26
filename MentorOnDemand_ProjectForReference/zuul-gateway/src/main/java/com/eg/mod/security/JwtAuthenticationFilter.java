package com.eg.mod.security;

import static com.eg.mod.model.Constants.HEADER_STRING;
import static com.eg.mod.model.Constants.TOKEN_PREFIX;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eg.mod.util.Translator;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
    private TokenProvider jwtTokenUtil;
	
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// Get the authentication header
		String header = request.getHeader(HEADER_STRING);
		// Validate the header and check the prefix
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		// Get the token
		String authToken = header.replace(TOKEN_PREFIX, "");
		String userName = null;
		
		try {
			userName = jwtTokenUtil.getUsernameFromToken(authToken);	
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
				UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (IllegalArgumentException e) {
			// logger.error("an error occured during getting username from token", e);
		} catch (ExpiredJwtException e) {
			request.setAttribute("expired", Translator.toLocale("error.token.expired"));
			// logger.warn("the token is expired and not valid anymore", e);
		} catch (SignatureException e) {
			// logger.error("Authentication Failed. Username or Password not valid.");
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}
		
		chain.doFilter(request, response);
	}

}
