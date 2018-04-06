import Http from '../http/Http';

export default class LayoutPosicional {

	static getBy (codigo) {

		var url = "parametrizacao/layout/posicional/list";

		return Http.get(url, {codigo: codigo})
			.then(data => {
				return data;
			}).catch (error => {
				throw error;
			});
	}

	static getBy (codigo, descricao, codigoIdentificador, status) {

		var url = "parametrizacao/layout/posicional/list";

		return Http.get(url, {codigo: codigo, descricao: descricao, codigoIdentificador: codigoIdentificador, status: status})
			.then(data => {
				return data;
			}).catch (error => {
				throw error;
			});
	}

	static getAll() {

		var url = "parametrizacao/layout/posicional/list";

		return Http.get(url)
			.then(data => {
				return data;
			}).catch (error => {
				throw error;
			});
	}

	static incluir (layout) {

		var url = "parametrizacao/layout/posicional/adicionar";
    	return Http
    			.post(url, layout)
					.then (data => {
						return data;
					}).catch (error => {
						throw error;
					});
    }

    static alterar (layout) {
		
		var url = "parametrizacao/layout/posicional/alterar";

    	return Http
    			.put(url, layout)
					.then (data => {
						return data;
					}).catch (error => {
						throw error;
					});
    }

    static remover (layouts) {

		var url = "parametrizacao/layout/posicional/delete";
		
    	return Http
    			.delete(url, layouts)
					.then (data => {
						return data;
					}).catch (error => {
						throw error;
					});
	}
				
	static ativar (id) {
		
		var url = "parametrizacao/layout/posicional/ativar";

    	return Http
    			.put(url, id)
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
    }
	
	static inativar (id) {
		
		var url = "parametrizacao/layout/posicional/inativar";

    	return Http
    			.put(url, id)
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
    }
}