package br.com.totvs.cia.infra.async;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import br.com.totvs.cia.infra.exception.CiaCustomAsyncExceptionHandler;

@Configuration
@EnableAsync
public class CiaAsync implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new CiaCustomAsyncExceptionHandler();
	}

}
