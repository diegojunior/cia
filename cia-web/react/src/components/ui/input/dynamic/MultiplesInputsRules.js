import React, { Component } from 'react';
import update from 'immutability-helper';
import ValidatorComponent from '../../../../shared/infra/ValidatorComponent';
import {required, integerOnly} from '../../../../shared/util/Rules';
import {ruleRunner, run} from '../../../../shared/util/RuleRunner';

export default class MultiplesInputsRules extends ValidatorComponent {
	
	constructor(props) {
		super(props);
	}

	apply (state, fieldsConfig, fieldConfig, rowIndex, onBlurCheck) {
		let newState = state;
		if (fieldConfig.subtractWithNextAndSetFollowing) {
			newState = this.subtractWithNextAndSetFollowing(newState, fieldsConfig, fieldConfig, rowIndex);
		} 
		if (fieldConfig.subtractWithPrevAndSetFollowing) {
			newState = this.subtractWithPrevAndSetFollowing(newState, fieldsConfig, fieldConfig, rowIndex);
		} 
		if (fieldConfig.isBiggerThanBefore && onBlurCheck) {
			newState = this.isBiggerThanBefore(newState, fieldsConfig, fieldConfig, rowIndex);
		}
		if (fieldConfig.fieldReadOnly) {
			newState = this.setReadOnly(newState, fieldsConfig, fieldConfig, rowIndex);
		}
		
		return newState;
	}

	subtractWithNextAndSetFollowing (state, fieldsConfig, fieldConfig, rowIndex) {

		for (var i in fieldsConfig) {
			if (fieldsConfig[i].key == fieldConfig.key) {
				var nextIndex = 1 + Number(i);
				var a = state.rows[rowIndex][fieldConfig.key+rowIndex];
				var b = state.rows[rowIndex][fieldsConfig[nextIndex].key+rowIndex];

				var result = '';

				if (b && b > 0 && a && a > 0) {
					result = b - a + 1;
				} 

				var targetIndex = Number(i)+2;
				let newState = this.changeRowField(state, result, fieldsConfig[targetIndex], rowIndex);

				newState.validationErrors = run(newState, this.getFunctionsValidators(newState));

				return newState;
			}
		}

		return state;
		
	}

	subtractWithPrevAndSetFollowing (state, fieldsConfig, fieldConfig, rowIndex) {

		for (var i in fieldsConfig) {
			if (fieldsConfig[i].key == fieldConfig.key) {
				var prevIndex = Number(i) - 1;
				var a = state.rows[rowIndex][fieldConfig.key+rowIndex];
				var b = state.rows[rowIndex][fieldsConfig[prevIndex].key+rowIndex];

				var result = '';

				if (b && b > 0 && a && a > 0) {
					result = a - b + 1;
					if (result <= 0) {
						result = '';
					}
				}
				
				var targetIndex = Number(i)+1;
				let newState = this.changeRowField (state, result, fieldsConfig[targetIndex], rowIndex);

				newState.validationErrors = run(newState, this.getFunctionsValidators(newState));

				return newState;
			}
		}
		
		return state;
	}

	isBiggerThanBefore (state, fieldsConfig, fieldConfig, rowIndex) {

		for (var i in fieldsConfig) {
			if (fieldsConfig[i].key == fieldConfig.key) {
				var prevIndex = Number(i) - 1;
				var a = state.rows[rowIndex][fieldConfig.key+rowIndex];
				var b = state.rows[rowIndex][fieldsConfig[prevIndex].key+rowIndex];

				var result = '';

				if (b && b > 0 && a && a > 0) {
					result = a - b;
					if (result < 0) {
						result = '';
					} else {
						result = a;
					}
				}
				
				var targetIndex = Number(i);
				let newState = this.changeRowField (state, result, fieldsConfig[targetIndex], rowIndex);

				newState.validationErrors = run(newState, this.getFunctionsValidators(newState));

				return newState;
			}
		}
		
		return state;
	}

	setReadOnly (state, fieldsConfig, fieldConfig, rowIndex) {

		for (var i in fieldsConfig) {
			if (fieldsConfig[i].key == fieldConfig.fieldReadOnly) {
				var codigo = state.rows[rowIndex][fieldConfig.key+rowIndex].codigo;
				var option = this.getOptionBy(fieldConfig, rowIndex, codigo);
				var readOnly = option.tipo.codigo == 'ALF';
				
				let newState = this.changeFieldReadOnly(state, fieldsConfig[i], rowIndex, readOnly);
				return newState;
			}
		}
		return state;
	}

	changeRowField(state, value, fieldConfig, index) {
		this.manageFieldValidator(state, fieldConfig, index, value);

		let newState;
		if (fieldConfig.type == 'select') {
			newState = update(state, {rows: {[index]:  {[fieldConfig.key+index]: {codigo: {$set: value}}}}});
		} else {
			newState = update(state, {rows: {[index]:  {[fieldConfig.key+index]: {$set: value}}}});
		}
		
		return newState;
	}

	changeFieldReadOnly(state, fieldConfig, index, readOnly) {
		if (fieldConfig.type == 'text') {
			var val = '';
			if (!readOnly) {
				val = state.rows[index][fieldConfig.key+index] ? state.rows[index][fieldConfig.key+index].value : '';
			}
			var newState = update(state, {rows: {[index]:  {[fieldConfig.key+index]: {$set: {value: val, readOnly: readOnly}}}}});
			return newState;
		}
		return state;
	}

	getOptionBy(fieldConfig, index, codigo) {
		var options = fieldConfig.options(index);
		for (var i in options) {
			var opt = options[i];
			if (opt.codigo == codigo) {
				return opt;
			}
		}
		return null;
	}

	render() {
	
		return (<div></div>);
	}
}