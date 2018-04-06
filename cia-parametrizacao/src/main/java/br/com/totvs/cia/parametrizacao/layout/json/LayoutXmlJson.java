package br.com.totvs.cia.parametrizacao.layout.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayoutXmlJson extends LayoutJson {

	private static final long serialVersionUID = 1L;

	@JsonProperty("tagRaiz")
	private String tagRaiz;

	public LayoutXmlJson(LayoutXml model) {
		super(model.getId(), model.getCodigo(), model.getDescricao(),
				TipoLayoutEnumJson.fromCodigo(model.getTipoLayout().getCodigo()),
				StatusLayoutEnumJson.fromCodigo(model.getStatus().getCodigo()), null);
		this.tagRaiz = model.getTagRaiz();
	}

}
