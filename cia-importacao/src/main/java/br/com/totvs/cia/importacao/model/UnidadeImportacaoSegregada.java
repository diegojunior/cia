package br.com.totvs.cia.importacao.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnidadeImportacaoSegregada {

	private final List<UnidadeImportacao> unidades = Lists.newArrayList();
	
	private final Map<ParametrizacaoUnidadeImportacaoChave, List<UnidadeLoteImportacao>> unidadesSegregadasPelaParametrizacao = Maps.newHashMap();
	
	/**
	 * Este método separa as unidades de lote por parametrização
	 * 
	 * @param parametrizacoes - Parametrizações de chaves configuradas
	 * @param unidadeLote - Unidade a verificar se pertence a parametrização
	 */
	public void encontrarUnidadeConfiguradaPorParametrizacao(final List<ParametrizacaoUnidadeImportacaoChave> parametrizacoes, 
			final UnidadeLoteImportacao unidadeLote) {
		for (ParametrizacaoUnidadeImportacaoChave parametrizacao : parametrizacoes) {
			if (parametrizacao.getSessoesChave().contains(unidadeLote.getSessao())) {
				List<UnidadeLoteImportacao> unidadesParametrizadas = this.unidadesSegregadasPelaParametrizacao.get(parametrizacao);
				if (unidadesParametrizadas == null) {
					this.unidadesSegregadasPelaParametrizacao.put(parametrizacao, Lists.newArrayList(unidadeLote));
				} else {
					unidadesParametrizadas.add(unidadeLote);
				}
			}
		}
	}

	/**
	 * Segregar as unidades que possuem relação entre elas pela parametrização. Exemplo:
	 * Na configuração de parametrização de unidade foi definido:
	 * Sessões: 32 e 31
	 * Chaves: Carteira
	 * Demais Campos: Qtd e Ativo
	 * Modelo:
	 * 32CARTEIRA_AGG100
	 * 31CARTEIRA_AGGPETR_4
	 * Irá gerar uma unidade com os campos:
	 * CARTEIRA_AGG, 100 e PETR_4
	 * Nota-se que que o metodo uniu informações das 2 linhas.
	 * 
	 * @param parametrizacoes - Parametrizações de unidade de chave configuradas
	 * @param importacao
	 */
	public void segregarUnidadesPorParametrizacao(final List<ParametrizacaoUnidadeImportacaoChave> parametrizacoes, 
			final Importacao importacao) {
		for (ParametrizacaoUnidadeImportacaoChave parametrizacao : parametrizacoes) {
			final List<UnidadeLoteImportacao> unidadesLotePorParametrizacao = this.unidadesSegregadasPelaParametrizacao.get(parametrizacao);
			Map<String, SegregadorUnidade> unidadeSegregadaPorChaves = this.efetuarMergeDasUnidadesParametrizadas(importacao, parametrizacao, unidadesLotePorParametrizacao);
			List<UnidadeImportacao> unidadesParametrizadas = this.filtrarUnidadesSegregadasComBaseRegra(unidadeSegregadaPorChaves, parametrizacao.getSessoes());
			this.unidades.addAll(unidadesParametrizadas);
		}
	}

	/**
	 * Método que verifica a chave de cada unidade para encontrar outra unidade com a mesma chave
	 * @param importacao - Utilizado para gerar a unidade de importacao
	 * @param parametrizacao - Utilizada para buscar as informações de chaves e demais campos
	 * @param unidadesLotePorParametrizacao - Unidades a verificar se possuem chaves iguais.
	 * @return - As unidades segregadas por chave.
	 */
	private Map<String, SegregadorUnidade> efetuarMergeDasUnidadesParametrizadas(final Importacao importacao,
			final ParametrizacaoUnidadeImportacaoChave parametrizacao,
			final List<UnidadeLoteImportacao> unidadesLotePorParametrizacao) {
		final Map<String, SegregadorUnidade> unidadeSegregadaPorChaves = Maps.newHashMap();
		for (UnidadeLoteImportacao unidadeLoteImportacao : unidadesLotePorParametrizacao) {
			Set<CampoLayoutImportacao> camposChaveLayout = unidadeLoteImportacao.getCamposIguaisDe(parametrizacao.getCamposChaves());
			Set<CampoLayoutImportacao> demaisCamposLayout = unidadeLoteImportacao.getCamposIguaisDe(parametrizacao.getDemaisCampos());
			String key = Integer.toString(camposChaveLayout.hashCode());
			SegregadorUnidade segregador = unidadeSegregadaPorChaves.get(key);
			if (segregador == null) {
				segregador = new SegregadorUnidade();
				final UnidadeImportacao unidadeImportacao = new UnidadeImportacao();
				segregador.adicionarUnidade(unidadeLoteImportacao.getSessao(), unidadeImportacao);
				unidadeImportacao.adicionarParametrizacaoUnidade(parametrizacao);
				unidadeImportacao.setImportacao(importacao);
				unidadeImportacao.convertAndAdd(camposChaveLayout);
				unidadeImportacao.convertAndAdd(demaisCamposLayout);
				unidadeSegregadaPorChaves.put(key, segregador);
			} else {
				segregador.adicionarCampos(unidadeLoteImportacao.getSessao(), demaisCamposLayout);
			}
		}
		return unidadeSegregadaPorChaves;
	}
	
	/**
	 * Após efetuar a segregação das unidades que possuem as mesmas chaves, deve-se ter certeza
	 * que será utilizada somente as unidades que possuem relação de chave em todas as sessões
	 * configuradas na parametrização. 
	 * Aplica-se a regra: 
	 * Caso você tenha parametrizado 3 sessões (s1, s2 e s3) com 1 campo chave em cada sessão (carteira) 
	 * e tenha configurado como demais campos apenas da sessão (s1 e s2) os campos (Ativo e Qtd).
	 * 31CARTPETR_4
	 * 32CART100
	 * 33AGORA20180228
	 * Com base nesse exemplo essas unidades não seriam segregadas porque a sessão 33 possui valor (AGORA) do campo 
	 * chave. O exemplo abaixo é o permitido. E vai gerar a unidade: Carteira: CART, Ativo: PETR_4 e QTd: 100 
	 * 31CARTPETR_4
	 * 32CART100
	 * 33CART20180228  
	 * @param unidadeSegregadaPorChaves - Unidades Segregadas pelas chaves.
	 * @param sessoesChaves - Sessões permitidas para efetuar o merge.
	 * @return Somente as unidades que aplicam a regra.
	 */
	private List<UnidadeImportacao> filtrarUnidadesSegregadasComBaseRegra(
			final Map<String, SegregadorUnidade> unidadeSegregadaPorChaves,
			final List<Sessao> sessoesChaves) {
		return unidadeSegregadaPorChaves
				.values()
				.stream()
				.filter(segregador -> segregador.getSessoes().containsAll(sessoesChaves))
				.map(segregador -> segregador.getUnidade())
				.collect(Collectors.toList());
	}
	
	class SegregadorUnidade {
		@Getter
		private final List<Sessao> sessoes = Lists.newArrayList();
		@Getter
		private UnidadeImportacao unidade;
		
		public void adicionarUnidade(final Sessao sessao, final UnidadeImportacao unidade) {
			Objects.requireNonNull(unidade);
			this.sessoes.add(sessao);
			this.unidade = unidade;
		}
		
		public void adicionarCampos(final Sessao sessao, final Set<CampoLayoutImportacao> campos) {
			this.sessoes.add(sessao);
			this.unidade.convertAndAdd(campos);
		}
	}

	
}
