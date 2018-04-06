package br.com.totvs.cia.parametrizacao.transformacao.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "ID")
@Table(name = "PA_ITEM_TRANS_FIXO")
@EqualsAndHashCode(callSuper = false)
public class ItemTransformacaoFixo extends AbstractItemTransformacao {

	private static final long serialVersionUID = 1L;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "itemTransformacaoFixo")
	private List<ItemTransformacaoFixoDePara> itensDePara;
	
	public ItemTransformacaoFixo(final String id, final Transformacao transformacao, final List<ItemTransformacaoFixoDePara> itensDePara) {
		super(id, transformacao);
		this.itensDePara = itensDePara;
	}
	
	public ItemTransformacaoFixo(final List<ItemTransformacaoFixoDePara> itensDePara) {
		this.itensDePara = itensDePara;
		for (final ItemTransformacaoFixoDePara item : itensDePara) {
			item.setItemTransformacaoFixo(this);
		}
	}
	
	public ItemTransformacaoFixo(final ItemTransformacaoFixo itemTransformacaoFixoModel, final List<ItemTransformacaoFixoDePara> itensDePara) {
		super(itemTransformacaoFixoModel.getId(), itemTransformacaoFixoModel.getTransformacao());
		this.itensDePara = Lists.newArrayList();
		for (final ItemTransformacaoFixoDePara itemDePara : itensDePara) {
			this.itensDePara.add(new ItemTransformacaoFixoDePara(itemDePara, itemTransformacaoFixoModel));
		}
	}
	
	@Override
	public ItemTransformacao of(final Transformacao transformacao) {
		this.setTransformacao(transformacao);
		return this;
	}
	
	@Override
	public Boolean isTipoTransformacaoFixo() {
		return true;
	}
	
	@Override
	public Boolean isTipoTransformacaoEquivalencia() {
		return false;
	}
	
	public String isPossuiDuplicidade () {
		final Multiset<ItemTransformacaoFixoDePara> multiset = HashMultiset.create(this.itensDePara);
		
		for(final Multiset.Entry<ItemTransformacaoFixoDePara> entry : multiset.entrySet()) {
			if(entry.getCount() > 1) {
				return entry.getElement().getDe();
			}
		}
		
		return null;	
		
	}

	public void atualizarItens(final List<ItemTransformacaoFixoDePara> itensDePara) {
		this.getItensDePara().clear();
		for (final ItemTransformacaoFixoDePara item : itensDePara) {
			item.setItemTransformacaoFixo(this);
			this.getItensDePara().add(item);
		}
		
	}
	
}