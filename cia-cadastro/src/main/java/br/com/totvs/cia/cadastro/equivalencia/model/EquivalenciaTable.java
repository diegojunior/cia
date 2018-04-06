package br.com.totvs.cia.cadastro.equivalencia.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EquivalenciaTable {

	private final Map<String, List<String>> mapCodigoExternoPorInterno;
	
	private final Map<String, String> mapCodigoInternoPorExterno;
	
	public EquivalenciaTable(final List<Equivalencia> equivalencias) {
		this.mapCodigoExternoPorInterno = Maps.newHashMap();
		this.mapCodigoInternoPorExterno = Maps.newHashMap();
		
		List<String> codigoExternoList = null;
		
		for (final Equivalencia equivalencia : equivalencias) {
			codigoExternoList = this.mapCodigoExternoPorInterno.get(equivalencia.getCodigoInterno());
			
			if (codigoExternoList == null) {
				codigoExternoList = Lists.newArrayList();
				this.mapCodigoExternoPorInterno.put(equivalencia.getCodigoInterno(), codigoExternoList);
			}
			
			codigoExternoList.add(equivalencia.getCodigoExterno());
			
			//monta codigos internos
			this.mapCodigoInternoPorExterno.put(equivalencia.getCodigoExterno(), equivalencia.getCodigoInterno());
			
		}
	}

	public List<String> getCodigoExternoPor(final String codigoInterno) {
		return this.mapCodigoExternoPorInterno.get(codigoInterno);
	}
	
	public String getCodigoInternoPor(final String codigoExterno) {
		return this.mapCodigoInternoPorExterno.get(codigoExterno);
	}
	
	public boolean contemEquivalencia(final String codigo) {
		final String codigoInterno = this.getCodigoInternoPor(codigo);
		final List<String> codigosExternos = this.getCodigoExternoPor(codigo);
		if (codigoInterno == null && codigosExternos == null) {
			return false;
		}
		return true;
	}
}
