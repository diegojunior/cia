package br.com.totvs.cia.infra.exception;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.google.common.collect.Lists;

@Data
public class DefaultErrorMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;

	private String status;

	private List<String> errors = Lists.newArrayList();
	
	private Throwable stack;
	
	public DefaultErrorMessage(final String code, final String status, Throwable stack, 
			final String error) {
		this.code = code;
		this.status = status;
		this.stack = stack;
		this.errors.add(error);
	}
	
	public DefaultErrorMessage(final String status, Throwable stack, 
			final String error) {
		this.status = status;
		this.stack= stack;
		this.errors.add(error);
	}
	
	public String getStackTrace() {
		StringBuilder sb = new StringBuilder();
		if (stack != null) {
			sb.append(stack.toString()).append(": ").append(stack.getMessage()).append("\n");
			for (StackTraceElement trace : stack.getStackTrace()) {
				sb.append("\tat " + trace.toString()).append("\n");
			}
			Throwable cause = stack.getCause();
			if (cause != null) {
				traceCause(sb, cause);
			}
		}
		return sb.toString();
	}
	
	public void traceCause(StringBuilder sb, Throwable e) {
		sb.append("Caused by: ").append(e.toString());
		for (StackTraceElement trace : e.getStackTrace()) {
			sb.append("\tat " + trace.toString()).append("\n");
		}
		Throwable cause = e.getCause();
		if (cause != null) {
			traceCause(sb, cause);
		}
	}
}
