package com.fr.exceptions;

import com.fr.commons.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

/**
 * This Class handle all Exceptions.
 *
 * Created by djenanewail on 3/7/17.
 */
@ControllerAdvice
public class SppotiExceptionHandler
{
	
	/** Class logger. */
	private final Logger LOGGER = LoggerFactory.getLogger(SppotiExceptionHandler.class);
	
	/**
	 * Catch All {@link NotAdminException} exceptions.
	 *
	 * @param e
	 * 		instace of {@link NotAdminException}
	 *
	 * @return 403 http status if exception was catched.
	 */
	@ExceptionHandler(value = NotAdminException.class)
	public ResponseEntity notTeamAdmin(final NotAdminException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	}
	
	/**
	 * Catch All {@link BusinessGlobalException} exceptions.
	 *
	 * @param e
	 * 		instace of {@link BusinessGlobalException}
	 *
	 * @return 400 http status if exception was catched.
	 */
	@ExceptionHandler(value = BusinessGlobalException.class)
	public ResponseEntity globalException(final BusinessGlobalException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	/**
	 * Catch All {@link EntityNotFoundException} exceptions.
	 *
	 * @param e
	 * 		instace of {@link EntityNotFoundException}
	 *
	 * @return 404 http status if exception was catched.
	 */
	@ExceptionHandler(value = {EntityNotFoundException.class, IllegalArgumentException.class})
	public ResponseEntity globalException(final EntityNotFoundException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	/**
	 * Catch All {@link org.springframework.dao.DataIntegrityViolationException}
	 *
	 * @param e
	 * 		instace of {@link DataIntegrityViolationException}
	 *
	 * @return 404 http status if exception was catched.
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity conflictException(final DataIntegrityViolationException e)
	{
		
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
	
	/**
	 * Catch All {@link javax.persistence.EntityExistsException} exceptions.
	 *
	 * @param e
	 * 		instace of {@link EntityExistsException}
	 *
	 * @return 404 http status if exception was catched.
	 */
	@ExceptionHandler(EntityExistsException.class)
	public ResponseEntity conflictExistException(final EntityExistsException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
	
	/**
	 * Catch All {@link NoRightToAcceptOrRefuseChallenge} exceptions.
	 *
	 * @param e
	 * 		instace of {@link NoRightToAcceptOrRefuseChallenge}
	 *
	 * @return 403 http status if exception was catched.
	 */
	@ExceptionHandler(value = NoRightToAcceptOrRefuseChallenge.class)
	public ResponseEntity globalException(final NoRightToAcceptOrRefuseChallenge e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	}
	
	/**
	 * Catch any unhandled {@link RuntimeException}
	 *
	 * @param e
	 * 		a {@link RuntimeException}
	 *
	 * @return 500 http status
	 */
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity otherRuntimeExceptions(final RuntimeException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
	
	/**
	 * Catch all {@link ConflictUsernameException}
	 *
	 * @param e
	 * 		exception content.
	 *
	 * @return 411 http status.
	 */
	@ExceptionHandler(value = ConflictUsernameException.class)
	public ResponseEntity conflictUsernameException(final ConflictUsernameException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(411).body(e.getMessage());
	}
	
	/**
	 * Catch all {@link ConflictEmailException}
	 *
	 * @param e
	 * 		exception content.
	 *
	 * @return 409 hhtp status.
	 */
	@ExceptionHandler(value = ConflictEmailException.class)
	public ResponseEntity conflictEmailException(final ConflictEmailException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(410).body(e.getMessage());
	}
	
	/**
	 * Catch all {@link AccountConfirmationLinkExpiredException}
	 *
	 * @param e
	 * 		exception content.
	 *
	 * @return 413 http status.
	 */
	@ExceptionHandler(value = AccountConfirmationLinkExpiredException.class)
	public ResponseEntity accountConfirmationLinkExpired(final AccountConfirmationLinkExpiredException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(413).body(e.getMessage());
	}
	
	
}
