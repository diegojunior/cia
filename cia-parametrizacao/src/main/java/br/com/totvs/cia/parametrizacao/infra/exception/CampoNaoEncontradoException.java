package br.com.totvs.cia.parametrizacao.infra.exception;

import br.com.totvs.cia.infra.exception.CiaBusinessException;


public class CampoNaoEncontradoException extends CiaBusinessException {

	private static final long serialVersionUID = 1L;

	public CampoNaoEncontradoException(String message) {
		super(message);
	}


}
