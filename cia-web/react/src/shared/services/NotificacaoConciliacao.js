import Http from '../http/Http';

export default class NotificacaoConciliacao {

	static search (dataConciliacao, codigoPerfil, statusConciliacao) {
		
		var url = "notificacao/conciliacao";
		
		return Http
				.get(url, {dataConciliacao: dataConciliacao, codigoPerfil: codigoPerfil, statusConciliacao: statusConciliacao})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
}