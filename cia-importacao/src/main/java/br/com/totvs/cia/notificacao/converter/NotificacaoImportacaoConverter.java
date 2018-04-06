package br.com.totvs.cia.notificacao.converter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import br.com.totvs.cia.importacao.json.ImportacaoJson;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;
import br.com.totvs.cia.notificacao.json.ImportacaoComNotificacoesJson;
import br.com.totvs.cia.notificacao.json.NotificacaoJson;
import br.com.totvs.cia.notificacao.json.StatusEnumJson;
import br.com.totvs.cia.notificacao.model.NotificacaoImportacao;

@Component
public class NotificacaoImportacaoConverter extends JsonConverter<NotificacaoImportacao, ImportacaoComNotificacoesJson> {

	@Override
	public NotificacaoImportacao convertFrom(final ImportacaoComNotificacoesJson json) {
		throw new ConverterException("Conversão de Json para Model não prevista");
	} 
	
	@Override
	public ImportacaoComNotificacoesJson convertFrom(final NotificacaoImportacao model) {
		throw new ConverterException("Conversão de Model para Json não prevista");
	}
	
	@Override
	public List<ImportacaoComNotificacoesJson> convertListModelFrom(final List<NotificacaoImportacao> notificacoes) {
		final List<ImportacaoComNotificacoesJson> notificacoesImportadas = Lists.newArrayList();
		final Map<Importacao, List<NotificacaoImportacao>> notificacoesPorImportacao = notificacoes
				.stream()
				.collect(Collectors.groupingBy(NotificacaoImportacao::getImportacao));
		
		for (Entry<Importacao, List<NotificacaoImportacao>> entry : notificacoesPorImportacao
				.entrySet()) {
			ImportacaoComNotificacoesJson notificacao = new ImportacaoComNotificacoesJson(new ImportacaoJson(entry.getKey()));
			
			for (NotificacaoImportacao notificacaoModel : entry.getValue()) {
				NotificacaoJson notificacaoJson = new NotificacaoJson(
						notificacaoModel.getId(), 
						notificacaoModel.getData(), 
						StatusEnumJson.fromCodigo(notificacaoModel.getStatus().getCodigo()), 
						notificacaoModel.getMensagem());
				
				notificacao.getNotificacoes().add(notificacaoJson);
			}
			notificacoesImportadas.add(notificacao);
		}
		
		return notificacoesImportadas;
	}

}
