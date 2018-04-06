package br.com.totvs.cia.importacao.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.totvs.cia.cadastro.agente.model.Agente;
import br.com.totvs.cia.cadastro.agente.service.AgenteService;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.importacao.model.Arquivo;
import br.com.totvs.cia.importacao.model.DadosArquivo;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.repository.ArquivoRepository;
import br.com.totvs.cia.importacao.repository.ImportacaoRepository;
import br.com.totvs.cia.importacao.repository.ImportacaoSpecification;
import br.com.totvs.cia.notificacao.model.NotificacaoImportacao;
import br.com.totvs.cia.notificacao.model.StatusEnum;
import br.com.totvs.cia.notificacao.service.NotificacaoImportacaoService;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;

@Service
public class ImportacaoService {
	
	@Autowired
	private ImportacaoBatchJobService importacaoBatchJobService;

	@Autowired
	private ArquivoRepository arquivoRepository;
	
	@Autowired
	private ImportacaoRepository importacaoRepository;

	@Autowired
	private AgenteService agenteService;
	
	@Autowired
	private UnidadeImportacaoService unidadeImportacaoService;
	
	@Autowired
	private UnidadeLayoutImportacaoService unidadeLayoutImportacaoService;
	
	@Autowired
	private NotificacaoImportacaoService notificacaoImportacaoService;
	
	public Arquivo saveFile(final MultipartFile file, final String idFile) {
		try {
			
			if (file != null) {
				
				if (idFile != null 
						&& !"".equals(idFile)) {
					this.arquivoRepository.delete(idFile);
				}
				
				final Arquivo arquivo = new Arquivo(null, file.getOriginalFilename(), file.getSize(), new DadosArquivo(null, file.getBytes()));
				
				return this.arquivoRepository.save(arquivo);
			}

			throw new RuntimeException("Arquivo nao encontrado.");
			
		} catch (final IOException e) {
			throw new RuntimeException("Erro ao incluir o arquivo");
		}
	}
	
	public Importacao executar(final Importacao importacaoParam){
		
		final Importacao importacao = this.verificaConfigura(importacaoParam);
		
		this.importacaoBatchJobService.run(importacao);
		
		return importacao;
	}

	public List<Importacao> getBy(final Date data, final SistemaEnum sistema, final Layout layout, final Agente agente) {
		return this.importacaoRepository.findAll(ImportacaoSpecification.getBy(data, sistema, layout, agente));
	}
	
	public List<Importacao> getBy(final Date dataImportacao, final Layout layout) {
		return this.importacaoRepository.findByDataImportacaoAndLayout(dataImportacao, layout);
	}
	
	public Arquivo findOneFile(final String id) {
		return this.arquivoRepository.findOne(id);
	}

	public <S extends Importacao> S save(final S entity) {
		// Agente nao precisam estar em cascade
		if (entity.getAgente() != null) {
			this.agenteService.save(entity.getAgente());
		}
		return this.importacaoRepository.save(entity);
	}
	
	public Importacao loadImportacaoAndUnidade(final String id) {
		return this.importacaoRepository.loadImportacaoAndUnidades(id);
	}
	
	public Importacao findOne(final String id) {
		return this.importacaoRepository.findOne(id);
	}

	public Importacao getOne(final String id) {
		return this.importacaoRepository.getOne(id);
	}

	public void delete(final Importacao entity) {
		this.importacaoRepository.delete(entity);
	}
	
	public void deleteFile(final Arquivo entity) {
		this.arquivoRepository.delete(entity);
	}

	public void deleteFiles(final Iterable<? extends Arquivo> entities) {
		this.arquivoRepository.delete(entities);
	}

	public List<Importacao> findAll(final Specification<Importacao> spec) {
		return this.importacaoRepository.findAll(spec);
	}
	
	public List<Importacao> findAll(final Specification<Importacao> spec, final Sort sort) {
		return this.importacaoRepository.findAll(spec, sort);
	}
	
	public void delete(final Iterable<? extends Importacao> entities) {
		this.importacaoRepository.delete(entities);
	}

	public void deleteAll() {
		this.importacaoRepository.deleteAll();
	}

	public Importacao findBy(final String id) {
		if (id != null) {
			return this.importacaoRepository.findOne(id);
		}
		return null;
	}
	
	public Boolean existeUnidadesImportadas(final Importacao importacao) {
		return this.importacaoRepository.countUnidadesPorImportacao(importacao.getId()) > 0;
	}
	
	private Importacao verificaConfigura(final Importacao importacaoParam) {
		
		final List<Importacao> importacoesExistentes = this.getBy(importacaoParam.getDataImportacao(), importacaoParam.getLayout());
		importacaoParam.inicializaStatus();
		for (final Importacao importacaoExistente : importacoesExistentes) {
			final Arquivo arquivoAntigo = importacaoExistente.getArquivo();
			final Importacao importacaoConfigurada = this.save(this.configuraNovaImportacao(importacaoExistente, importacaoParam));
			if (!arquivoAntigo.getId().equals(importacaoConfigurada.getArquivo().getId())) {
				this.arquivoRepository.delete(arquivoAntigo);
			}
			return importacaoConfigurada;
		}
		
		return this.save(importacaoParam);
		

	}

	private Importacao configuraNovaImportacao(final Importacao importacaoExistente, final Importacao importacaoParam) {
		this.removeUnidadesDaImportacao(importacaoExistente);
		this.removeEquivalenciasDaImportacao(importacaoExistente);
		this.removeNotificacoes(importacaoExistente);
		importacaoExistente.configura(importacaoParam);
		return importacaoExistente;
	}

	private void removeUnidadesLayout(final Importacao importacaoExistente) {
		this.unidadeLayoutImportacaoService.removeUnidadesLayoutPor(importacaoExistente);
	}

	private void removeUnidadesDaImportacao(final Importacao importacao) {
		this.unidadeImportacaoService.delete(importacao);
		this.removeUnidadesLayout(importacao);
	}

	private void removeEquivalenciasDaImportacao(final Importacao importacaoExistente) {
		importacaoExistente.getEquivalencias().clear();
	}
	
	private void removeNotificacoes(final Importacao importacaoExistente) {
		final List<NotificacaoImportacao> notificacoes = this.notificacaoImportacaoService.findAll(importacaoExistente);
		if (!notificacoes.isEmpty()) this.notificacaoImportacaoService.delete(notificacoes);
		
	}
	
	public void removeArquivoAntigo(final Importacao importacaoExistente) {
		this.arquivoRepository.delete(importacaoExistente.getArquivo());
		
	}
	
	public Importacao atualizarStatus(final String importacaoId, final StatusEnum status) {
		final Importacao importacao = this.findBy(importacaoId);
		return this.save(importacao.atualizaStatus(status));
	}

}