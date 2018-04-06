import Http from '../http/Http';

export default class Cliente {

	static getAll (sistema) {
		
		var url = "gateway/cliente/all";
		
		return Http
				.get(url, {sistema: sistema})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}

	static getBy (sistema, codigo, pagina, tamanhoPagina) {
		
		var url = "gateway/cliente/search";
		
		return Http
				.get(url, {sistema: sistema, codigo: codigo, pagina: pagina, tamanhoPagina: tamanhoPagina})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
}