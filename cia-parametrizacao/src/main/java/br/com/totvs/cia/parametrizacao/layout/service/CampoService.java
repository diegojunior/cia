package br.com.totvs.cia.parametrizacao.layout.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.repository.CampoRepository;

@Service
public class CampoService {
	
	@Autowired
	private CampoRepository campoRepository;
	
	public Campo findOne(final String id) {
		return this.campoRepository.findOne(id);
	}

	public <S extends Campo> S save(S entity) {
		return campoRepository.save(entity);
	}	
	
	
}
