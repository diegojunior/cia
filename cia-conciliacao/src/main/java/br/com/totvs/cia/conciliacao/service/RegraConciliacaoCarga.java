package br.com.totvs.cia.conciliacao.service;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.totvs.cia.carga.model.UnidadeCarga;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.Regra;

public class RegraConciliacaoCarga extends AbstractRegraConciliacao {
	
	private PerfilConciliacao perfil;
	
	public RegraConciliacaoCarga (final PerfilConciliacao perfil) {
		this.perfil = perfil;
	}
	
	public List<UnidadeCarga> aplicar (final List<UnidadeCarga> unidades) {
		List<UnidadeCarga> unidadesComRegrasAplicadas = Lists.newArrayList(unidades);
		
		for (Regra regra : this.perfil.listRegrasCarga()) {
			
			List<UnidadeCarga> unidadesReduzidas = Lists.newArrayList(unidadesComRegrasAplicadas);
			for (UnidadeCarga unidade : unidadesReduzidas) {
				Boolean isUnidadeEnquadradaNaRegra = unidade.getCampos()
														 .stream()
														 .filter(campo -> campo.getCampoServico().equals(regra.getCampoCarga()) && 
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