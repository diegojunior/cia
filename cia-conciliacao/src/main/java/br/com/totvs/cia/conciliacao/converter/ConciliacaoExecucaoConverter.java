package br.com.totvs.cia.conciliacao.converter;

import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.batch.item.file.transform.ConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.service.CargaService;
import br.com.totvs.cia.conciliacao.json.ConciliacaoExecucaoJson;
import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.service.PerfilConciliacaoService;

@Component
public class ConciliacaoExecucaoConverter extends JsonConverter<Conciliacao, ConciliacaoExecucaoJson> {
	
	private static final Logger log = Logger.getLogger(ConciliacaoExecucaoConverter.class); 
	
	@Autowired
	private PerfilConciliacaoService perfilConciliacaoService;
	
	@Autowired
	private ImportacaoService importacaoService;
	
	@Autowired
	private CargaService cargaService;
	
	@Override
	public ConciliacaoExecucaoJson convertFrom(final Conciliacao model) {
		return new ConciliacaoExecucaoJson(model);
	}

	@Override
	public Conciliacao convertFrom(final ConciliacaoExecucaoJson json) {
		try {
			final Date data = DateUtil.parse(json.getData(), DateUtil.yyyy_MM_dd);
			final PerfilConciliacao perfil = this.perfilConciliacaoService.findByCodigoFetchConfiguracaoAndRegras(json.getPerfil());
			final Importacao importacao = this.importacaoService.findBy(json.getImportacao());
			final Carga carga = this.cargaService.getCargaFetchLotesBy(json.getCarga()); 
			
			return new Conciliacao(data, perfil, importacao, carga, new Date());
		} catch (final ParseException e) {
			log.error(e);
			throw new ConversionException("Erro ao realizar o Parse da Data: "+ json.getData());
		}
	}
}