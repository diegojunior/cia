package br.com.totvs.cia.infra.exception;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.totvs.cia.integracao.core.exception.GatewayException;
import br.com.totvs.cia.integracao.core.exception.RecursoIntegracaoNaoEncontrado;

@ControllerAdvice
public class CiaRestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final String DEFAULT_ERROR_MSG = "Ocorreu um erro interno, favor entrar em contato com o administrador.";
	
	private static final Logger LOG = Logger.getLogger(CiaRestResponseEntityExceptionHandler.class);

	@ExceptionHandler(CiaBusinessException.class)
	public ResponseEntity<Object> businessException(
			final CiaBusinessException ex) {

		final DefaultErrorMessage defaultErrorMessage = new DefaultErrorMessage(
				StatusCodeEnum.BUSINESS_EXCEPTION.name(), ex, ex.getMessage());
		
		LOG.error(ex.getMessage(), ex);

		return new ResponseEntity<Object>(defaultErrorMessage,
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HibernateException.class)
	public ResponseEntity<Object> hibernateException(
			final HibernateException ex) {

		final DefaultErrorMessage defaultErrorMessage = new DefaultErrorMessage(
				StatusCodeEnum.DATABASE_EXCEPTION.name(), ex, DEFAULT_ERROR_MSG);

		LOG.error(ex.getMessage(), ex);
		
		return new ResponseEntity<Object>(defaultErrorMessage,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> notException(
			final Exception ex) {
		
		final DefaultErrorMessage defaultErrorMessage = new DefaultErrorMessage(
				StatusCodeEnum.EXCEPTION.name(), ex, DEFAULT_ERROR_MSG);
		
		LOG.error(ex.getMessage(), ex);
		
		return new ResponseEntity<Object>(defaultErrorMessage,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<Object> sqlException(
			final DataAccessException ex) {
		
		final DefaultErrorMessage defaultErrorMessage = new DefaultErrorMessage(
				StatusCodeEnum.DATABASE_EXCEPTION.name(), ex, DEFAULT_ERROR_MSG);
		
		LOG.error(ex.getMessage(), ex);
		
		return new ResponseEntity<Object>(defaultErrorMessage,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(GatewayException.class)
	public ResponseEntity<Object> integracaoException(
			final GatewayException ex) {

		final DefaultErrorMessage defaultErrorMessage = new DefaultErrorMessage(
				StatusCodeEnum.INTEGRATION_EXCEPTION.name(), ex, ex.getMessage());

		LOG.error(ex.getMessage(), ex);
		
		return new ResponseEntity<Object>(defaultErrorMessage,
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RecursoIntegracaoNaoEncontrado.class)
	public ResponseEntity<Object> recursoNaoEncontradoException(
			final RecursoIntegracaoNaoEncontrado ex) {

		final DefaultErrorMessage defaultErrorMessage = new DefaultErrorMessage(
				StatusCodeEnum.INTEGRATION_EXCEPTION.name(), ex, ex.getMessage());

		LOG.error(ex.getMessage(), ex);
		
		return new ResponseEntity<Object>(defaultErrorMessage,
				HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(JobException.class)
	public ResponseEntity<Object> jobException(
			final JobException ex) {

		final DefaultErrorMessage defaultErrorMessage = new DefaultErrorMessage(
				StatusCodeEnum.JOB_EXCEPTION.name(), ex, ex.getMessage());

		LOG.error(ex.getMessage(), ex);
		
		return new ResponseEntity<Object>(defaultErrorMessage,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<Object> numberParserException(
			final NumberFormatException ex) {
		
		final DefaultErrorMessage defaultErrorMessage = new DefaultErrorMessage(
				StatusCodeEnum.PARSER_EXCEPTION.name(), ex, ex.getMessage());
		
		LOG.error(ex.getMessage(), ex);
		
		return new ResponseEntity<Object>(defaultErrorMessage,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
