
import AxiosInterceptor from './AxiosInterceptor';

//const baseURL = "http://localhost:8080/cia/api/v1/";
const baseURL = "/cia/api/v1/";

export default class Http {
	
	static get(url, params) {
        return AxiosInterceptor
        		.get(baseURL + url, {
	       			method: 'get',
					headers: {'Content-Type': 'application/json'},
					params: params
        		}).then(response => {
					return response.data;
				}).catch (error => {
					console.log('Erro: ', error.response);
					throw Http.checkErrorCode(error.response);
				});
    }

	static post(url, data) {
		return AxiosInterceptor
				.post(baseURL + url, data, {
					method: 'post',
					headers: {'Content-Type': 'application/json'}
				}).then(response => {
					return response.data;
				}).catch(error => {
					console.log('Erro: ', error.response);
					throw Http.checkErrorCode(error.response);
				});
	}

	static put(url, data) {
		return AxiosInterceptor
				.put(baseURL + url, data, {
					method: 'put',
					headers: {'Content-Type': 'application/json'}
				}).then(response => {
					return response.data;
				}).catch(error => {
					console.log('Erro: ', error.response);
					throw Http.checkErrorCode(error.response);
				});
	}

	static delete(url, data) {
		return AxiosInterceptor
				.delete(baseURL + url, {
					method: 'delete',
					headers: {'Content-Type': 'application/json'},				
					data: data
				}).then(response => {
					return response.data;
				}).catch(error => {
					console.log('Erro: ', error.response);
					throw Http.checkErrorCode(error.response);
				});
	}

	static postFile(url, file, id) {
		var data = new FormData();
        data.append('file', file);
        data.append('id', id)
		return AxiosInterceptor
				.post(baseURL + url, data, {
					method: 'POST',
					headers: {'Content-Type': ['multipart/form-data', 'application/json']}
				}).then(response => {
					return response.data;
				}).catch(error => {
					console.log('Erro: ', error.response);
					throw Http.checkErrorCode(error.response);
				});
	}

	static loadMultipleRequest (...requests) {
		return AxiosInterceptor
				.all(requests)
				.then (AxiosInterceptor.spread(function (...results) {
					return results;
				}))
				.catch (error => {
					console.log('Erro Axios All: ', error);
					throw error;
				});
	}

	static checkErrorCode (response) {
		if (response.status >= 400 && response.status < 500) {
			return response.data.stack.message;
		}
		return 'Ocorreu um erro interno, favor entre em contato com o Administrador do Sistema!';
	}
}