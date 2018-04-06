package br.com.totvs.cia.parametrizacao.perfilconciliacao.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;
import br.com.totvs.cia.parametrizacao.layout.converter.LayoutConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.ConfiguracaoPerfilJson;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.PerfilConciliacaoJson;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.RegraPerfilJson;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.ConfiguracaoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.Regra;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.StatusPerfilEnum;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.service.PerfilConciliacaoService;
@Component
public class PerfilConciliacaoConverter extends JsonConverter<PerfilConciliacao, PerfilConciliacaoJson> {
	
	@Autowired
	private PerfilConciliacaoService service;
	
	@Autowired
	private LayoutConverter layoutConverter;
	
	@Autowired
	private ConfiguracaoPerfilConciliacaoConverter configuracaoConverter;
	
	@Autowired
	private RegraPerfilConciliacaoConverter regraConverter;
	
	@Override
	public PerfilConciliacao convertFrom(final PerfilConciliacaoJson json) {
		try {
			if (json.getId() != null) {
				PerfilConciliacao perfilEncontrado = this.service.getOne(json.getId());
				perfilEncontrado.setStatus(StatusPerfilEnum.fromCodigo(json.getIdentificacao().getStatus().getCodigo()));
				return perfilEncontrado;
			}
			
			Layout layout = this.layoutConverter.convertFrom(json.getIdentificacao().getLayout());
			this.configuracaoConverter.of(layout);
			ConfiguracaoPerfilConciliacao configuracao = this.configuracaoConverter.convertFrom(json.getConfiguracao());
			
			this.regraConverter.of(configuracao);
			List<Regra> regras = this.regraConverter.convertListJsonFrom(json.getRegras());
			
			return new PerfilConciliacao(json, layout, configuracao, regras);
		} catch (Exception ex) {
			throw new ConverterException("Erro ao tentar converter PerfilConciliacaoJson to PerfilConciliacao", ex);
		}
	}
	
	@Override
	public PerfilConciliacaoJson convertFrom(final PerfilConciliacao model) {
		try {
			LayoutJson layout = this.layoutConverter.convertFrom(model.getLayout());
			ConfiguracaoPerfilJson configuracao = this.configuracaoConverter.convertFrom(model.getConfiguracao());
			List<RegraPerfilJson> regras = this.regraConverter.convertListModelFrom(model.getRegras());
			return new PerfilConciliacaoJson (model, layout, configuracao, regras);
		} catch (Exception ex) {
			throw new ConverterException("Erro ao tentar converter PerfilConciliacao to PerfilConciliacaoJson", ex);
		}
	}
}