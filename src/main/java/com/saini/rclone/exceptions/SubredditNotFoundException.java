package com.saini.rclone.exceptions;

public class SubredditNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1818305614839894228L;

	public SubredditNotFoundException(String message) {
        super(message);
    }
}
