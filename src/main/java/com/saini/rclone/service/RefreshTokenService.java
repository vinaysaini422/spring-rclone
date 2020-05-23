package com.saini.rclone.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.saini.rclone.exceptions.SpringRedditException;
import com.saini.rclone.model.RefreshToken;
import com.saini.rclone.repositories.RefreshTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());

		return refreshTokenRepository.save(refreshToken);
	}

	void validateRefreshToken(String token) {
		refreshTokenRepository.findByToken(token).orElseThrow(() -> new SpringRedditException("Invalid refresh Token"));
	}

	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}
}
