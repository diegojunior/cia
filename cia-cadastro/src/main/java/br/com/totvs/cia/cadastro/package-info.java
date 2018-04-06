@TypeDefs({
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.cadastro.base.model.SistemaEnum") }, typeClass = PersistentEnumType.class, defaultForType = SistemaEnum.class),
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.cadastro.configuracaoservico.model.TecnologiaServicoEnum") }, typeClass = PersistentEnumType.class, defaultForType = TecnologiaServicoEnum.class),
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.cadastro.configuracaoservico.model.TipoServicoEnum") }, typeClass = PersistentEnumType.class, defaultForType = TipoServicoEnum.class),
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnum") }, typeClass = PersistentEnumType.class, defaultForType = TipoValorDominioEnum.class),
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum") }, typeClass = PersistentEnumType.class, defaultForType = ServicoEnum.class)})
		
package br.com.totvs.cia.cadastro;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.TecnologiaServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.TipoServicoEnum;
import br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnum;
import br.com.totvs.cia.infra.enums.PersistentEnumType;
