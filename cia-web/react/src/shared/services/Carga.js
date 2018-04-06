import Http from '../http/Http';
import ConfiguracaoServicoService from './ConfiguracaoServico';
import ClienteService from './Cliente';
import GrupoService from './Grupo';

export default class Carga {

	static executar (carga) {
		
		var url = "carga/executar";

    	return Http
    			.post(url, carga)
				.then (response => {
					return response;
				}).catch (error => {
					throw error;
				});
    }

	static search (data, sistema, status) {
		
		var url = "carga/search";
		
		return Http
				.get(url, {data: data, sistema: sistema, status: status})
				.then(response => {
					return response;
				}).catch (error => {
					throw error;
				});
	}

	static loadServicosAndClientesAndGrupos (sistema, codigo) {
		return Http
				.loadMultipleRequest(ConfiguracaoServicoService.getBy(sistema), 
					ClienteService.getBy(sistema, codigo, 0, 100),
					GrupoService.getBy(sistema, codigo, 0, 100))
				.then (response => {
					return {servicos: response[0], clientes: response[1], grupos: response[2]};
				}).catch (error => {
					throw "Nao foi possivel carregar os Servicos e Clientes/Grupos";
				});
	}
}