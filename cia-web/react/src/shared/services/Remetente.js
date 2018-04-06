import Http from '../http/Http';

export default class Remetente {
	
	static getBy (sistema) {
		
		var url = "gateway/equivalencia/remetente/all";
		
		return Http
				.get(url, {sistema: sistema})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
}