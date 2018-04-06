package br.com.totvs.cia.importacao.mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ArquivoXmlMock {

	public static final InputStream getArquivoMock() {
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