import Http from '../http/Http';

export default class ConfiguracaoServico {

	static getBy (sistema) {
		
		var url = "cadastro/configuracaoservico/by";
		
		return Http
				.get(url, {sistema: sistema})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}
	
	static search (codigo, descricao, sistema, servico, tipoServico) {
		
		var url = "cadastro/configuracaoservico/search";
		
		return Http
				.get(url, {codigo: codigo, 
						   descricao: descricao,
						   sistema: sistema,
						   servico: servico,
						   tipoServico: tipoServico})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}

	static incluir (configuracaoServico) {
    	return Http
    			.post("cadastro/configuracaoservico/incluir", configuracaoServico)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static alterar (configuracaoServico) {
    	return Http
    			.put("cadastro/configuracaoservico/alterar", configuracaoServico)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static excluir (selectedList) {
    	return Http
    			.delete("cadastro/configuracaoservico/delete", selectedList)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }
}