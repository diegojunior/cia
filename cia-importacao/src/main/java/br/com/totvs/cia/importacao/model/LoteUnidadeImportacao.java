package br.com.totvs.cia.importacao.model;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import lombok.Getter;

@Getter
public class LoteUnidadeImportacao {

	private final List<UnidadeLoteImportacao> unidades = Lists.newArrayList();
	
	public void adicionarUnidadeLote(final UnidadeLoteImportacao unidade) {
		this.unidades.add(unidade);
	}

	public UnidadeLoteImportacao getUnidadeLotePor(final Sessao sessao) {
		return Iterables.tryFind(this.getUnidades(), new Predicate<UnidadeLoteImportacao>() {

			@Override
			public boolean apply(final UnidadeLoteImportacao unidade) {
				return unidade.getSessao().equals(sessao);
			}
		}).orNull();
	}
}