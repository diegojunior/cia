package br.com.totvs.cia.parametrizacao.layout.json;

import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LayoutPosicionalJson extends LayoutJson {

	private static final long serialVersionUID = 1L;

	public LayoutPosicionalJson(LayoutPosicional model) {
		super(model.getId(), model.getCodigo(), model.getDescricao(),
				TipoLayoutEnumJson.fromCodigo(model.getTipoLayout().getCodigo()),
				StatusLayoutEnumJson.fromCodigo(model.getStatus().getCodigo()), null);
	}
	
}
