@TypeDefs({
		@TypeDef(parameters = { @Parameter(name = "enumClass", value = "br.com.totvs.cia.conciliacao.model.StatusConciliacaoEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusConciliacaoEnum.class),
		@TypeDef(parameters = { @Parameter(name = "enumClass", value = "br.com.totvs.cia.conciliacao.model.StatusLoteConciliacaoEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusLoteConciliacaoEnum.class),
		@TypeDef(parameters = { @Parameter(name = "enumClass", value = "br.com.totvs.cia.conciliacao.model.StatusUnidadeConciliacaoEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusUnidadeConciliacaoEnum.class),
		@TypeDef(parameters = { @Parameter(name = "enumClass", value = "br.com.totvs.cia.conciliacao.model.StatusCampoConciliacaoEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusCampoConciliacaoEnum.class)
})
package br.com.totvs.cia.conciliacao;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import br.com.totvs.cia.conciliacao.model.StatusCampoConciliacaoEnum;
import br.com.totvs.cia.conciliacao.model.StatusConciliacaoEnum;
import br.com.totvs.cia.conciliacao.model.StatusLoteConciliacaoEnum;
import br.com.totvs.cia.conciliacao.model.StatusUnidadeConciliacaoEnum;
import br.com.totvs.cia.infra.enums.PersistentEnumType;


