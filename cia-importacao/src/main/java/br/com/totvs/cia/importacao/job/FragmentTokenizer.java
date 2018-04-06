package br.com.totvs.cia.importacao.job;

import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.exception.JobException;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;

public class FragmentTokenizer implements LineTokenizer {

	private final NodeList nodes;

	private final Sessao sessao;

	private int nodeIndex = 0;

	public FragmentTokenizer(final NodeList nodes, final Sessao sessao) {
		this.nodes = nodes;
		this.sessao = sessao;
	}
	
	public FieldSet doTokenize(final String fragment, final int index) {
		this.nodeIndex = index;
		return this.tokenize(fragment);
	}

	@Override
	public FieldSet tokenize(final String fragment) {

		final List<String> tags = Lists.transform(this.sessao.getCampos(), new Function<Campo, String>() {
			@Override
			public String apply(final Campo input) {
				return FragmentTokenizer.this.sessao.getCodigo() + input.getTag();
			}
		});

		final List<String> tokens = Lists.newArrayList();

		final Node node = this.nodes.item(this.nodeIndex);

		for (final String tag : tags) {

			final XPathFactory xpathFactory = XPathFactory.newInstance();
			final XPath xpath = xpathFactory.newXPath();

			try {

				final XPathExpression expression = xpath.compile(tag);
				final NodeList nodes = (NodeList) expression.evaluate(node, XPathConstants.NODESET);
				if (nodes.getLength() == 0) {
					throw new XPathExpressionException(tag);
				}
				tokens.add(this.extractValue(Lists.newArrayList(nodes.item(this.nodeIndex)), tag));

			} catch (final XPathExpressionException e) {
				throw new JobException("Erro ao filtrar as tags xml do arquivo. " + e.getMessage(), e);
			}

		}

		final DefaultFieldSet fieldSet = new DefaultFieldSet(tokens.toArray(new String[tokens.size()]));

		return fieldSet;
	}

	private String extractValue(final List<Node> nodes, final String fragment) {

		for (int index = 0; index < nodes.size(); index++) {
			final Node node = nodes.get(index);
			final Node parentNode = node.getParentNode();
			final String extrairUltimoElemento = this.extrairUltimoElemento(fragment);
			if (extrairUltimoElemento.equals(node.getNodeName())) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (node.hasChildNodes()) {
						return this.extractValue(this.convertToList(node.getChildNodes()), fragment);
					}
				}
				if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
					if (node.hasChildNodes()) {
						return this.extractValue(this.convertToList(node.getChildNodes()), fragment);
					}
				}
			} else if (extrairUltimoElemento.equals(parentNode.getNodeName())) {
				if (node.getNodeType() == Node.TEXT_NODE) {
					final String value = node.getNodeValue().trim();
					if (!value.isEmpty()) {
						return value;
					}
				}
			} else if (node.hasChildNodes()) {
				return this.extractValue(this.convertToList(node.getChildNodes()), fragment);
			}
		}
		return null;
	}

	private List<Node> convertToList(final NodeList nodesToConvert) {
		final List<Node> nodes = Lists.newArrayList();
		for (int index = 0; index < nodesToConvert.getLength(); index++) {
			nodes.add(nodesToConvert.item(index));
		}
				
		return nodes;
	}

	private String extrairUltimoElemento(final String fragment) {

		final List<String> elementos = Arrays.asList(fragment.split("/"));

		return elementos.get(elementos.size() - 1).replaceAll("@", "");
	}

}
