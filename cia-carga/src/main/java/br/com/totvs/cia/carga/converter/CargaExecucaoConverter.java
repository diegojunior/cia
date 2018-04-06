package br.com.totvs.cia.carga.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.converters.ConversionException;

import br.com.totvs.cia.cadastro.carteira.json.ClienteJson;
import br.com.totvs.cia.cadastro.configuracaoservico.converter.ConfiguracaoServicoResumidoConverter;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.carga.json.CargaExecucaoJson;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;

@Component
public class CargaExecucaoConverter extends JsonConverter<Carga, CargaExecucaoJson>{
	
	@Autowired
	private ConfiguracaoServicoResumidoConverter configuracaoServicoConverter;
	
	@Override
	public Carga convertFrom(final CargaExecucaoJson json) {
		try{
			List<ConfiguracaoServico> servicos = this.configuracaoServicoConverter.convertListJsonFrom(json.getServicos());
			
			if (json.getTipoExecucao().isCliente()) {
				List<String> clientes = Lists.newArrayList();
				for (ClienteJson clienteJson : json.getClientes()) {
					clientes.add(clienteJson.getCodigo());
				}
				return new Carga(json, servicos, clientes);
			}
			
			return new Carga(json, servicos);
		}catch (Exception ex){
			throw new ConversionException(ex);
		}
	}
	
	@Override
	public CargaExecucaoJson convertFrom(final Carga model) {
		throw new ConverterException("Conversão de Model para Json não prevista");
	}
}