package br.com.totvs.cia.parametrizacao.perfilconciliacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.StatusPerfilEnum;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.repository.PerfilConciliacaoRepository;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.repository.PerfilConciliacaoSpecification;

@Service
public class PerfilConciliacaoService {

	@Autowired
	private PerfilConciliacaoRepository perfilConciliacaoRepository;
	
	public List<PerfilConciliacao> search(final String codigo, final String descricao, final SistemaEnum sistema,
			final TipoLayoutEnum tipoLayout, final String layout, final StatusPerfilEnum status) {
		return this.perfilConciliacaoRepository.findAll(
				PerfilConciliacaoSpecification.search(codigo, descricao, sistema, tipoLayout, layout, status), 
				new Sort(Sort.Direction.ASC, "codigo"));
	}
	
	public List<PerfilConciliacao> getAtivos() {
		return this.perfilConciliacaoRepository.findByStatusOrderByCodigoAsc(StatusPerfilEnum.ATIVO);
	}
	
	public PerfilConciliacao findByCodigoFetchConfiguracaoAndRegras(final String codigo) {
		return this.perfilConciliacaoRepository.findByCodigoFetchConfiguracaoAndRegras(codigo);
	}
	
	public PerfilConciliacao findByCodigo(final String codigo) {
		return this.perfilConciliacaoRepository.findByCodigo(codigo);
	}
	
	public List<PerfilConciliacao> findAllBy(final String codigo) {
		return this.perfilConciliacaoRepository.findAll(PerfilConciliacaoSpecification.findBy(codigo));
	}
	
	public PerfilConciliacao getOne(final String id) {
		return this.perfilConciliacaoRepository.getOne(id);
	}

	public PerfilConciliacao save(final PerfilConciliacao entity) {
		PerfilConciliacao perfilEncontrado = this.perfilConciliacaoRepository.findByCodigo(entity.getCodigo());

		final Boolean isPerfilExistente = perfilEncontrado != null;
		if(isPerfilExistente) {
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		}
		PerfilConciliacao perfilPersistido = this.perfilConciliacaoRepository.save(entity);
		return perfilPersistido;
	}
	
	public PerfilConciliacao ativar(final String id) {
		PerfilConciliacao perfilEncontrado = this.perfilConciliacaoRepository.findOne(id);
		
		final Boolean isPerfilInexistente = perfilEncontrado == null;
		if(isPerfilInexistente) {
			throw new CiaBusinessException("Perfil de Conciliação não encontrado!");
		}
		
		perfilEncontrado.setStatus(StatusPerfilEnum.ATIVO);
		return this.perfilConciliacaoRepository.save(perfilEncontrado);
		
	}
	
	public PerfilConciliacao inativar(final String id) {
		PerfilConciliacao perfilEncontrado = this.perfilConciliacaoRepository.findOne(id);
		
		final Boolean isPerfilInexistente = perfilEncontrado == null;
		if(isPerfilInexistente) {
			throw new CiaBusinessException("Perfil de Conciliação não encontrado!");
		}
		
		perfilEncontrado.setStatus(StatusPerfilEnum.INATIVO);
		return this.perfilConciliacaoRepository.save(perfilEncontrado);
		
	}

	public void delete(final PerfilConciliacao entity) {
		this.perfilConciliacaoRepository.delete(entity);
	}

	public void deleteAll() {
		this.perfilConciliacaoRepository.deleteAll();
	}

	public void delete(final Iterable<? extends PerfilConciliacao> entities) {
		this.perfilConciliacaoRepository.delete(entities);
	}
}