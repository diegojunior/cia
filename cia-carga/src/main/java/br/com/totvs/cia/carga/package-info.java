@TypeDefs({
		@TypeDef(parameters = { @Parameter(name = "enumClass", value = "br.com.totvs.cia.carga.model.StatusCargaEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusCargaEnum.class),
		@TypeDef(parameters = { @Parameter(name = "enumClass", value = "br.com.totvs.cia.carga.model.StatusLoteCargaEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusLoteCargaEnum.class),
		@TypeDef(parameters = { @Parameter(name = "enumClass", value = "br.com.totvs.cia.carga.model.StatusLoteClienteEnum") }, typeClass = PersistentEnumType.class, defaultForType = StatusLoteClienteEnum.class)
		
})
package br.com.totvs.cia.carga;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import br.com.totvs.cia.carga.model.StatusCargaEnum;
import br.com.totvs.cia.carga.model.StatusLoteCargaEnum;
import br.com.totvs.cia.carga.model.StatusLoteClienteEnum;
import br.com.totvs.cia.infra.enums.PersistentEnumType;


