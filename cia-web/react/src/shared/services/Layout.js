import Http from '../http/Http';

export default class Layout {

	static getBy (tipoLayout) {

		var url = "parametrizacao/layout/listBy/{" + tipoLayout + "}";

		return Http.get(url, {tipoLayout: tipoLayout})
			.then(data => {
				return data;
			}).catch (error => {
				throw error;
			});
	}
}