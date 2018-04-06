package br.com.totvs.cia.cadastro.equivalencia.json;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import br.com.totvs.cia.infra.json.Json;
import lombok.NoArgsConstructor;
@JsonRootName("equivalencia")
@NoArgsConstructor
public class EquivalenciaTableJson implements Json {

	private static final long serialVersionUID = 1L;
	
	private Map<String, List<String>> mapCodigoExternoPorCodigoInterno;
	
	private Map<String, String> mapCodigoInternoPorCodigoExterno;
	
	@JsonProperty("codigoInterno")
	private String codigo;
	
	public EquivalenciaTableJson(final List<EquivalenciaJson> equivalencias, final String codigo) {
		this.codigo = codigo;
		this.mapCodigoExternoPorCodigoInterno = Maps.newHashMap();
		this.mapCodigoInternoPorCodigoExterno = Maps.newHashMap();
		
		List<String> codigosExternos = null;
		
		for (final EquivalenciaJson equivalencia : equivalencias) {
			if (codigosExternos == null) { 
				codigosExternos = Lists.newArrayList();
				this.mapCodigoExternoPorCodigoInterno.put(equivalencia.getCodigoInterno(), codigosExternos);
			}
			
			codigosExternos.add(equivalencia.getCodigoExterno());
			
			this.mapCodigoInternoPorCodigoExterno.put(equivalencia.getCodigoExterno(), equivalencia.getCodigoInterno());
		}
	}
	
	@JsonGetter("codigosExternos")
	public List<String> getCodigoExterno() {
		return this.mapCodigoExternoPorCodigoInterno.get(this.codigo);
	}

}
