package br.com.totvs.cia.importacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.importacao.model.Arquivo;
import br.com.totvs.cia.importacao.repository.ArquivoRepository;

@Service
public class ArquivoService {

	@Autowired
	private ArquivoRepository arquivoRepository;

	public <S extends Arquivo> S save(final S entity) {
		return this.arquivoRepository.save(entity);
	}

	public Arquivo getOne(final String id) {
		return this.arquivoRepository.getOne(id);
	}

	public void delete(final Arquivo entity) {
		this.arquivoRepository.delete(entity);
	}
	
}
