package br.com.totvs.cia.parametrizacao.layout.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.repository.SessaoRepository;

@Service
public class SessaoService {

	@Autowired
	private SessaoRepository sessaoRepository;
		
	public Sessao getBy(final String codigo) {
		return this.sessaoRepository.getByCodigo(codigo);
	}

	public <S extends Sessao> S save(S entity) {
		return this.sessaoRepository.save(entity);
	}

	public List<Sessao> findAll() {
		return this.sessaoRepository.findAll();
	}

	public Sessao findOne(String id) {
		return this.sessaoRepository.findOne(id);
	}

	public void delete(Sessao entity) {
		this.sessaoRepository.delete(entity);
	}

	public void deleteAll() {
		this.sessaoRepository.deleteAll();
	}
	
}