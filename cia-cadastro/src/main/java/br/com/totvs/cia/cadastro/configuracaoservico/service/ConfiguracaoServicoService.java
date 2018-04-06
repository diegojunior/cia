package br.com.totvs.cia.cadastro.configuracaoservico.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.TipoServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.repository.ConfiguracaoServicoRepository;
import br.com.totvs.cia.cadastro.configuracaoservico.repository.ConfiguracaoServicoSpecification;
import br.com.totvs.cia.infra.exception.CiaBusinessException;

@Service
public class ConfiguracaoServicoService {
	
	private static final Logger log = Logger.getLogger(ConfiguracaoServicoService.class);
	
	@Autowired
	private ConfiguracaoServicoRepository servicoRepository;
	
	public List<ConfiguracaoServico> search(final String codigo, final String descricao, final SistemaEnum sistema,
			final ServicoEnum servico, final TipoServicoEnum tipoServico) {
		return this.servicoRepository.findAll(ConfiguracaoServicoSpecification.search(codigo, descricao, sistema, servico, tipoServico));
	}
	
	public List<ConfiguracaoServico> findBy(final SistemaEnum sistema) {
		List<ConfiguracaoServico> configuracoes = this.servicoRepository.findBySistema(sistema);
		
		if (configuracoes == null || configuracoes.isEmpty()) {
			throw new CiaBusinessException("Não há Configurações de Serviços cadastradas!");
		}
		return configuracoes;
	}
	
	public ConfiguracaoServico findOneFetchCampos(final String id) {
		return this.servicoRepository.findOneFetchCampos(id);
	}
	
	public ConfiguracaoServico getBy(final String codigo) {
		return this.servicoRepository.getByCodigo(codigo);
	}

	public ConfiguracaoServico findOne(final String id) {
		return this.servicoRepository.findOne(id);
	}
	
	public List<ConfiguracaoServico> findAll() {
		return this.servicoRepository.findAll();
	}

	public ConfiguracaoServico incluir(final ConfiguracaoServico entity) {
		return this.servicoRepository.save(entity);
	}
	
	public ConfiguracaoServico alterar(final ConfiguracaoServico entity) {
		try {
			return this.servicoRepository.save(entity);
		} catch (Exception ex) {
			log.error(ex);
			return entity;
		}
	}
	
	public void saveAll(final List<ConfiguracaoServico> servicos) {
		for (ConfiguracaoServico configuracaoServico : servicos) {
			this.incluir(configuracaoServico);
		}
	}

	public void delete(final ConfiguracaoServico entity) {
		this.servicoRepository.delete(entity);
	}
	
	public void delete(final List<ConfiguracaoServico> configuracoes) {
		this.servicoRepository.delete(configuracoes);
	}

	public void deleteAll() {
		this.servicoRepository.deleteAll();
	}	
}