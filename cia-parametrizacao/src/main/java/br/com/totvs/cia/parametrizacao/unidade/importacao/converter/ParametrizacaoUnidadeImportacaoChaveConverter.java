package br.com.totvs.cia.parametrizacao.unidade.importacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.LayoutConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.SessaoDefaultConverter;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.CampoParametrizacaoUnidadeImportacaoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ChaveParametrizacaoUnidadeImportacaoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoChaveJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.CampoParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ChaveParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoChaveService;
@Component
public class ParametrizacaoUnidadeImportacaoChaveConverter extends JsonConverter<ParametrizacaoUnidadeImportacaoChave, ParametrizacaoUnidadeImportacaoChaveJson>{

	@Autowired
	private LayoutConverter layoutConverter;
	
	@Autowired
	private CampoParamatrizacaoUnidadeImportacaoConverter campoUnidadeImportacaoConverter;
	
	@Autowired
	private ChaveParamatrizacaoUnidadeImportacaoConverter chaveUnidadeImportacaoConverter;
	
	@Autowired
	private SessaoDefaultConverter sessaoConverter;
	
	@Autowired
	private ParametrizacaoUnidadeImportacaoChaveService service;
	
	@Override
	public ParametrizacaoUnidadeImportacaoChaveJson convertFrom(final ParametrizacaoUnidadeImportacaoChave model) {
		final ParametrizacaoUnidadeImportacaoChaveJson json = new ParametrizacaoUnidadeImportacaoChaveJson();
		json.setId(model.getId());
		json.setCodigo(model.getCodigo());
		json.setDescricao(model.getDescricao());
		json.setTipoLayout(TipoLayoutEnumJson.fromCodigo(model.getTipoLayout().getCodigo()));
		json.setLayout(this.layoutConverter.convertFrom(model.getLayout()));
		for (final Sessao sessao : model.getSessoes()) {
			json.getSessoes().add(this.sessaoConverter.convertFrom(sessao));
		}
		for (final ChaveParametrizacaoUnidadeImportacao chave : model.getChaves()) {
			ChaveParametrizacaoUnidadeImportacaoJson chaveJson = this.chaveUnidadeImportacaoConverter.convertFrom(chave);
			json.getChavesUnidadeImportacao().add(chaveJson);
		}
		for (final CampoParametrizacaoUnidadeImportacao campo : model.getCamposUnidadeImportacao()) {
			json.getCamposUnidadeImportacao().add(this.campoUnidadeImportacaoConverter.convertFrom(campo));
		}
		return json;
	}

	@Override
	public ParametrizacaoUnidadeImportacaoChave convertFrom(final ParametrizacaoUnidadeImportacaoChaveJson json) {
		ParametrizacaoUnidadeImportacaoChave param = new ParametrizacaoUnidadeImportacaoChave();
		if (json.getId() != null) {
			param = this.service.getOne(json.getId());
		}
		param.setCodigo(json.getCodigo());
		param.setDescricao(json.getDescricao());
		param.setTipoLayout(TipoLayoutEnum.fromCodigo(json.getTipoLayout().getCodigo()));
		param.setLayout(this.layoutConverter.convertFrom(json.getLayout()));
		for (final SessaoJson sessao : json.getSessoes()) {
			param.atualizarSessao(this.sessaoConverter.convertFrom(sessao));
		}
		for (final ChaveParametrizacaoUnidadeImportacaoJson chave : json.getChavesUnidadeImportacao()) {
			param.atualizarChaveUnidade(this.chaveUnidadeImportacaoConverter.convertFrom(chave));
		}
		for (final CampoParametrizacaoUnidadeImportacaoJson campo : json.getCamposUnidadeImportacao()) {
			param.atualizarCampoUnidade(this.campoUnidadeImportacaoConverter.convertFrom(campo));
		}
		return param;
	}

}
