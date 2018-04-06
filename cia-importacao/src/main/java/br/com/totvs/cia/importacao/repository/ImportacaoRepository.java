package br.com.totvs.cia.importacao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;

public interface ImportacaoRepository extends JpaRepository<Importacao, String>, JpaSpecificationExecutor<Importacao> {

	@Query("select count(imp) from UnidadeLayoutImportacao imp  where imp.importacao.id = :id")
	public Long countUnidadesPorImportacao(@Param("id") String id);
	
	@Query("select imp from Importacao imp left join fetch imp.unidades unidades where imp.id = :id")
	public Importacao loadImportacaoAndUnidades(@Param("id") String id);

	public List<Importacao> findByDataImportacaoAndLayout(Date dataImportacao, Layout layout);
}
