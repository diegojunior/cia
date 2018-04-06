package br.com.totvs.cia.carga.json;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.carteira.json.ClienteJson;
import br.com.totvs.cia.cadastro.configuracaoservico.json.ConfiguracaoServicoJson;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.infra.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoteCargaJson implements Json, Comparable<LoteCargaJson> {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	@JsonProperty("servico")
	private ConfiguracaoServicoJson servico;

	@JsonProperty("lotesClientes")
	private List<LoteClienteJson> lotesClientes;
	
	@JsonProperty("status")
	@JsonSerialize(using = StatusLoteCargaJsonEnumSerializer.class)
	@JsonDeserialize(using = StatusLoteCargaJsonEnumDeserializer.class)
	private StatusLoteCargaJsonEnum status;
	
	public LoteCargaJson(final LoteCarga model, final ConfiguracaoServicoJson servicoJson,
			final List<LoteClienteJson> lotesClientesJson) {
		this.id = model.getId();
		this.servico = servicoJson;
		this.lotesClientes = lotesClientesJson;
		this.status = StatusLoteCargaJsonEnum.fromCodigo(model.getStatus().getCodigo());
	}
	
	public LoteCargaJson(final LoteCarga model) {
		this.id = model.getId();
		this.servico = new ConfiguracaoServicoJson(model.getServico());
		this.status = StatusLoteCargaJsonEnum.fromCodigo(model.getStatus().getCodigo());
		this.lotesClientes = Lists.newArrayList();
		if (model.getLotesClientes() != null && !model.getLotesClientes().isEmpty()) {
			this.lotesClientes = model.getLotesClientes()
									 .stream()
									 .map(lote -> {
								  		 return new LoteClienteJson(lote.getId(),
												new ClienteJson(lote.getCliente()), 
												DateUtil.format(lote.getDataProcessamento(), DateUtil.ddMMyyyyHHmmss), 
												StatusLoteClienteJsonEnum.fromCodigo(lote.getStatus().getCodigo()));})
									 .collect(Collectors.toList());
		}
	}

	@Override
	public int compareTo(final LoteCargaJson outro) {
		return this.getServico().getCodigo().compareTo(outro.getServico().getCodigo());
	}
}