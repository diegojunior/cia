package br.com.totvs.cia.parametrizacao.layout.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.dominio.converter.DominioConverter;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.service.CampoService;

@Component
public class CampoConverter extends JsonConverter<Campo, CampoJson> {

	@Autowired
	private DominioConverter dominioConverter;

	@Autowired
	private CampoService campoService;

	@Override
	public CampoJson convertFrom(final Campo model) {
		return new CampoJson(model.getId(), model.getOrdem(), this.dominioConverter.convertFrom(model.getDominio()), model.getDescricao(),
				model.getTag(), model.getTamanho(), model.getPosicaoInicial(), model.getPosicaoFinal(),
				model.getPattern());
	}

	@Override
	public Campo convertFrom(final CampoJson json) {
		Campo campo = new Campo();
		if (json.getId() != null
				&& !"".equals(json.getId())) {
			campo = this.campoService.findOne(json.getId());
		}
		campo.setDominio(this.dominioConverter.convertFrom(json.getDominio()));
		campo.setOrdem(json.getOrdem());
		campo.setTag(json.getTag());
		campo.setDescricao(json.getDescricao());
		campo.setPosicaoInicial(json.getPosicaoInicial());
		campo.setPosicaoFinal(json.getPosicaoFinal());
		campo.setTamanho(json.getTamanho());
		campo.setPattern(json.getPattern());
		return campo;
	}

}
