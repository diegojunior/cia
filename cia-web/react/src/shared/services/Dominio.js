import Http from '../http/Http';

export default class Dominio {
    
    static getBy(codigo, tipo) {
    	return Http
	    		.get("parametrizacao/dominio/list", {codigo: codigo, tipo: tipo})
	    			.then(response => {
	    				return response;
	    			}).catch (error => {
	    				throw error;
	    			})
    }

    static getAll () {
        return Http
        		.get("parametrizacao/dominio/list")
	        		.then (response => {
						return response;
					 }).catch (error => {
						throw error;
					 });
    }

    static incluir (dominio) {
    	return Http
    			.post("parametrizacao/dominio/incluir", dominio)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static alterar (dominio) {
    	return Http
    			.put("parametrizacao/dominio/alterar", dominio)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static excluir (selectedList) {
    	return Http
    			.delete("parametrizacao/dominio/delete", selectedList)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }
}