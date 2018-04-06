package br.com.totvs.cia.parametrizacao.layout.model;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;

public interface Layout extends Model {

	public String getCodigo();

	public String getDescricao();

	public List<Dominio> getDominios();

	public TipoLayoutEnum getTipoLayout();
	
	Campo getCampoSessaoBy (final String codigoSessao, final String codigoDominio);

	default List<Sessao> getSessoes() {
		return Lists.newArrayList();
	}

	Sessao getBy(final String codigoSessao);
	
	TipoDelimitadorEnum getTipoDelimitador();
}
