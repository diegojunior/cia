package br.com.totvs.cia.parametrizacao.layout.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LayoutDelimitadorJson extends LayoutJson {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("tipoDelimitador")
	@JsonDeserialize(using = TipoDelimitadorEnumJsonDeserializer.class)
	@JsonSerialize(using = TipoDelimitadorEnumJsonSerializer.class)
	private TipoDelimitadorEnumJson tipoDelimitador;

	public LayoutDelimitadorJson(final LayoutDelimitador model) {
		super(model.getId(), model.getCodigo(), model.getDescricao(),
				TipoLayoutEnumJson.fromCodigo(model.getTipoLayout().getCodigo()),
				StatusLayoutEnumJson.fromCodigo(model.getStatus().getCodigo()), null);
	}
	
}
