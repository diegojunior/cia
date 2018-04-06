import Http from '../http/Http';

export default class NotificacaoCarga {

	static search (data, sistema, servico) {
		
		var url = "notificacao/carga";
		
		return Http
				.get(url, {data: data, sistema: sistema, servico: servico})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
}