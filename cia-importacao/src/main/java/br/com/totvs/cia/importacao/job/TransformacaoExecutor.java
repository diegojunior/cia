package br.com.totvs.cia.importacao.job;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.importacao.model.CampoLayoutImportacao;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import br.com.totvs.cia.parametrizacao.transformacao.model.TransformacaoStrategy;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoContextService;

@Component
public class TransformacaoExecutor {
	
	private final TransformacaoContextService transformacaoContext;
	
	private Map<Transformacao, TransformacaoStrategy> transformacoesSegregadas;
	
	@Autowired
	public TransformacaoExecutor(final TransformacaoContextService transformacaoContext) {
		
		this.transformacaoContext = transformacaoContext;
		
	}
	
	public void config(final SistemaEnum sistema, 
			final List<Transformacao> transformacoes) {
		this.transformacoesSegregadas = Maps.newHashMap();
		for (final Transformacao transformacao : transformacoes) {
			final TransformacaoStrategy strategy = this.transformacaoContext.loadStrategies(sistema, transformacao);
			transformacoesSegregadas.put(transformacao, strategy);
		}
	}
	
	public void transformar(final Collection<CampoLayoutImportacao> campos, 
			final List<Transformacao> transformacoes) {
		Objects.requireNonNull(transformacoesSegregadas, "Deve-se executar o metodo para configurar as transformações");
		for (final Transformacao transformacao : transformacoes) {
			final TransformacaoStrategy strategy = this.transformacoesSegregadas.get(transformacao);
			final CampoLayoutImportacao campoLayoutImportacao = campos
																	.stream()
																	.filter(new Filtro(transformacao))
																	.findAny()
																	.orElse(null);
			if (campoLayoutImportacao != null) {
				final String valorTransformado = strategy.transform(campoLayoutImportacao.getValor());
				campoLayoutImportacao.atualizaValor(valorTransformado);
			}
		}
		
	}
	
	
	
	public class Filtro implements Predicate<CampoLayoutImportacao> {
		
		private Transformacao transformacao;

		public Filtro(Transformacao transformacao) {
			this.transformacao = transformacao;
			
		}

		@Override
		public boolean test(CampoLayoutImportacao t) {
			return t.getCampo().equals(this.transformacao.getCampo().getDominio())
					&& t.getUnidade().getSessao().equals(this.transformacao.getSessao());
		}
		
	}
	
}