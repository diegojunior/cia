import Http from '../http/Http';
import ConfiguracaoServicoService from './ConfiguracaoServico';

export default class PerfilConciliacao {

	static search (codigo, descricao, sistema, tipoLayout, layout, status) {
		
		var url = "parametrizacao/perfilconciliacao/search";
		
		return Http
				.get(url, {codigo: codigo, 
						   descricao: descricao, 
						   sistema: sistema, 
						   tipoLayout: tipoLayout,
						   layout: layout,
						   status: status})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}

	static getAtivos () {
		
		var url = "parametrizacao/perfilconciliacao/ativos";
		
		return Http
				.get(url)
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}

	static incluir (perfil) {
		
		var url = "parametrizacao/perfilconciliacao/incluir";

    	return Http
    			.post(url, perfil)
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}

	static ativar (id) {
		
		var url = "parametrizacao/perfilconciliacao/ativar";

    	return Http
    			.put(url, id)
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
    }
	
	static inativar (id) {
		
		var url = "parametrizacao/perfilconciliacao/inativar";

    	return Http
    			.put(url, id)
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
    }
}