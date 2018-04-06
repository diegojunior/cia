import Http from '../http/Http';

export default class Conciliacao {

	static search (data, perfil, status) {
		
		var url = "conciliacao/search";
		
		return Http
				.get(url, {data: data, perfil: perfil, status: status})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}

	static consultarParaExecucao (data, perfil) {
		
		var url = "conciliacao/searchforexecution";

    	return Http
    			.get(url, {data: data, perfil: perfil})
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
			
	static buscaConciliacaoCompleta (data, idPerfil) {
		
		var url = "conciliacao/full";
		
		return Http
				.get(url, {data: data, perfil: idPerfil})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}

	static executar (conciliacoes) {
		
		var url = "conciliacao/executar";

    	return Http
    			.post(url, conciliacoes)
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
			
	static gravar (conciliacao) {
		
		var url = "conciliacao/gravar";

    	return Http
    			.post(url, conciliacao)
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
    }

	
}