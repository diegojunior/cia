import Http from '../http/Http';

export default class Grupo {

	static getBy (sistema, codigo, pagina, tamanhoPagina) {
		
		var url = "gateway/grupo/search";
		
		return Http
				.get(url, {sistema: sistema, codigo: codigo, pagina: pagina, tamanhoPagina: tamanhoPagina})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
}