package br.com.totvs.cia.importacao.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import br.com.totvs.cia.importacao.json.CampoImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.model.CampoLayoutImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.infra.converter.JsonObjectConverter;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.dominio.service.DominioService;
import br.com.totvs.cia.parametrizacao.layout.service.SessaoService;

@Component
public class UnidadeLayoutImportacaoConverter extends JsonObjectConverter<UnidadeLayoutImportacao, UnidadeImportacaoProcessamentoJson> {

	@Autowired
	private DominioService dominioService;

	@Autowired
	private SessaoService sessaoService;

	@Override
	public Json convertFrom(final UnidadeLayoutImportacao model) {
		return null;
	}

	@Override
	public UnidadeLayoutImportacao convertFrom(final UnidadeImportacaoProcessamentoJson json) {
		final UnidadeLayoutImportacao unidadeImportacao = new UnidadeLayoutImportacao();
		for (final CampoImportacaoProcessamentoJson campo : json.getCampos()) {
			final CampoLayoutImportacao campoConciliacao = new CampoLayoutImportacao();
			final Dominio dominio = this.dominioService.getBy(campo.getCampo());
			campoConciliacao.setCampo(dominio);
			campoConciliacao.setValor(campo.getValor());
			unidadeImportacao.getCampos().add(campoConciliacao);
		}

		return unidadeImportacao;
	}

	public UnidadeLayoutImportacao convertFrom(final UnidadeImportacaoProcessamentoJson json, final List<Dominio> dominios) {
		final UnidadeLayoutImportacao unidadeImportacao = new UnidadeLayoutImportacao();
		unidadeImportacao.setSessao(this.sessaoService.findOne(json.getIdSessao()));
		for (final CampoImportacaoProcessamentoJson campo : json.getCampos()) {
			final CampoLayoutImportacao campoImportacao = new CampoLayoutImportacao();
			final Dominio dominio = this.filtrar(dominios, campo.getCampo());
			campoImportacao.setCampo(dominio);
			campoImportacao.setValor(campo.getValor().trim());
			campoImportacao.setUnidade(unidadeImportacao);
			campoImportacao.setPattern(campo.getPattern());
			unidadeImportacao.getCampos().add(campoImportacao);
		}

		return unidadeImportacao;
	}

	private Dominio filtrar(final List<Dominio> dominios, final String codigoDominio) {
		return Iterables.find(dominios, new Predicate<Dominio>() {
			@Override
			public boolean apply(final Dominio input) {
				return input.getCodigo().equalsIgnoreCase(codigoDominio);
			}
		});
	}

}
