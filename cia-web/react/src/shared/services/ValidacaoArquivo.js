import Http from '../http/Http';

export default class ValidacaoArquivoService {

    static getBy(tipoLayout, layout, campoValidacao, localValidacao) {
    	return Http
	    		.get("parametrizacao/validacao/arquivo/search", 
					{
						tipoLayout: tipoLayout,
						layout: layout,
						campoValidacao: campoValidacao,
						localValidacao: localValidacao
					})
	    			.then(response => {
	    				return response;
	    			}).catch (error => {
	    				throw error;
	    			});
    }

    static incluir (validacao) {
    	return Http
    			.post("parametrizacao/validacao/arquivo/incluir", validacao)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static alterar (validacao) {
    	return Http
    			.put("parametrizacao/validacao/arquivo/alterar", validacao)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static excluir (selectedList) {
    	return Http
    			.delete("parametrizacao/validacao/arquivo/delete", selectedList)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }
}