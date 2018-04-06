import Http from '../http/Http';

export default class UnidadeImportacaoChave {

    static getBy(codigo, descricao, tipoLayout, codigoLayout, codigoIdentificador) {
    	return Http
				.get("parametrizacao/unidadeimportacao/chave/search", 
					 {codigo: codigo, 
					  descricao: descricao, 
					  tipoLayout: tipoLayout, 
					  codigoLayout: codigoLayout, 
					  codigoIdentificador: codigoIdentificador})
	    			.then(response => {
	    				return response;
	    			}).catch (error => {
	    				throw error;
	    			});
    }

	static getByLayout(idLayout) {
    	return Http
	    		.get("parametrizacao/unidadeimportacao/chave/layout", {layout: idLayout})
	    			.then(response => {
	    				return response;
	    			}).catch (error => {
	    				throw error;
	    			});
    }

    static incluir (unidadeImportacao) {
    	return Http
    			.post("parametrizacao/unidadeimportacao/chave/incluir", unidadeImportacao)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static alterar (unidadeImportacao) {
    	return Http
    			.put("parametrizacao/unidadeimportacao/chave/alterar", unidadeImportacao)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static excluir (selectedList) {
    	return Http
    			.delete("parametrizacao/unidadeimportacao/chave/delete", selectedList)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }
}