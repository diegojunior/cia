package br.com.totvs.cia.parametrizacao.unidade.importacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.LayoutConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.SessaoDelimitadorConverter;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoBlocoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoBlocoService;

@Component
public class ParametrizacaoUnidadeImportacaoBlocoConverter extends JsonConverter<ParametrizacaoUnidadeImportacaoBloco, ParametrizacaoUnidadeImportacaoBlocoJson>{

	@Autowired
	private LayoutConverter layoutConverter;
	
	@Autowired
	private SessaoDelimitadorConverter sessaoConverter;
	
	@Autowired
	private LinhaBlocoParametrizacaoUnidadeImportacaoConverter linhaBlocoConverter;
	
	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoService service;
	
	@Override
	public ParametrizacaoUnidadeImportacaoBlocoJson convertFrom(final ParametrizacaoUnidadeImportacaoBloco model) {
		final ParametrizacaoUnidadeImportacaoBlocoJson json = new ParametrizacaoUnidadeImportacaoBlocoJson();
		json.setId(model.getId());
		json.setCodigo(model.getCodigo());
		json.setDescricao(model.getDescricao());		
		json.setLayout(this.layoutConverter.convertFrom(model.getLayout()));
		json.setSessaoAbertura(this.sessaoConverter.convertFrom(model.getAbertura()));
		json.setSessaoFechamento(this.sessaoConverter.convertFrom(model.getFechamento()));
		json.setLinhas(this.linhaBlocoConverter.convertListModelFrom(model.getLinhas()));
		return json;
	}

	@Override
	public ParametrizacaoUnidadeImportacaoBloco convertFrom(final ParametrizacaoUnidadeImportacaoBlocoJson json) {
		ParametrizacaoUnidadeImportacaoBloco param = new ParametrizacaoUnidadeImportacaoBloco();
		if (json.getId() != null) {
			param = this.service.findOne(json.getId());
		}
		param.setCodigo(json.getCodigo());
		param.setDescricao(json.getDescricao());
		param.setTipoLayout(TipoLayoutEnum.DELIMITADOR);
		param.setLayout(this.layoutConverter.convertFrom(json.getLayout()));
		param.setAbertura(this.sessaoConverter.convertFrom(json.getSessaoAbertura()));
		param.setFechamento(this.sessaoConverter.convertFrom(json.getSessaoFechamento()));
		param.setLinhas(this.linhaBlocoConverter.convertListJsonFrom(json.getLinhas()));
		param.atualizaLinhas(param);
		return param;
	}
}