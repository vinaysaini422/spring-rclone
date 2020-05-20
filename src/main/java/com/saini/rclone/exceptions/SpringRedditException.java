package com.saini.rclone.exceptions;

public class SpringRedditException extends RuntimeException {
    
	private static final long serialVersionUID = 4091993317181374328L;

	public SpringRedditException(String exMessage) {
        super(exMessage);
    }
}