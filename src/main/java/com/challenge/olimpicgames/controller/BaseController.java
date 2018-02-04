package com.challenge.olimpicgames.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.challenge.olimpicgames.exception.DurationCompetitionException;
import com.challenge.olimpicgames.exception.QuantityCompetitionException;
import com.challenge.olimpicgames.exception.SameTimeCompetitionException;

@ControllerAdvice
public class BaseController {

	@ExceptionHandler({ DurationCompetitionException.class })
	public ResponseEntity<?> handleDurationCompetitionError(final DurationCompetitionException exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}

	@ExceptionHandler({ QuantityCompetitionException.class })
	public ResponseEntity<?> handleQuantityCompetitionError(final QuantityCompetitionException exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}

	@ExceptionHandler({ SameTimeCompetitionException.class })
	public ResponseEntity<?> handleSameTimeCompetitionError(final SameTimeCompetitionException exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}
}
