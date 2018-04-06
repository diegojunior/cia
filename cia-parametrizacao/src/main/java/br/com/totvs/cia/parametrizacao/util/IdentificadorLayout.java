package br.com.totvs.cia.parametrizacao.util;

import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import lombok.Setter;

public abstract class IdentificadorLayout {

	@Setter
	protected IdentificadorLayout sucessor;

	protected TipoLayoutEnumJson tipoLayout;

	protected abstract Layout geraLayout(String id);

	protected abstract LayoutJson geraLayoutJson();

	public Layout processaRequest(final LayoutJson layoutJson) {
		if (layoutJson.getTipoLayout().equals(this.tipoLayout)) {
			return this.geraLayout(layoutJson.getId());
		} else if (this.sucessor != null) {
			return this.sucessor.processaRequest(layoutJson);
		}

		throw new RuntimeException("Falta Implementar identificador especifico do novo layout");
	}

	public LayoutJson processaRequestJson(final Layout layout) {
		if (layout.getTipoLayout().getCodigo().equals(this.tipoLayout.getCodigo())) {
			return this.geraLayoutJson();
		} else if (this.sucessor != null) {
			return this.sucessor.processaRequestJson(layout);
		}

		throw new RuntimeException("Falta Implementar identificador especifico do novo layout");
	}

}