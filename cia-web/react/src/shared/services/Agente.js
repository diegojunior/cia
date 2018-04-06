import Http from '../http/Http';

export default class Agente {

    static getBy (sistema) {
        return Http.get("gateway/corretora/all", {sistema: sistema})
	        		.then (response => {
						return response;
					 }).catch (error => {
						throw error;
					 });
    }
}