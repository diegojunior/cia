package br.com.totvs.cia.parametrizacao.unidade.importacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.parametrizacao.unidade.importacao.model.LinhaBlocoParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.LinhaBlocoParametrizacaoUnidadeImportacaoRepository;

@Service
public class LinhaBlocoParametrizacaoUnidadeImportacaoService {

	@Autowired
	private LinhaBlocoParametrizacaoUnidadeImportacaoRepository repository;
	
	public LinhaBlocoParametrizacaoUnidadeImportacao findOne (final String id) {
		return this.repository.findOne(id);
	}
}