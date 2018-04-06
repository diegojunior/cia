package br.com.totvs.cia.importacao.job;

import java.util.Map;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;

public class CustomPatternMatchingCompositeLineMapper implements LineMapper<UnidadeImportacaoProcessamentoJson>, InitializingBean {

	private final PatternMatchingCompositeLineMapper<UnidadeImportacaoProcessamentoJson> delegate;
	
	public CustomPatternMatchingCompositeLineMapper(final PatternMatchingCompositeLineMapper<UnidadeImportacaoProcessamentoJson> lineMapper) {
		this.delegate = lineMapper;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.delegate.afterPropertiesSet();		
	}

	@Override
	public UnidadeImportacaoProcessamentoJson mapLine(final String line, final int lineNumber) throws Exception {
		UnidadeImportacaoProcessamentoJson mapLine = this.delegate.mapLine(line, lineNumber);
		mapLine.setNumeroLinha(lineNumber);
		mapLine.setNumeroLinhaAnterior(lineNumber - 1);
		return mapLine;
	}
	
	public void setTokenizers(final Map<String, LineTokenizer> tokenizers) {
		this.delegate.setTokenizers(tokenizers);
	}

	public void setFieldSetMappers(final Map<String, FieldSetMapper<UnidadeImportacaoProcessamentoJson>> fieldSetMappers) {
		Assert.isTrue(!fieldSetMappers.isEmpty(), "The 'fieldSetMappers' property must be non-empty");
		this.delegate.setFieldSetMappers(fieldSetMappers);
	}

}
