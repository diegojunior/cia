import Http from '../http/Http';
import Agente from './Agente';
import Layout from './Layout';
import Remetente from './Remetente';

export default class Importacao {

    static getBy (dataReferencia, sistema, corretora, tipoLayout, layout) {
        return Http
        		.get("importacao/list", {data: dataReferencia, sistema: sistema, corretora: corretora, tipoLayout: tipoLayout, layout: layout})
	        		.then (response => {
						return response;
					 }).catch (error => {
						throw error;
					 });
    }

    static loadAgenteRemetenteLayouts (constantSistema, tipoLayout) {
        
        if (constantSistema == null) {
            return Http
                .loadMultipleRequest(Layout.getBy(tipoLayout))
                    .then (response => {
                        return {agentes: [], remetentes: [], layouts: response[0]};
                     }).catch (error => {
                        throw error;
                     });
        }

        return Http
                .loadMultipleRequest(Agente.getBy(constantSistema), Remetente.getBy(constantSistema), Layout.getBy(tipoLayout))
                    .then (response => {
                        return {agentes: response[0], remetentes: response[1], layouts: response[2]};
                     }).catch (error => {
                        throw error;
                     });
    }

    static loadAgenteRemetente (constantSistema) {
        return Http
                .loadMultipleRequest(Agente.getBy(constantSistema), Remetente.getBy(constantSistema))
                    .then (response => {
                        return {agentes: response[0], remetentes: response[1]};
                     }).catch (error => {
                        throw error;
                     });
    }

    static incluir (importacao) {
        return Http
                .post('importacao/incluir', importacao)
                .then (response => {
                    return response;
                }).catch (error => {
                    throw error;
                });
    }

    static incluirArquivo (arquivo, id) {
    	
    	return Http.postFile("importacao/incluirArquivo", arquivo, id)
    				.then (response => {
						return response;
					}).catch (error => {
						throw error;
					});
    }
    
}