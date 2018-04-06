import * as ErrorMessage from './ErrorMessage';

export const required = (text) => { 
	if (text && text.length == 0) {
		return ErrorMessage.required;
	}
	if (text) {
		return null;
	} else {
		return ErrorMessage.required;
	}
};

export const integerOnly = (text) => { 
	let regEx = /^\d+$/;

	if (text && regEx.test(text)) {
		return null;
	} else {
		return ErrorMessage.integerOnly;
	}
};


export const minLength = (length) => {
  return (text) => {
    return text.length >= length ? null : ErrorMessage.minLength(length);
  };
};