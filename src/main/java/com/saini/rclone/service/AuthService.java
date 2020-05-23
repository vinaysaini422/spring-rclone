package com.saini.rclone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saini.rclone.dto.AuthenticationResponse;
import com.saini.rclone.dto.LoginRequest;
import com.saini.rclone.dto.RefreshTokenRequest;
import com.saini.rclone.dto.RegisterRequest;
import com.saini.rclone.exceptions.SpringRedditException;
import com.saini.rclone.model.NotificationEmail;
import com.saini.rclone.model.User;
import com.saini.rclone.model.VerificationToken;
import com.saini.rclone.repositories.UserRepository;
import com.saini.rclone.repositories.VerificationTokenRepository;
import com.saini.rclone.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final RefreshTokenService refreshTokenService;

	@Transactional
	public void signup(RegisterRequest registerRequest) {

		if (isUserExists(registerRequest)) {
			User user = new User();
			user.setUsername(registerRequest.getUsername());
			user.setEmail(registerRequest.getEmail());
			user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
			user.setCreated(Instant.now());
			user.setEnabled(false);

			userRepository.save(user);
			String activationToken = generateVerificationToken(user);

			mailService
					.sendMail(
							NotificationEmail.builder().subject("Please activate your account")
									.recipient(user.getEmail() +"Thank you for signing up to Our Application")
									.body("please click on the below url to activate your account : "
											+ "http://localhost:9091/api/auth/accountVerification/" + activationToken)
									.build());
		} else {
			throw new SpringRedditException("User Already exists with username or email!");
		}

	}

	@Transactional
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }
	
	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String userName = verificationToken.getUser().getUsername();
		Optional<User> optional = userRepository.findByUsername(userName);

		optional.orElseThrow(() -> new SpringRedditException("User not found with the username : " + userName));

		User user = optional.get();
		user.setEnabled(true);
		userRepository.save(user);
	}
	
	@Transactional
	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();

		VerificationToken verificationToken = VerificationToken.builder().token(token).user(user).build();

		verificationTokenRepository.save(verificationToken);

		return token;
	}
	
	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));

		fetchUserAndEnable(verificationToken.get());
	}
	
	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authentication =	 authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token =jwtProvider.generateToken(authentication);
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(loginRequest.getUsername())
				.build();
	}
	
	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

	
	
	

	

	

	private boolean isUserExists(RegisterRequest registerRequest) {

		if (userRepository.findByEmail(registerRequest.getEmail()).isEmpty()) {
			return userRepository.findByUsername(registerRequest.getUsername()).isEmpty();
		}
		return false;

	}

}
