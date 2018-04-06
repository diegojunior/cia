@TypeDefs({
		@TypeDef(parameters = { @Parameter(name = "enumClass", value = "br.com.totvs.cia.notificacao.model.StatusEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusEnum.class)
})
package br.com.totvs.cia;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import br.com.totvs.cia.infra.enums.PersistentEnumType;
import br.com.totvs.cia.notificacao.model.StatusEnum;


