import Http from '../http/Http';

export default class NotificacaoImportacao {

	static search (data, sistema, tipoLayout, codigolayout) {
		
		var url = "notificacao/importacao";
		
		return Http
				.get(url, {data: data, sistema: sistema, tipoLayout: tipoLayout, codigolayout: codigolayout})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
}