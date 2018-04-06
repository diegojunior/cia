package br.com.totvs.cia.importacao.job;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;

public class DefaultObjectFieldSetMapper implements FieldSetMapper<UnidadeImportacaoProcessamentoJson> {

	@Override
	public UnidadeImportacaoProcessamentoJson mapFieldSet(final FieldSet fs) throws BindException {
		return new UnidadeImportacaoProcessamentoJson();
	}

}