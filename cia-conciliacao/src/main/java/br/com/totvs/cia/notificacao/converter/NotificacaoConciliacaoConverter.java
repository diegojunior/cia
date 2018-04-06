package br.com.totvs.cia.notificacao.converter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import br.com.totvs.cia.conciliacao.json.ConciliacaoResumidaJson;
import br.com.totvs.cia.conciliacao.json.LoteConciliacaoJson;
import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;
import br.com.totvs.cia.notificacao.json.ConciliacaoComNotificacoesJson;
import br.com.totvs.cia.notificacao.json.NotificacaoConciliacaoJson;
import br.com.totvs.cia.notificacao.json.NotificacaoLoteConciliacaoJson;
import br.com.totvs.cia.notificacao.model.NotificacaoConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.converter.PerfilConciliacaoConverter;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.ConfiguracaoPerfilJson;

@Component
public class NotificacaoConciliacaoConverter extends JsonConverter<NotificacaoConciliacao, ConciliacaoComNotificacoesJson> {
	

	@Autowired
	private PerfilConciliacaoConverter perfilConverter;
	
	@Override
	public NotificacaoConciliacao convertFrom(final ConciliacaoComNotificacoesJson json) {
		throw new ConverterException("Conversão de Json para Model não prevista");
	}
	
	@Override
	public ConciliacaoComNotificacoesJson convertFrom(final NotificacaoConciliacao model) {
		throw new ConverterException("Conversão de Model para Json não prevista");
	}
	
	@Override
	public List<ConciliacaoComNotificacoesJson> convertListModelFrom(final List<NotificacaoConciliacao> notificacoes) {
		
		List<ConciliacaoComNotificacoesJson> conciliacoesComNotificacao = Lists.newArrayList();
		
		Map<Conciliacao, List<NotificacaoConciliacao>> notificacaoPorLote = notificacoes
			.stream()
			.collect(Collectors.groupingBy(NotificacaoConciliacao::getConciliacao));
		
		for (Entry<Conciliacao, List<NotificacaoConciliacao>> entry : notificacaoPorLote.entrySet()) {
			
			Conciliacao conciliacao = entry.getKey();
			
			ConciliacaoResumidaJson conciliacaoResumidaJson = new ConciliacaoResumidaJson(conciliacao, 
					this.perfilConverter.convertFrom(conciliacao.getPerfil()));
			
			ConciliacaoComNotificacoesJson conciliacaoComNotificacao = new ConciliacaoComNotificacoesJson(conciliacaoResumidaJson);
			
			ConfiguracaoPerfilJson configuracaoPerfilJson = new ConfiguracaoPerfilJson(conciliacao.getPerfil().getConfiguracao(), 
					Lists.newArrayList());
				
			LoteConciliacaoJson loteConciliacaoJson = new LoteConciliacaoJson(conciliacao.getLote(), configuracaoPerfilJson);
			
			NotificacaoLoteConciliacaoJson notificacaoLoteJson = new NotificacaoLoteConciliacaoJson(loteConciliacaoJson);
			
			notificacaoPorLote.get(conciliacao)
				.stream()
				.sorted((n1, n2) -> n1.getData().compareTo(n2.getData()))
				.collect(Collectors.toList())
				.forEach(notificacao -> {
					notificacaoLoteJson.getNotificacoes().add(new NotificacaoConciliacaoJson(notificacao));
				});
			
			conciliacaoComNotificacao.getNotificacaoLote().add(notificacaoLoteJson);	
		
			conciliacoesComNotificacao.add(conciliacaoComNotificacao);
		
		}
		
		return conciliacoesComNotificacao;
	}

}
