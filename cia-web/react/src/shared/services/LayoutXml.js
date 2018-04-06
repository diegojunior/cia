import Http from '../http/Http';

export default class LayoutXml {

	static getBy (codigo, descricao, status) {

		var url = "parametrizacao/layout/xml/list";

		return Http.get(url, {codigo: codigo, descricao: descricao, status: status})
			.then(data => {
				return data;
			}).catch (error => {
				throw error;
			});
	}

	static getAll() {

		var url = "parametrizacao/layout/xml/list";

		return Http.get(url)
			.then(data => {
				return data;
			}).catch (error => {
				throw error;
			});
	}

	static incluir (layout) {

		var url = "parametrizacao/layout/xml/adicionar";

    	return Http
    			.post(url, layout)
					.then (data => {
						return data;
					}).catch (error => {
						throw error;
					});
    }

    static alterar (layout) {
		
		var url = "parametrizacao/layout/xml/alterar";

    	return Http
    			.put(url, layout)
					.then (data => {
						return data;
					}).catch (error => {
						throw error;
					});
    }

    static remover (layouts) {

		var url = "parametrizacao/layout/xml/delete";
		
    	return Http
    			.delete(url, layouts)
					.then (data => {
						return data;
					}).catch (error => {
						throw error;
					});
	}
				
	static ativar (id) {
		
		var url = "parametrizacao/layout/xml/ativar";

    	return Http
    			.put(url, id)
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
    }
	
	static inativar (id) {
		
		var url = "parametrizacao/layout/xml/inativar";

    	return Http
    			.put(url, id)
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
    }
}