package br.com.totvs.cia.importacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.importacao.json.ArquivoJson;
import br.com.totvs.cia.importacao.model.Arquivo;
import br.com.totvs.cia.importacao.service.ArquivoService;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class ArquivoConverter extends JsonConverter<Arquivo, ArquivoJson> {

	@Autowired
	private ArquivoService arquivoService;
	
	@Override
	public ArquivoJson convertFrom(final Arquivo model) {
		return new ArquivoJson(model.getId(), model.getFileName(), model.getFileSize());
	}

	@Override
	public Arquivo convertFrom(final ArquivoJson json) {
		Arquivo arquivo = new Arquivo();
		if (json.getId() != null) {
			arquivo = this.arquivoService.getOne(json.getId());
		} 		
		return arquivo;
	}

}
