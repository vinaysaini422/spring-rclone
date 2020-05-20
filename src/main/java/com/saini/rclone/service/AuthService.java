package com.saini.rclone.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saini.rclone.dto.RegisterRequest;
import com.saini.rclone.model.NotificationEmail;
import com.saini.rclone.model.User;
import com.saini.rclone.model.VerificationToken;
import com.saini.rclone.repositories.UserRepository;
import com.saini.rclone.repositories.VerificationTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	
	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user=new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		
		userRepository.save(user);
		String activationToken =generateVerificationToken(user);
		
		mailService.sendMail(NotificationEmail.builder()
				.subject("Please activate your account")
				.recipient(user.getEmail())
				.body("please click on the below url to activate your account : " +
		                "http://localhost:8080/api/auth/accountVerification/" + activationToken)
				.build());
	}
	
	@Transactional
	private String generateVerificationToken(User user) {
		String token =UUID.randomUUID().toString();
		
		VerificationToken verificationToken =VerificationToken.builder()
				.token(token)
				.user(user)
				.build();
		
		verificationTokenRepository.save(verificationToken);
		
		return token;
	}
}
