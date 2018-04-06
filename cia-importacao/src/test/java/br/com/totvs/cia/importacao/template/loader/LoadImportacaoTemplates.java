package br.com.totvs.cia.importacao.template.loader;

import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.importacao.template.ArquivoTemplate;
import br.com.totvs.cia.importacao.template.CampoImportacaoProcessamentoJsonTemplate;
import br.com.totvs.cia.importacao.template.EquivalenciaTemplate;
import br.com.totvs.cia.importacao.template.ImportacaoTemplate;
import br.com.totvs.cia.importacao.template.LoteUnidadeImportacaoTemplate;
import br.com.totvs.cia.importacao.template.RemetenteTemplate;
import br.com.totvs.cia.importacao.template.UnidadeImportacaoProcessamentoJsonTemplate;
import br.com.totvs.cia.importacao.template.UnidadeLayoutImportacaoTemplate;
import br.com.totvs.cia.importacao.template.UnidadeLoteImportacaoTemplate;
import br.com.totvs.cia.parametrizacao.template.loader.LoadParametrizacaoTemplates;

public class LoadImportacaoTemplates implements TemplateLoader {

	@Override
	public void load() {
		new LoadParametrizacaoTemplates().load();
		new ArquivoTemplate().load();
		new ImportacaoTemplate().load();
		new UnidadeLayoutImportacaoTemplate().load();
		new CampoImportacaoProcessamentoJsonTemplate().load();
		new UnidadeImportacaoProcessamentoJsonTemplate().load();
		new EquivalenciaTemplate().load();
		new UnidadeLoteImportacaoTemplate().load();
		new LoteUnidadeImportacaoTemplate().load();
		new RemetenteTemplate().load();
		
	}

}
