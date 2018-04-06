package br.com.totvs.cia.parametrizacao.unidade.importacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.parametrizacao.unidade.importacao.model.AbstractParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.AbstractParametrizacaoUnidadeImportacaoRepository;

@Service
public class ParametrizacaoUnidadeImportacaoService {

	@Autowired
	private AbstractParametrizacaoUnidadeImportacaoRepository repository;

	public List<AbstractParametrizacaoUnidadeImportacao> listByLayout(final String layout) {
		return this.repository.listByLayout(layout);
	}

	public AbstractParametrizacaoUnidadeImportacao getOne(final String id) {
		return this.repository.findOne(id);
	}

	public AbstractParametrizacaoUnidadeImportacao getBy(final String codigo) {
		return this.repository.findByCodigo(codigo);
	}
}