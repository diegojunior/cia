package br.com.totvs.cia.carga.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.carga.model.UnidadeCarga;
import br.com.totvs.cia.carga.repository.UnidadeCargaRepository;

@Service
public class UnidadeCargaService {

	@Autowired
	private UnidadeCargaRepository unidadeCargaRepository;
	
	public void save(final UnidadeCarga unidade){
		this.unidadeCargaRepository.save(unidade);
	}

	public void saveAll(final List<? extends UnidadeCarga> items){
		this.unidadeCargaRepository.save(items);
	}
}