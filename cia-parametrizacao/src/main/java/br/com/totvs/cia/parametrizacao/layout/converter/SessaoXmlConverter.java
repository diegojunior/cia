package br.com.totvs.cia.parametrizacao.layout.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.service.SessaoService;

@Component
public class SessaoXmlConverter extends JsonConverter<Sessao, SessaoJson> {

	@Autowired
	private SessaoService sessaoService;

	@Autowired
	private CampoConverter campoConverter;

	@Override
	public SessaoJson convertFrom(final Sessao model) {
		final SessaoJson sessaoJson = new SessaoJson(model);
		for (final Campo campo : model.getCampos()) {
			final CampoJson campoJson = this.campoConverter.convertFrom(campo);
			sessaoJson.addCampo(campoJson);
		}

		return sessaoJson;
	}

	@Override
	public Sessao convertFrom(final SessaoJson json) {
		if (json != null) {
			final Sessao sessao = new Sessao();
			if (json.getId() != null 
					&& !"".equals(json.getId())) {
				return this.sessaoService.findOne(json.getId());
			}
			sessao.setCodigo(json.getCodigo());
			sessao.setNome(json.getNome());
			sessao.setTamanho(json.getTamanho());
			sessao.setSemSessaoConfigurada(json.isSemSessaoConfigurada());
			sessao.getCampos().clear();
			for (final CampoJson campoJson : json.getCampos()) {
				sessao.addCampo(this.campoConverter.convertFrom(campoJson));
			}

			return sessao;

		}

		return null;
	}
}