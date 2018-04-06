package br.com.totvs.cia.parametrizacao.transformacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.transformacao.json.ItemTransformacaoFixoDeParaJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PA_ITEM_TRANS_FIXO_DE_PARA")
@EqualsAndHashCode(callSuper = false, of = {"id", "de"})
public class ItemTransformacaoFixoDePara implements Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-depara-uuid")
	@GenericGenerator(name = "system-depara-uuid", strategy = "uuid")
	private String id;
	
	@ManyToOne
	@JoinColumn(name="ID_ITEM_TRANS_FIXO")
	private ItemTransformacaoFixo itemTransformacaoFixo;
	
	@Column(name = "DE")
	private String de;
	
	@Column(name = "PARA")
	private String para;
	
	public ItemTransformacaoFixoDePara(final ItemTransformacaoFixoDeParaJson json, final ItemTransformacaoFixo itemTransformacaoFixo) {
		this.id = json.getId();
		this.de = json.getDe();
		this.para = json.getPara();
		this.itemTransformacaoFixo = itemTransformacaoFixo;
	}

	public ItemTransformacaoFixoDePara(ItemTransformacaoFixoDeParaJson json) {
		this.id = json.getId();
		this.de = json.getDe();
		this.para = json.getPara();
	}

	public ItemTransformacaoFixoDePara(final ItemTransformacaoFixoDePara itemDePara,
			final ItemTransformacaoFixo itemTransformacaoFixo) {
		this.id = itemDePara.getId();
		this.de = itemDePara.getDe();
		this.para = itemDePara.getPara();
		this.itemTransformacaoFixo = itemTransformacaoFixo;
	}
}