
package br.com.totvs.cia.conciliacao.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.model.ConciliacaoForPainel;
import br.com.totvs.cia.conciliacao.repository.ConciliacaoRepository;

@Service
public class PainelConciliacaoService {

	private static final Logger log = Logger.getLogger(PainelConciliacaoService.class);
	
	@Autowired
	private ConciliacaoRepository conciliacaoRepository;
	
	public List<ConciliacaoForPainel> listBy(final Date data) {
		try {
			return this.conciliacaoRepository.findProjectedColumnsForPainel(data);
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public Conciliacao findOneBy(final String id) {
		return this.conciliacaoRepository.findOne(id);
	}
}