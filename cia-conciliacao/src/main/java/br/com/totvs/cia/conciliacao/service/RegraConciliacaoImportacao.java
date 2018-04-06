package br.com.totvs.cia.conciliacao.service;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.totvs.cia.importacao.model.UnidadeImportacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.Regra;

public class RegraConciliacaoImportacao extends AbstractRegraConciliacao {
	
	private PerfilConciliacao perfil;
	
	public RegraConciliacaoImportacao (final PerfilConciliacao perfil) {
		this.perfil = perfil;
	}
	
	public List<UnidadeImportacao> aplicar (final List<UnidadeImportacao> unidades) {
		List<UnidadeImportacao> unidadesComRegrasAplicadas = Lists.newArrayList(unidades);
		
		for (Regra regra : this.perfil.listRegrasImportacao()) {
			
			List<UnidadeImportacao> unidadesReduzidas = Lists.newArrayList(unidadesComRegrasAplicadas);
			for (UnidadeImportacao unidade : unidadesReduzidas) {
				Boolean isUnidadeEnquadradaNaRegra = unidade.getCampos()
														 .stream()
														 .filter(campo -> campo.getCampo().equals(regra.getCampoImportacao().getDominio()) && 
																		  this.isEnquadrado(regra, campo.getValor()))
														 .findFirst()
														 .isPresent();
				if (!isUnidadeEnquadradaNaRegra) {
					unidadesComRegrasAplicadas.remove(unidade);
				}
			}
			
			if (unidadesComRegrasAplicadas.isEmpty()) {
				return Lists.newArrayList();
			}
		}
		
		return Lists.newArrayList(unidadesComRegrasAplicadas);
	}
}