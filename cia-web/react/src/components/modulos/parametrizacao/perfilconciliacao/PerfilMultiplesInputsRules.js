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
		if (fieldConfig.uncheckInputs) {
			newState = this.uncheckInputs(newState, fieldsConfig, fieldConfig, rowIndex);
		} 
		
		return newState;
	}

	uncheckInputs (state, fieldsConfig, fieldConfig, rowIndex) {
		let fieldsToChangeCheck = [];
		for (var j in fieldConfig.uncheckInputs) {
			let keyFieldToUncheck = fieldConfig.uncheckInputs[j];
			for (var i in fieldsConfig) {
				let fieldKey = fieldsConfig[i].key;
				if (fieldKey === keyFieldToUncheck) {
					fieldsToChangeCheck.push(fieldsConfig[i]);
				}
			}

		}

		return this.changeRowField(state, false, fieldsToChangeCheck, rowIndex);
		
	}

	changeRowField(state, value, fieldsToChangeCheck, index) {
		let newState = state;
		let newRows = newState.rows;
		for (var x in fieldsToChangeCheck) {
			var field = fieldsToChangeCheck[x].key+index;
			newRows[index][field] = value;
		}
		return update(newState, {rows: {$set: newRows}});
	}

	render() {
	
		return (<div></div>);
	}
}