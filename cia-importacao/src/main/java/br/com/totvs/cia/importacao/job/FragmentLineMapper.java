package br.com.totvs.cia.importacao.job;

import org.springframework.batch.item.file.LineMapper;

import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;

public class FragmentLineMapper implements LineMapper<UnidadeImportacaoProcessamentoJson> {

	private final FragmentFieldSetMapper fieldSetMapper;

	private final FragmentTokenizer tokenizer;

	public FragmentLineMapper(final FragmentFieldSetMapper fieldSetMapper, final FragmentTokenizer tokenizer) {
		this.fieldSetMapper = fieldSetMapper;
		this.tokenizer = tokenizer;
	}

	@Override
	public UnidadeImportacaoProcessamentoJson mapLine(final String fragment, final int fragmentNumber) throws Exception {
		return this.fieldSetMapper.mapFieldSet(this.tokenizer.doTokenize(fragment, fragmentNumber));
	}

}
