package br.com.totvs.cia.cadastro.configuracaoservico.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.repository.CampoConfiguracaoServicoRepository;

@Service
public class CampoConfiguracaoServicoService {
	
	private static final Logger log = Logger.getLogger(CampoConfiguracaoServicoService.class);
	
	@Autowired
	private CampoConfiguracaoServicoRepository campoConfiguracaoServicoRepository;

	public CampoConfiguracaoServico save(final CampoConfiguracaoServico entity) {
		return this.campoConfiguracaoServicoRepository.save(entity);
	}
	
	public CampoConfiguracaoServico getBy(final String campo, ConfiguracaoServico servico) {
		return this.campoConfiguracaoServicoRepository.getByCampoAndServico(campo, servico.getCodigo());
	}

	public CampoConfiguracaoServico findOne(final String id) {
		return this.campoConfiguracaoServicoRepository.findOne(id);
	}
	
	public void delete(final CampoConfiguracaoServico entity) {
		try {
			this.campoConfiguracaoServicoRepository.delete(entity);
		} catch (Exception ex) {
			log.error("Não foi possível remover o Campo da Configuração de Serviço.", ex);
		}
	}
}