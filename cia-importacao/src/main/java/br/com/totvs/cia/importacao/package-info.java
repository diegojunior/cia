@TypeDefs({
		@TypeDef(parameters = { @Parameter(name = "enumClass", value = "br.com.totvs.cia.importacao.model.TipoImportacaoEnum") }, typeClass = PersistentEnumType.class, defaultForType = TipoImportacaoEnum.class),
		@TypeDef(parameters = { @Parameter(name = "enumClass", value = "br.com.totvs.cia.importacao.model.StatusImportacaoEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusImportacaoEnum.class)
})
package br.com.totvs.cia.importacao;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import br.com.totvs.cia.importacao.model.StatusImportacaoEnum;
import br.com.totvs.cia.importacao.model.TipoImportacaoEnum;
import br.com.totvs.cia.infra.enums.PersistentEnumType;