package br.com.totvs.cia.parametrizacao.template.loader;

import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.template.CampoParametrizacaoUnidadeImportacaoTemplate;
import br.com.totvs.cia.parametrizacao.template.CampoTemplate;
import br.com.totvs.cia.parametrizacao.template.ChaveParametrizacaoImportacaoTemplate;
import br.com.totvs.cia.parametrizacao.template.DominioTemplate;
import br.com.totvs.cia.parametrizacao.template.ItemTransformacaoEquivalenciaTemplate;
import br.com.totvs.cia.parametrizacao.template.ItemTransformacaoFixaTemplate;
import br.com.totvs.cia.parametrizacao.template.LayoutDelimitadorTemplate;
import br.com.totvs.cia.parametrizacao.template.LayoutPosicionalTemplate;
import br.com.totvs.cia.parametrizacao.template.LayoutXmlTemplate;
import br.com.totvs.cia.parametrizacao.template.LinhaBlocoParametrizacaoUnidadeImportacaoTemplate;
import br.com.totvs.cia.parametrizacao.template.ParametrizacaoUnidadeImportacaoBlocoTemplate;
import br.com.totvs.cia.parametrizacao.template.ParametrizacaoUnidadeImportacaoChaveTemplate;
import br.com.totvs.cia.parametrizacao.template.SessaoTemplate;
import br.com.totvs.cia.parametrizacao.template.TransformacaoTemplate;
import br.com.totvs.cia.parametrizacao.template.ValidacaoInternaTemplate;

public class LoadParametrizacaoTemplates implements TemplateLoader {

	@Override
	public void load() {
		new DominioTemplate().load();
		new CampoTemplate().load();
		new SessaoTemplate().load();
		new LayoutPosicionalTemplate().load();
		new LayoutDelimitadorTemplate().load();
		new LayoutXmlTemplate().load();
		new CampoParametrizacaoUnidadeImportacaoTemplate().load();
		new ChaveParametrizacaoImportacaoTemplate().load();
		new LinhaBlocoParametrizacaoUnidadeImportacaoTemplate().load();
		new ParametrizacaoUnidadeImportacaoBlocoTemplate().load();
		new ParametrizacaoUnidadeImportacaoChaveTemplate().load();
		new ValidacaoInternaTemplate().load();
		new ItemTransformacaoFixaTemplate().load();
		new TransformacaoTemplate().load();
		new ItemTransformacaoEquivalenciaTemplate().load();
	}

}
