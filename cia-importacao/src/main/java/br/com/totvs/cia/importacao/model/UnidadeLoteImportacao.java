package br.com.totvs.cia.importacao.model;

import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UnidadeLoteImportacao {

	private Sessao sessao;

	private List<CampoLayoutImportacao> campos = Lists.newArrayList();
	

	public CampoLayoutImportacao getCampoPorDominio(final Campo campo) {
		return Iterables.find(this.campos, new Predicate<CampoLayoutImportacao>() {
			@Override
			public boolean apply(final CampoLayoutImportacao campoImportacao) {
				return campoImportacao.getCampo().equals(campo.getDominio());
			}
		});
	}

	public Set<CampoLayoutImportacao> getCamposIguaisDe(final Set<Campo> camposParam) {
		final Set<CampoLayoutImportacao> campos = Sets.newHashSet();
		for (final Campo campo : camposParam) {
			final CampoLayoutImportacao campoLayoutImportacao = Iterables.tryFind(this.campos, new Predicate<CampoLayoutImportacao>() {
				
				@Override
				public boolean apply(final CampoLayoutImportacao input) {
					return input.getCampo().equals(campo.getDominio());
				}
			}).orNull();
			
			if (campoLayoutImportacao != null) {
				campos.add(campoLayoutImportacao);
			}
			
		}
		return campos;
	}

	public String generateHash(final Set<Campo> camposChaves) {
		Set<CampoLayoutImportacao> camposIguaisDe = this.getCamposIguaisDe(camposChaves);
		return String.valueOf(camposIguaisDe.hashCode());
	}
	
}
