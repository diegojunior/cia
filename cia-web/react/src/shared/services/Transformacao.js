import Http from '../http/Http';
import RemetenteService from './Remetente';
import TipoEquivalenciaService from './TipoEquivalencia';


export default class TransformacaoService {

    static getBy(tipoLayout, layout, sessao, campo, tipoTransformacao) {
    	return Http
	    		.get("parametrizacao/transformacao/search", 
					{tipoLayout: tipoLayout, 
					 layout: layout, 
					 sessao: sessao, 
					 campo: campo,
					 tipoTransformacao: tipoTransformacao})
	    			.then(response => {
	    				return response;
	    			}).catch (error => {
	    				throw error;
	    			});
    }

    static incluir (transformacao) {
    	return Http
    			.post("parametrizacao/transformacao/incluir", transformacao)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

    static excluir (selectedList) {
    	return Http
    			.delete("parametrizacao/transformacao/delete", selectedList)
					.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }

	static loadRemetenteAndTiposEquivalencias (sistema) {
		return Http
                .loadMultipleRequest(RemetenteService.getBy(sistema), TipoEquivalenciaService.getBy(sistema))
                    .then (response => {
                        return {remetentes: response[0], tiposEquivalencias: response[1]};
                     }).catch (error => {
						 throw "Nao foi possivel carregar os Remetentes e os Tipos de Equivalência";
                     });
	}
}