package com.xalts.io.multi.user.approval.config;

import com.xalts.io.multi.user.approval.model.User;
import com.xalts.io.multi.user.approval.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) {
		// Create some sample users if none exist
		if (userRepository.count() == 0) {
			createSampleUser("John Doe", "john@example.com", "password");
			createSampleUser("Jane Smith", "jane@example.com", "password");
			createSampleUser("Bob Johnson", "bob@example.com", "password");
			createSampleUser("Alice Williams", "alice@example.com", "password");
			createSampleUser("Charlie Brown", "charlie@example.com", "password");
		}
	}

	private void createSampleUser(String name, String email, String password) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}
}