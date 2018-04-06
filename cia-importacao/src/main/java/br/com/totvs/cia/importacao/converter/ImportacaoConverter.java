package br.com.totvs.cia.importacao.converter;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.agente.converter.AgenteConverter;
import br.com.totvs.cia.cadastro.agente.json.AgenteJson;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.converter.EquivalenciaConverter;
import br.com.totvs.cia.cadastro.equivalencia.converter.RemetenteConverter;
import br.com.totvs.cia.cadastro.equivalencia.json.EquivalenciaJson;
import br.com.totvs.cia.cadastro.equivalencia.json.RemetenteJson;
import br.com.totvs.cia.cadastro.equivalencia.model.Equivalencia;
import br.com.totvs.cia.importacao.json.ArquivoJson;
import br.com.totvs.cia.importacao.json.ImportacaoJson;
import br.com.totvs.cia.importacao.json.StatusImportacaoJsonEnum;
import br.com.totvs.cia.importacao.json.TipoImportacaoEnumJson;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.TipoImportacaoEnum;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.notificacao.model.NotificacaoImportacao;
import br.com.totvs.cia.notificacao.model.StatusEnum;
import br.com.totvs.cia.notificacao.service.NotificacaoImportacaoService;
import br.com.totvs.cia.parametrizacao.layout.converter.LayoutConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
@Component
public class ImportacaoConverter extends JsonConverter<Importacao, ImportacaoJson> {

	@Autowired
	private LayoutConverter layoutConverter;
	
	@Autowired
	private AgenteConverter agenteConverter;
	
	@Autowired
	private RemetenteConverter remetenteConverter;
	
	@Autowired
	private EquivalenciaConverter equivalenciaConverter;
	
	@Autowired
	private ImportacaoService importacaoService;
	
	@Autowired
	private NotificacaoImportacaoService notificacaoImportacaoService;
	
	@Override
	public ImportacaoJson convertFrom(final Importacao model) {
		final ArquivoJson arquivo = new ArquivoJson(model.getArquivo().getId(), model.getArquivo().getFileName(), model.getArquivo().getFileSize());
		final SistemaJsonEnum sistema = model.getSistema() != null ? SistemaJsonEnum.fromCodigo(model.getSistema().getCodigo()) : null;
		final TipoImportacaoEnumJson tipoImportacao = TipoImportacaoEnumJson.fromCodigo(model.getTipoImportacao().getCodigo());
		final NotificacaoImportacao notificacao = this.notificacaoImportacaoService.findLast(model);
		StatusEnum status = StatusEnum.INICIADO;
		if (notificacao != null) {
			status = notificacao.getStatus();
		}
		final LayoutJson layout = this.layoutConverter.convertFrom(model.getLayout());
		final AgenteJson agente = model.getAgente() != null ? this.agenteConverter.convertFrom(model.getAgente()) : null;
		final RemetenteJson remetente = model.getRemetente() != null ? this.remetenteConverter.convertFrom(model.getRemetente()) : null;
		final List<EquivalenciaJson> equivalencias = model.getEquivalencias() != null ? this.equivalenciaConverter.convertListModelFrom(model.getEquivalencias()) : Lists.newArrayList(); 
		final String dataImportacao = DateUtil.format(model.getDataImportacao(), DateUtil.yyyy_MM_dd);
		return new ImportacaoJson(model.getId(), dataImportacao, StatusImportacaoJsonEnum.fromCodigo(status.getCodigo()), sistema, layout, tipoImportacao, arquivo, agente, remetente, equivalencias);
	}

	@Override
	public Importacao convertFrom(final ImportacaoJson json) {
		
		Importacao importacao = null;
		List<Equivalencia> equivalencias = Lists.newArrayList();
		
		if (json.getId() != null) {
			importacao = this.importacaoService.findOne(json.getId());
		} else {
			importacao = new Importacao();
		}
		try {
			importacao.setDataImportacao(DateUtil.parse(json.getDataImportacao(), DateUtil.yyyy_MM_dd));
		} catch (final ParseException e) {
			throw new RuntimeException("Erro ao converter as datas de importação.");
		}
		importacao.setSistema(json.getSistema() != null ? SistemaEnum.fromCodigo(json.getSistema().getCodigo()) : null);
		importacao.setTipoImportacao(TipoImportacaoEnum.fromCodigo(json.getTipoImportacao().getCodigo()));
		importacao.setArquivo(this.importacaoService.findOneFile(json.getArquivo().getId()));
		importacao.setLayout(this.layoutConverter.convertFrom(json.getLayout()));
		importacao.setAgente(json.getAgente() != null ? this.agenteConverter.convertFrom(json.getAgente()) : null);
		importacao.setRemetente(json.getRemetente() != null ? this.remetenteConverter.convertFrom(json.getRemetente()) : null);
		if (json.getEquivalencias() != null
				&& !json.getEquivalencias().isEmpty()) {
			equivalencias = this.equivalenciaConverter.convertListJsonFrom(json.getEquivalencias());
		}
		importacao.setEquivalencias(equivalencias);
		return importacao;
	}

}
