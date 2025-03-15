package com.xalts.io.multi.user.approval.service;

import com.xalts.io.multi.user.approval.dto.AuthRequest;
import com.xalts.io.multi.user.approval.dto.AuthResponse;
import com.xalts.io.multi.user.approval.model.User;
import com.xalts.io.multi.user.approval.repository.UserRepository;
import com.xalts.io.multi.user.approval.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	public AuthResponse register(AuthRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BadCredentialsException("Email already in use");
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		User savedUser = userRepository.save(user);

		String jwtToken = jwtUtil.generateToken(savedUser.getEmail());

		return new AuthResponse(jwtToken, savedUser.getId(), savedUser.getName(), savedUser.getEmail());
	}

	public AuthResponse login(AuthRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Invalid email or password");
		}

		String jwtToken = jwtUtil.generateToken(user.getEmail());

		return new AuthResponse(jwtToken, user.getId(), user.getName(), user.getEmail());
	}

	public User getUserById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
	}

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
}