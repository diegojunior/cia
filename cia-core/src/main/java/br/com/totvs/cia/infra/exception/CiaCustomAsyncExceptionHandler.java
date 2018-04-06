package br.com.totvs.cia.infra.exception;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class CiaCustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	private static final Logger LOG = Logger.getLogger(CiaCustomAsyncExceptionHandler.class);
	
	@Override
	public void handleUncaughtException(final Throwable throwable, final Method method, final Object... obj) {
		LOG.error("Ocorreu um erro ao executar a conciliação.", throwable);
		LOG.error("Método onde se ocorreu o erro: " + method.getName());
		for (final Object param : obj) {
			LOG.error("Parameter value: " + param);
		}

	}

}
