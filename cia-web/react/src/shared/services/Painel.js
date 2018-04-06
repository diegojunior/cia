import Http from '../http/Http';

export default class Painel {

	static getConciliacoes (data) {
		
		var url = "painel/conciliacao";
		
		return Http
				.get(url, {data: data})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
}