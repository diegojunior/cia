import Http from '../http/Http';

export default class UnidadeImportacaoBloco {

    static search(codigo, descricao, codigoLayout) {
    	return Http
				.get("parametrizacao/unidadeimportacao/bloco/search", 
					 {codigo: codigo, 
					  descricao: descricao, 
					  codigoLayout: codigoLayout})
	    			.then(response => {
	    				return response;
	    			}).catch (error => {
	    				throw error;
	    			});
    }

	static getByLayout(idLayout) {
    	return Http
	    		.get("parametrizacao/unidadeimportacao/bloco/layout", {layout: idLayout})
	    			.then(response => {
	    				return response;
	    			}).catch (error => {
	    				throw error;
	    			});
    }

    static incluir (unidadeImportacao) {
    	return Http
    			.post("parametrizacao/unidadeimportacao/bloco/incluir", unidadeImportacao)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static alterar (unidadeImportacao) {
    	return Http
    			.put("parametrizacao/unidadeimportacao/bloco/alterar", unidadeImportacao)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static excluir (selectedList) {
    	return Http
    			.delete("parametrizacao/unidadeimportacao/bloco/delete", selectedList)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }
}