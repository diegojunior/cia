package br.com.totvs.cia.parametrizacao.unidade.importacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.parametrizacao.unidade.importacao.model.CampoParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.CampoParametrizacaoUnidadeImportacaoRepository;

@Service
public class CampoParametrizacaoUnidadeImportacaoService {

	@Autowired
	private CampoParametrizacaoUnidadeImportacaoRepository campoRepository;

	public void delete(CampoParametrizacaoUnidadeImportacao campo) {
		campoRepository.delete(campo);
	}

	public void delete(Iterable<? extends CampoParametrizacaoUnidadeImportacao> campos) {
		campoRepository.delete(campos);
	}

	public List<CampoParametrizacaoUnidadeImportacao> findAll(Specification<CampoParametrizacaoUnidadeImportacao> spec) {
		return campoRepository.findAll(spec);
	}

	public List<CampoParametrizacaoUnidadeImportacao> findAll() {
		return campoRepository.findAll();
	}

	public CampoParametrizacaoUnidadeImportacao findOne(String id) {
		return campoRepository.findOne(id);
	}

	public <S extends CampoParametrizacaoUnidadeImportacao> S save(S campo) {
		return campoRepository.save(campo);
	}
}
