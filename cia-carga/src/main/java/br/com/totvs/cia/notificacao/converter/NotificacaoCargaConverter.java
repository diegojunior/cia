package br.com.totvs.cia.notificacao.converter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import br.com.totvs.cia.carga.json.CargaResumidaJson;
import br.com.totvs.cia.carga.json.LoteCargaJson;
import br.com.totvs.cia.carga.json.NotificacaoLoteCargaJson;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;
import br.com.totvs.cia.notificacao.json.CargaComNotificacoesJson;
import br.com.totvs.cia.notificacao.json.NotificacaoCargaJson;
import br.com.totvs.cia.notificacao.model.NotificacaoCarga;

@Component
public class NotificacaoCargaConverter extends JsonConverter<NotificacaoCarga, CargaComNotificacoesJson> {
	
	private static final Logger log = Logger.getLogger(NotificacaoCargaConverter.class);

	@Override
	public CargaComNotificacoesJson convertFrom(final NotificacaoCarga model) {
		throw new ConverterException("Conversão de Model para Json não prevista");
	}
	
	@Override
	public List<CargaComNotificacoesJson> convertListModelFrom(final List<NotificacaoCarga> notificacoes) {
		try {
			final List<CargaComNotificacoesJson> cargaComNotificacoes = Lists.newArrayList();
			
			final Map<LoteCarga, List<NotificacaoCarga>> notificacoesPorLote = notificacoes
				.stream()
				.collect(Collectors.groupingBy(NotificacaoCarga::getLoteCarga));
			
			Map<Carga, List<LoteCarga>> cargaPorLote = notificacoesPorLote
					.keySet().stream()
					.collect(Collectors.groupingBy(LoteCarga::getCarga));
			
			for (Entry<Carga, List<LoteCarga>> entryCarga : cargaPorLote.entrySet()) {
				CargaResumidaJson cargaResumidaJson = new CargaResumidaJson(entryCarga.getKey());
				CargaComNotificacoesJson cargaComNotificacoesJson = new CargaComNotificacoesJson(cargaResumidaJson);
				for (LoteCarga lote : entryCarga.getValue()) {
					LoteCargaJson loteCargaJson = new LoteCargaJson(lote);
					NotificacaoLoteCargaJson notificacaoLoteCargaJson = new NotificacaoLoteCargaJson(loteCargaJson);
					notificacoesPorLote.get(lote)
						.stream()
						.sorted((n1, n2) -> n1.getData().compareTo(n2.getData()))
						.collect(Collectors.toList())
						.forEach(notificacao -> {
							notificacaoLoteCargaJson.getNotificacoes().add(new NotificacaoCargaJson(notificacao));
						});
					cargaComNotificacoesJson.getLotesCarga().add(notificacaoLoteCargaJson);
				}
				Collections.sort(cargaComNotificacoesJson.getLotesCarga());
				cargaComNotificacoes.add(cargaComNotificacoesJson);
			}
			
			return cargaComNotificacoes;
		} catch (Exception e) {
			log.error("Ocorreu erro ao converter as notificacoes de carga.", e);
			throw new ConverterException(e);
		}
	}

	@Override
	public NotificacaoCarga convertFrom(final CargaComNotificacoesJson json) {
		throw new ConverterException("Conversão de Json para Model não prevista");
	} 
}