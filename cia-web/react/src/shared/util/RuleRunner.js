export const ruleRunner = (field, name, ...validations) => {
	return (state) => {

		for (let validador of validations) {
			let fields = field.split('.');
			let expressao = 'state[';
			for (let i = 0; i < fields.length; i++) {
				if (i == 0) {
					expressao = expressao + '\'' + fields[i] + '\']';
				} else {
					expressao = expressao + '[\'' + fields[i] + '\']';
				}
			}
			let errorMessageFunc = validador(eval(expressao), field);
			if (errorMessageFunc) {
				return {[field]: errorMessageFunc(name)};
			}
		}
		return null;
	};
};

export const run = (state, runners) => {
	return runners.reduce((memoria, runner) => {
		return Object.assign(memoria, runner(state));
	}, {});
};