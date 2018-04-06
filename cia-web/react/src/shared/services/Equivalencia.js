import Http from '../http/Http';

export default class Equivalencia {
	
	static getBy (sistemaParam, senderParam, tipoEquivalenciaParam) {
		
		var url = "gateway/equivalencia/sistema/" + sistemaParam + "/tipoEquivalencia/" + tipoEquivalenciaParam;
		
		return Http
				.get(url, {sender: senderParam})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
}