package com.example.security.filters;

import java.awt.PageAttributes.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.models.UserEntity;
import com.example.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	
	
	private JwtUtils jwtUtils;
	
	
	public JwtAuthenticationFilter(JwtUtils jwtUtils) {
		this.jwtUtils =jwtUtils;
	}
	
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		UserEntity userEntity = null;
		String username ="";
		String password ="";
		
		try {
			userEntity=new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
			username = userEntity.getUsername();
			password=userEntity.getPassword();
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username, password);
		return getAuthenticationManager().authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		User user = (User) authResult.getPrincipal();
		String token =jwtUtils.generateAccessToken(user.getUsername());
		
		response.addHeader("Authorization", token);
		
		Map<String, Object> httpResponse = new HashMap<>();
		
		httpResponse.put("token", token);
		httpResponse.put("Message","Autentication Correcta");
		httpResponse.put("Username",user.getUsername());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().flush();
		
		
		super.successfulAuthentication(request, response, chain, authResult);
	}

	
	
	
	
}
