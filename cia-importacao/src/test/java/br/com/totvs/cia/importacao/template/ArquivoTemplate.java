package br.com.totvs.cia.importacao.template;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.importacao.mock.ArquivoXmlMock;
import br.com.totvs.cia.importacao.model.Arquivo;
import br.com.totvs.cia.importacao.model.DadosArquivo;

public class ArquivoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(Arquivo.class)
			.addTemplate("arquivoCinfPosicional", new Rule() {{
				this.add("id", "123");
				this.add("fileName", "cinf-posicional.txt");
				this.add("dadosArquivo", ArquivoTemplate.this.getDadosArquivoPosicionalCinf());
			}});
		
		Fixture
			.of(Arquivo.class)
			.addTemplate("arquivoDelimitadorPontoVirgula", new Rule() {{
				this.add("id", "456");
				this.add("fileName", "teste-delimitador-ponto-virgula.txt");
				this.add("dadosArquivo", ArquivoTemplate.this.getArquivoDelimitadorMock());
			}});
		
		Fixture
			.of(Arquivo.class)
			.addTemplate("arquivoXml", new Rule() {{
				this.add("id", "44444");
				this.add("fileName", "teste.xml");
				this.add("dadosArquivo", ArquivoTemplate.this.getArquivoXml());
			}});
		
		Fixture
			.of(Arquivo.class)
			.addTemplate("arquivoCinfPosicionalTamanhoInvalido", new Rule() {{
				this.add("id", "555");
				this.add("fileName", "cinf-posicional.txt");
				this.add("dadosArquivo", ArquivoTemplate.this.getDadosArquivoPosicionalCinfTamanhoInvalido());
			}});
	}
	
	private DadosArquivo getDadosArquivoPosicionalCinf() {
		final StringBuilder sb = new StringBuilder();
		sb.append("32BMFVST0000000020000000").append(System.getProperty("line.separator"))
		.append("32BMFTST0000000200000000").append(System.getProperty("line.separator"))
		.append("32TTTVST0000000020000000").append(System.getProperty("line.separator"))
		.append("32VVVPST0000000010000000");
		DadosArquivo dadosArquivo = new DadosArquivo();
		dadosArquivo.setId("1");
		dadosArquivo.setDados(sb.toString().getBytes());
		return dadosArquivo;
	}
	
	private DadosArquivo getDadosArquivoPosicionalCinfTamanhoInvalido() {
		final StringBuilder sb = new StringBuilder();
		sb.append("32BMFVST0000000020000000").append(System.getProperty("line.separator"))
		.append("32BMFTST000000020000");
		DadosArquivo dadosArquivo = new DadosArquivo();
		dadosArquivo.setId("123321");
		dadosArquivo.setDados(sb.toString().getBytes());
		return dadosArquivo;
	}
	
	public DadosArquivo getArquivoDelimitadorMock() {
		final StringBuilder sb = new StringBuilder();
		sb.append("0;20070207;20051101;OPER COMPROMISSADA AB;20040607;2014;EV2.0").append(System.getProperty("line.separator"))
			.append("1;AGORA").append(System.getProperty("line.separator"))
			.append("2;100000;50.00").append(System.getProperty("line.separator"))
			.append("2;200000;150.00").append(System.getProperty("line.separator"))
			.append("3;2").append(System.getProperty("line.separator"))
			.append("1;XP INVEST").append(System.getProperty("line.separator"))
			.append("2;350000;10.00").append(System.getProperty("line.separator"))
			.append("3;1").append(System.getProperty("line.separator"))
			.append("4;2");
		
		DadosArquivo dadosArquivo = new DadosArquivo();
		dadosArquivo.setId("2");
		dadosArquivo.setDados(sb.toString().getBytes());

		return dadosArquivo;
	}
	
	private DadosArquivo getArquivoXml() {
		DadosArquivo dadosArquivo = new DadosArquivo();
		dadosArquivo.setId("3");
		InputStream resource = ArquivoXmlMock.getArquivoMock();
		String newLine = System.getProperty("line.separator");
		BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
		StringBuilder result = new StringBuilder();
		String line; boolean flag = false;
		try {
			while ((line = reader.readLine()) != null) {
				result.append(flag? newLine: "").append(line);
				flag = true;
			}
			
		} catch(IOException e) {
			System.out.println(e);
		}
		
		dadosArquivo.setDados(result.toString().getBytes());
		
		return dadosArquivo;
	}
	
	public InputStream getArquivoMock() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<Document xsi:schemaLocation=\"urn:bvmf.052.01.xsd bvmf.052.01.xsd\" xmlns=\"urn:bvmf.052.01.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">")
				.append("<BizFileHdr><Xchg><BizGrp>")
				.append("<Document xsi:schemaLocation=\"urn:bvmf.041.02.xsd bvmf.041.02.xsd\" xmlns=\"urn:bvmf.041.02.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">")
				.append("<SttlmRpt>")
				.append("<RptParams><Frqcy><xpto>")
				.append("<RptNb>11111</RptNb>")
				.append("<ActvtyInd>true</ActvtyInd>")
				.append("<Financeiro>100000</Financeiro>")
				.append("<Data>20170510</Data>")
				.append("<Quantidade>100</Quantidade>")
				.append("<ValorLiquidacao valor=\"10\"></ValorLiquidacao>")
				.append("</xpto></Frqcy></RptParams>")
				.append("<LftParams>")
				.append("<RptNb>11111</RptNb>")
				.append("<Pu>100</Pu>")
				.append("</LftParams>")
				.append("</SttlmRpt></Document></BizGrp></Xchg></BizFileHdr></Document>");

		final byte[] bytes = sb.toString().getBytes();
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		return byteArrayInputStream;
	}
}
