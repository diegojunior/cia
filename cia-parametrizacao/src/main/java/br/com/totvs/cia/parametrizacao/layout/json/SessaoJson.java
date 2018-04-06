package br.com.totvs.cia.parametrizacao.layout.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.dominio.json.DominioJson;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = false, of = { "id", "codigo" })
@EqualsAndHashCode(callSuper = false, of = { "id", "codigo" })
public class SessaoJson implements Json {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("nome")
	private String nome;

	@JsonProperty("codigo")
	private String codigo;

	@JsonProperty("tag")
	private String tag;

	@JsonProperty("tamanho")
	private int tamanho;
	
	@JsonProperty("semSessaoConfigurada")
	private boolean semSessaoConfigurada;

	@JsonProperty("campos")
	private List<CampoJson> campos;

	public SessaoJson(final Sessao model) {
		this.id = model.getId();
		this.nome = model.getNome();
		this.codigo = model.getCodigo();
		this.tamanho = model.getTamanho();
		this.semSessaoConfigurada = model.isSemSessaoConfigurada();
	}

	public List<CampoJson> getCampos() {
		if (this.campos == null) {
			this.campos = Lists.newArrayList();
		}
		return this.campos;
	}

	public void addCampo(final CampoJson campo) {
		if (this.campos == null) {
			this.campos = new ArrayList<CampoJson>();
		}
		this.campos.add(campo);
	}

	public List<DominioJson> getDominios() {
		return Lists.newArrayList(Lists.transform(this.getCampos(), new Function<CampoJson, DominioJson>() {
			@Override
			public DominioJson apply(final CampoJson campo) {
				return campo.getDominio();
			}
		}));
	}

}
