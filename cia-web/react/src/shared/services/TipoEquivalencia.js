import Http from '../http/Http';

export default class TipoEquivalencia {
	
	static getBy (sistema) {
		
		var url = "gateway/equivalencia/tipo/all";
		
		return Http
				.get(url, {sistema: sistema})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
}