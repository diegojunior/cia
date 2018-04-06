import Http from '../http/Http';

export default class UnidadeImportacaoBloco {

   	static getByLayout(idLayout) {
    	return Http
	    		.get("parametrizacao/unidadeimportacao/layout", {layout: idLayout})
	    			.then(response => {
	    				return response;
	    			}).catch (error => {
	    				throw error;
	    			});
    }
}