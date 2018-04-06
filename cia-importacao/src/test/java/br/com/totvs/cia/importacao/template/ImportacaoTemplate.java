package br.com.totvs.cia.importacao.template;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.model.Equivalencia;
import br.com.totvs.cia.cadastro.equivalencia.model.Remetente;
import br.com.totvs.cia.importacao.model.Arquivo;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;

public class ImportacaoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(Importacao.class)
			.addTemplate("importacaoCinf", new Rule(){{
				this.add("id", "124");
				this.add("dataImportacao", this.instant("now"));
				this.add("sistema", SistemaEnum.AMPLIS);
				this.add("layout", this.one(LayoutPosicional.class, "cinfPosicional"));
				this.add("arquivo", this.one(Arquivo.class, "arquivoCinfPosicional"));
			}});
		
		Fixture
			.of(Importacao.class)
			.addTemplate("importacaoDelimitadorPontoVirgula", new Rule(){{
				this.add("id", "123");
				this.add("dataImportacao", this.instant("now"));
				this.add("sistema", SistemaEnum.AMPLIS);
				this.add("layout", this.one(LayoutDelimitador.class, "cinfDelimitador"));
				this.add("arquivo", this.one(Arquivo.class, "arquivoDelimitadorPontoVirgula"));
			}});
		
		Fixture
			.of(Importacao.class)
			.addTemplate("importacaoXml", new Rule(){{
				this.add("id", "125");
				this.add("dataImportacao", this.instant("now"));
				this.add("sistema", SistemaEnum.AMPLIS);
				this.add("layout", this.one(LayoutXml.class, "cinfXml"));
				this.add("arquivo", this.one(Arquivo.class, "arquivoXml"));
			}});
		
		Fixture
			.of(Importacao.class)
			.addTemplate("importacaoCinfTamanhoArquivoInvalido", new Rule(){{
				this.add("id", "126");
				this.add("dataImportacao", this.instant("now"));
				this.add("sistema", SistemaEnum.AMPLIS);
				this.add("layout", this.one(LayoutPosicional.class, "cinfPosicional"));
				this.add("arquivo", this.one(Arquivo.class, "arquivoCinfPosicionalTamanhoInvalido"));
			}});
		
		Fixture
			.of(Importacao.class)
			.addTemplate("importacaoXmlComParametrizacao", new Rule(){{
				this.add("id", "127");
				this.add("dataImportacao", this.instant("now"));
				this.add("sistema", SistemaEnum.AMPLIS);
				this.add("layout", this.one(LayoutXml.class, "layoutXmlComParametrizacao"));
				this.add("arquivo", this.one(Arquivo.class, "arquivoXml"));
			}});
		
		Fixture
			.of(Importacao.class)
			.addTemplate("importacaoCinfComValidacaoCorretora", new Rule(){{
				this.add("id", "128");
				this.add("dataImportacao", geraData("2017-12-27", new SimpleDateFormat("yyyy-MM-dd")));
				this.add("sistema", SistemaEnum.AMPLIS);
				this.add("layout", this.one(LayoutPosicional.class, "cinfPosicional"));
				this.add("arquivo", this.one(Arquivo.class, "arquivoCinfPosicional"));
				this.add("equivalencias", has(1).of(Equivalencia.class, "corretora"));
			}});
		
		Fixture
			.of(Importacao.class)
			.addTemplate("importacaoCinfComTransformacaoEquivalencia", new Rule(){{
				this.add("id", "129");
				this.add("dataImportacao", this.instant("now"));
				this.add("sistema", SistemaEnum.AMPLIS);
				this.add("layout", this.one(LayoutPosicional.class, "posicionalValidarCorretora"));
				this.add("arquivo", this.one(Arquivo.class, "arquivoCinfPosicional"));
				this.add("equivalencias", has(1).of(Equivalencia.class, "corretora"));
				this.add("remetente", one(Remetente.class, "abn"));
			}});
		
	}
	
	private Date geraData(String data, SimpleDateFormat pattern) {
		Date dataGerada = null;
		try {
			dataGerada =  pattern.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataGerada;
	}

}
