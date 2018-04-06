package br.com.totvs.cia.importacao.job;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;

@Component
public class ImportacaoItemXmlReader implements ItemReader<UnidadeImportacaoProcessamentoJson>, ItemStream {

	@Autowired
	private ImportacaoService importacaoService;

	private Importacao importacao;

	private Document document;

	private List<FragmentLineMapper> fragmentsMapper;

	private List<Sessao> sessoes;

	private List<NodeList> nodes;

	private int limiteFragmentos;

	private int indexFragmentos;
	
	private int indexElementoSessao;
	
	private int limiteElementoSessao;
	
	public ItemReader<UnidadeImportacaoProcessamentoJson> reader(final String importacaoId) {
		this.importacao = this.importacaoService.findOne(importacaoId);
		this.sessoes = this.importacao.getLayout().getSessoes();
		this.indexFragmentos = 0;
		this.indexElementoSessao = 0;
		this.limiteElementoSessao = this.sessoes.size();
		this.fragmentsMapper = Lists.newArrayList();
		this.nodes = Lists.newArrayList();
		this.open(new ExecutionContext());
		return this;

	}

	@Override
	public UnidadeImportacaoProcessamentoJson read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		return this.doRead();

	}

	private UnidadeImportacaoProcessamentoJson doRead() throws Exception {

		if (this.indexElementoSessao >= this.limiteElementoSessao) {
			return null;
		}
		
		final NodeList nodeList = this.nodes.get(this.indexElementoSessao);
		
		this.limiteFragmentos = nodeList.getLength();
		
		if (this.indexFragmentos < this.limiteFragmentos) {
			final String fragment = this.nodeToString(this.nodes.get(this.indexElementoSessao).item(this.indexFragmentos));
			final UnidadeImportacaoProcessamentoJson obj = this.fragmentsMapper.get(this.indexElementoSessao).mapLine(fragment, this.indexFragmentos);
			this.indexFragmentos++;
			return obj;
		} else {
			this.indexFragmentos = 0;
			this.indexElementoSessao++;
			return this.doRead();
		}
		
	}

	@Override
	public void open(final ExecutionContext executionContext) throws ItemStreamException {
		final Resource resource = new ByteArrayResource(this.importacao.getArquivo().getDadosArquivo().getDados());

		if (!resource.exists()) {
			throw new IllegalStateException("O arquivo deve existir: " + resource);
		}

		if (!resource.isReadable()) {
			throw new IllegalStateException("O arquivo deve estar disponível para leitura: " + resource);
		}

		try {

			final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.document = builder.parse(resource.getInputStream());
			this.document.getDocumentElement().normalize();
			for (final Sessao sessao : this.sessoes) {
				final XPathFactory xpathFactory = XPathFactory.newInstance();
				final XPath xpath = xpathFactory.newXPath();
				final XPathExpression expression = xpath.compile(sessao.getCodigo());
				final NodeList node = (NodeList) expression.evaluate(this.document, XPathConstants.NODESET);
				final FragmentTokenizer tokenizer = new FragmentTokenizer(node, sessao);
				final FragmentLineMapper fragmentLineMapper = new FragmentLineMapper(
						new FragmentFieldSetMapper(sessao), tokenizer);
				this.nodes.add(node);
				this.fragmentsMapper.add(fragmentLineMapper);
			}
				
		} catch (final Exception e) {
			throw new ItemStreamException("Erro ao efetuar o parser do arquivo xml");
		}
	}

	@Override
	public void update(final ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws ItemStreamException {
		// TODO Auto-generated method stub

	}

	private String nodeToString(final Node node) {
		final StringWriter sw = new StringWriter();
		try {
			final Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (final TransformerException te) {
			throw new ItemStreamException("nodeToString Transformer Exception");
		}
		return sw.toString();
	}

}
