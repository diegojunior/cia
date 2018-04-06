@TypeDefs({
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum") }, typeClass = PersistentEnumType.class, defaultForType = TipoLayoutEnum.class),
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.parametrizacao.layout.model.DelimitadorEnum") }, typeClass = PersistentEnumType.class, defaultForType = DelimitadorEnum.class),
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusLayoutEnum.class),
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.parametrizacao.perfilconciliacao.model.StatusPerfilEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusPerfilEnum.class),
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.parametrizacao.transformacao.model.TipoTransformacaoEnum") }, typeClass = PersistentEnumType.class, defaultForType = TipoTransformacaoEnum.class),
		
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.parametrizacao.validacao.model.TipoValidacaoEnum") }, typeClass = PersistentEnumType.class, defaultForType = TipoValidacaoEnum.class),
		
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.parametrizacao.validacao.model.LocalValidacaoArquivoEnum") }, typeClass = PersistentEnumType.class, defaultForType = LocalValidacaoArquivoEnum.class),
		
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.parametrizacao.validacao.model.CampoValidacaoArquivoEnum") }, typeClass = PersistentEnumType.class, defaultForType = CampoValidacaoArquivoEnum.class),
		
		@TypeDef(parameters = {
				@Parameter(name = "enumClass", value = "br.com.totvs.cia.parametrizacao.layout.model.TipoDelimitadorEnum") }, typeClass = PersistentEnumType.class, defaultForType = TipoDelimitadorEnum.class)})
		
package br.com.totvs.cia.parametrizacao;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import br.com.totvs.cia.infra.enums.PersistentEnumType;
import br.com.totvs.cia.parametrizacao.layout.model.DelimitadorEnum;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoDelimitadorEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.StatusPerfilEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.TipoTransformacaoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.CampoValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.LocalValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.TipoValidacaoEnum;
