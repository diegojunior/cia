package br.com.totvs.cia.conciliacao.service;

import static br.com.totvs.cia.conciliacao.model.LoteConciliacao.buildLoteConciliacao;
import static br.com.totvs.cia.notificacao.model.NotificacaoConciliacao.concluirNotificacao;
import static br.com.totvs.cia.notificacao.model.NotificacaoConciliacao.iniciar;
import static br.com.totvs.cia.notificacao.model.NotificacaoConciliacao.notificarErro;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.carga.model.UnidadeCarga;
import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.model.LoteConciliacao;
import br.com.totvs.cia.conciliacao.model.StatusConciliacaoEnum;
import br.com.totvs.cia.conciliacao.model.StatusLoteConciliacaoEnum;
import br.com.totvs.cia.conciliacao.model.UnidadeConciliacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacao;
import br.com.totvs.cia.importacao.service.UnidadeImportacaoService;
import br.com.totvs.cia.notificacao.model.NotificacaoConciliacao;
import br.com.totvs.cia.notificacao.service.NotificacaoConciliacaoService;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.CampoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.ConfiguracaoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;

@Service
public class ProcessamentoConciliacao {
	
	private static final Logger log = Logger.getLogger(ProcessamentoConciliacao.class);

	@Autowired
	private ConciliacaoService conciliacaoService;
	
	@Autowired
	private NotificacaoConciliacaoService notificacaoConciliacaoService;
	
	@Autowired
	private UnidadeImportacaoService unidadeImportacaoService;
	
	public void processar(final Conciliacao conciliacao) {
		log.info("Iniciando processamento da conciliação");
		final List<NotificacaoConciliacao> notificacoes = Lists.newArrayList();
		Conciliacao conciliacaoPersistida = this.conciliacaoService.iniciarConciliacao(conciliacao);
		try {
			final ConfiguracaoPerfilConciliacao configuracao = conciliacaoPersistida.getPerfil().getConfiguracao();
			
			final List<UnidadeCarga> unidadesCarga = conciliacaoPersistida.getLoteServicoConfiguradoParaCarga(configuracao.getServico()).getUnidadesCarga();
			
			final List<UnidadeImportacao> unidadesImportacao = this.unidadeImportacaoService.findAll(conciliacaoPersistida.getImportacao(), configuracao.getSessao(), configuracao.getUnidade());
			
			final List<UnidadeCarga> unidadesCargaComRegrasAplicadas = new RegraConciliacaoCarga(conciliacaoPersistida.getPerfil()).aplicar(unidadesCarga);
			
			final List<UnidadeImportacao> unidadesImportacaoComRegrasAplicadas = new RegraConciliacaoImportacao(conciliacaoPersistida.getPerfil()).aplicar(unidadesImportacao);
			
			this.executarConciliacao(unidadesCargaComRegrasAplicadas, unidadesImportacaoComRegrasAplicadas, conciliacaoPersistida.getPerfil(), conciliacaoPersistida, notificacoes);
		} catch (Exception e) {
			log.error("Ocorreu erro na conciliação.", e);
		}	
		
		this.terminaConciliacao(conciliacaoPersistida, notificacoes);
	}
	
	private void executarConciliacao(final List<UnidadeCarga> unidadesCarga,
			final List<UnidadeImportacao> unidadesImportacao,
			final PerfilConciliacao perfil, 
			final Conciliacao conciliacao, 
			final List<NotificacaoConciliacao> notificacoes) {
		log.info("Executando a conciliacao.");
		
		final LoteConciliacao loteConciliacao = buildLoteConciliacao(conciliacao);
		
		try {
			
			final Map<Integer, UnidadeImportacao> unidadesImportadasConsolidadas = this.consolidarUnidadesImportadas(perfil, unidadesImportacao);
			
			final Map<Integer, UnidadeCarga> unidadesCargaConsolidadas = this.consolidarUnidadesCarga(perfil, unidadesCarga);
			
			this.notificarInicioConciliacao(conciliacao, notificacoes, unidadesCargaConsolidadas.size(), unidadesImportadasConsolidadas.size());
			
			this.conciliarCargaImportacao(loteConciliacao, unidadesCargaConsolidadas, unidadesImportadasConsolidadas, perfil.getConfiguracao());
			
			this.conciliarSomenteCarga(loteConciliacao, unidadesCargaConsolidadas, perfil.getConfiguracao());
			
			this.conciliarSomenteImportacao(loteConciliacao, unidadesImportadasConsolidadas, perfil.getConfiguracao());
			
			this.notificarTerminoConciliacao(conciliacao, notificacoes);
			
		} catch (Exception e) {
			log.error("Ocorreu um erro ao executar a conciliação. Notificando o erro e abortando a conciliação", e);
			conciliacao.comStatus(StatusConciliacaoEnum.ERRO);
			StringBuilder msg = NotificacaoConciliacao.gerarMsg(conciliacao, new Date());
			msg.append(e.getMessage());
			notificacoes.add(notificarErro(conciliacao, msg.toString()));
		}
	}
	
	private Map<Integer, UnidadeImportacao> consolidarUnidadesImportadas(final PerfilConciliacao perfil,
			final List<UnidadeImportacao> unidadesImportacao) {
		final Map<Integer, UnidadeImportacao> consolidados = new HashMap<>();
		if (perfil.consolidarDados()) {
			unidadesImportacao.forEach(unidade -> this.consolidar(unidade, consolidados, perfil.getConfiguracao()));
			return consolidados;
		}
		return this.gerarMapComChaves(unidadesImportacao, perfil.getConfiguracao());
	}
	
	private Map<Integer, UnidadeCarga> consolidarUnidadesCarga(final PerfilConciliacao perfil,
			final List<UnidadeCarga> unidadesCarga) {
		final Map<Integer, UnidadeCarga> consolidados = new HashMap<>();
		if (perfil.consolidarDados()) {
			unidadesCarga.forEach(unidade -> this.consolidar(unidade, consolidados, perfil.getConfiguracao()));
			return consolidados;
		}
		return this.gerarMapComChavesCarga(unidadesCarga, perfil.getConfiguracao());
	}
	
	private void consolidar(final UnidadeImportacao unidadeConsolidar, 
			final Map<Integer, UnidadeImportacao> consolidados, 
			final ConfiguracaoPerfilConciliacao configuracao) {
		Map<Dominio, CampoPerfilConciliacao> chaves = configuracao.getChaves().stream().collect(Collectors.toMap((campoChave) -> campoChave.getCampoImportacao(), (campoChave) -> campoChave));
		Map<Dominio, CampoPerfilConciliacao> camposConciliaveis = configuracao.getCamposConciliaveis().stream().collect(Collectors.toMap((campoConciliavel) -> campoConciliavel.getCampoImportacao(), (campoConciliavel) -> campoConciliavel));
		Integer chave = unidadeConsolidar.getHashChave(chaves);
		UnidadeImportacao unidadeEncontrada = consolidados.get(chave);
		if (unidadeEncontrada != null) {
			unidadeEncontrada.consolidar(camposConciliaveis, unidadeConsolidar);
		} else {
			consolidados.put(chave, unidadeConsolidar);
		}
	}
	
	private void consolidar(final UnidadeCarga unidadeConsolidar, 
			final Map<Integer, UnidadeCarga> consolidados, 
			final ConfiguracaoPerfilConciliacao configuracao) {
		Map<String, CampoPerfilConciliacao> chaves = configuracao.getChaves().stream().collect(Collectors.toMap((campoChave) -> campoChave.getCampoCarga().getCampo(), (campoChave) -> campoChave));
		Map<String, CampoPerfilConciliacao> camposConciliaveis = configuracao.getCamposConciliaveis().stream().collect(Collectors.toMap((campoConciliavel) -> campoConciliavel.getCampoCarga().getCampo(), (campoConciliavel) -> campoConciliavel));
		Map<String, CampoPerfilConciliacao> camposInformativos = configuracao.getCamposInformativos().stream().collect(Collectors.toMap((campoInformatico) -> campoInformatico.getCampoCarga().getCampo(), (campoInformatico) -> campoInformatico));
		Integer chave = unidadeConsolidar.getHashChave(chaves);
		UnidadeCarga unidadeEncontrada = consolidados.get(chave);
		if (unidadeEncontrada != null) {
			unidadeEncontrada.consolidar(camposConciliaveis, camposInformativos, unidadeConsolidar);
		} else {
			consolidados.put(chave, unidadeConsolidar);
		}
	}

	private Map<Integer, UnidadeImportacao> gerarMapComChaves(final List<UnidadeImportacao> unidadesImportacao,
		final ConfiguracaoPerfilConciliacao configuracao) {
		Map<Dominio, CampoPerfilConciliacao> chaves = configuracao.getChaves().stream().collect(Collectors.toMap((campoChave) -> campoChave.getCampoImportacao(), (campoChave) -> campoChave));
		final Map<Integer, UnidadeImportacao> map = new HashMap<>();
		unidadesImportacao.forEach(unidade -> {
			map.put(unidade.getHashChave(chaves), unidade);
		});
		return map;
	}
	
	private Map<Integer, UnidadeCarga> gerarMapComChavesCarga(final List<UnidadeCarga> unidadesCarga,
			final ConfiguracaoPerfilConciliacao configuracao) {
			List<CampoPerfilConciliacao> chaves = configuracao.getChaves().stream().collect(Collectors.toList());
			final Map<Integer, UnidadeCarga> map = new HashMap<>();
			unidadesCarga.forEach(unidade -> {
				map.put(unidade.getHashChave(chaves), unidade);
			});
			return map;
		}
	
	private void notificarInicioConciliacao(final Conciliacao conciliacao, final List<NotificacaoConciliacao> notificacoes,
			final int totalCarga, final int totalImportacao) {
		notificacoes.add(iniciar(new Date(), conciliacao, totalCarga, totalImportacao));
	}

	private void conciliarCargaImportacao(final LoteConciliacao loteConciliacao, final Map<Integer, UnidadeCarga> unidadesCargaConsolidadas,
			final Map<Integer, UnidadeImportacao> unidadesImportacaoPorChave, final ConfiguracaoPerfilConciliacao configuracaoPerfil) {
		log.info("Conciliando informações de Carga - Importação.");
		for (Iterator<Map.Entry<Integer, UnidadeCarga>> iterator = unidadesCargaConsolidadas.entrySet().iterator(); iterator.hasNext();) {
			Entry<Integer, UnidadeCarga> nextUnidadeCarga = iterator.next();
			final UnidadeCarga unidadeCarga = nextUnidadeCarga.getValue();
			final UnidadeImportacao unidadeImportacao = unidadesImportacaoPorChave.remove(nextUnidadeCarga.getKey());
			if (unidadeImportacao != null) {
				final UnidadeConciliacao unidadeConciliacao = new UnidadeConciliacao(loteConciliacao);
				unidadeConciliacao.gerarChaves(configuracaoPerfil, unidadeCarga);
				unidadeConciliacao.gerarConciliacoes(configuracaoPerfil.getCamposConciliaveis(), unidadeCarga, unidadeImportacao);
				unidadeConciliacao.gerarCamposInformativos(configuracaoPerfil.getCamposInformativos(), unidadeCarga);
				iterator.remove();
			}
		}
		if (loteConciliacao.possuiUnidadeComStatusDiferenteOK()) {
			loteConciliacao.setStatusLote(StatusLoteConciliacaoEnum.DIVERGENTE);
		} else {
			loteConciliacao.setStatusLote(StatusLoteConciliacaoEnum.OK);
		}
	}
	
	private void conciliarSomenteCarga(final LoteConciliacao loteConciliacao, 
			final Map<Integer, UnidadeCarga> unidadesCargaConsolidadas,
			final ConfiguracaoPerfilConciliacao configuracaoPerfil) {
		log.info("Conciliando itens somente da carga.");
		for (UnidadeCarga unidadeCarga: unidadesCargaConsolidadas.values()) {
			final UnidadeConciliacao unidadeConciliacao = new UnidadeConciliacao(loteConciliacao);
			unidadeConciliacao.gerarChaves(configuracaoPerfil, unidadeCarga);
			unidadeConciliacao.gerarConciliacoes(configuracaoPerfil.getCamposConciliaveis(), unidadeCarga);
			unidadeConciliacao.gerarCamposInformativos(configuracaoPerfil.getCamposInformativos(), unidadeCarga);
		}
	}
	
	private void conciliarSomenteImportacao(final LoteConciliacao loteConciliacao,
			final Map<Integer, UnidadeImportacao> unidadesImportacaoPorChave,
			final ConfiguracaoPerfilConciliacao configuracaoPerfil) {
		log.info("Conciliando itens somente da importação.");
		for (final UnidadeImportacao unidadeImportacao : unidadesImportacaoPorChave.values()) {
			final UnidadeConciliacao unidadeConciliacao = new UnidadeConciliacao(loteConciliacao);
			unidadeConciliacao.gerarChaves(configuracaoPerfil, unidadeImportacao);
			unidadeConciliacao.gerarConciliacoes(configuracaoPerfil.getCamposConciliaveis(), unidadeImportacao);
			unidadeConciliacao.gerarCamposInformativos(configuracaoPerfil.getCamposInformativos(), null);
		}
		
	}
	
	private void notificarTerminoConciliacao(final Conciliacao conciliacao,
			final List<NotificacaoConciliacao> notificacoes) {
		log.info("Notificando o termino da conciliação");
		notificacoes.add(concluirNotificacao(new Date(), conciliacao));
	}

	private void terminaConciliacao(final Conciliacao conciliacao, final List<NotificacaoConciliacao> notificacoes) {
		log.info("Finalizando a conciliacao.");
		if (!conciliacao.isErro()) {
			if (conciliacao.possuiLoteDivergente()) {
				conciliacao.comStatus(StatusConciliacaoEnum.DIVERGENTE);
			} else if (StatusConciliacaoEnum.ANDAMENTO.equals(conciliacao.getStatus())) {
				conciliacao.comStatus(StatusConciliacaoEnum.OK);
			}
		}
		this.conciliacaoService.save(conciliacao);
		this.notificacaoConciliacaoService.save(notificacoes);
	}
}