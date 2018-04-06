import React, { Component } from 'react';
import update from 'immutability-helper';
import {ruleRunner, run} from '../util/RuleRunner';
import {required, integerOnly} from '../util/Rules';

export default class ValidatorComponent extends Component {

	constructor(props) {
	    super(props);
        this.state = {
        	fieldValidations: [],
            validationErrors: [],
            value: '', 
            defaultValue: '',
            showError: false
        };
        
        this.errorFor = this.errorFor.bind(this);

        this.setDefaultValue = this.setDefaultValue.bind(this);

        this.handlerField = this.handlerField.bind(this);
        this.onBlur = this.onBlur.bind(this);
        this.clear = this.clear.bind(this);
	}

    errorFor (field) {
		return this.state.validationErrors[field];
	}

    manageFieldValidator (state, fieldConfig, index, value) {
		let key = fieldConfig.key+index;
        let label = fieldConfig.label;
        if (value.length > 0 
            || value > 0
            || (!value && !$.isEmptyObject(value))) {
			this.removeFieldValidation(state, key, 'required');
			
			let regEx = /^\d+$/;
			if (regEx.test(value)) {
				 this.removeFieldValidation(state, key, 'integerOnly');
			}

		} else {
            if (this.isRequiredConfigured(fieldConfig)) {
                if (!this.containsFieldValidation(state, key, 'required')) {
                    this.addFieldValidation(state, key, 'required', ruleRunner(key, label, required));
                }
            }

            if (this.integerOnlyConfigured(fieldConfig)) {
                if (!this.containsFieldValidation(state, key, 'integerOnly')) {
                    this.addFieldValidation(state, key, 'integerOnly', ruleRunner(key, label, integerOnly));  
                }
            }
			
		}
	}

    addFieldValidation(state, key, validator, functionValidator) {

        var fieldValidator = this.createFieldValidation(key, validator, functionValidator);

        state.fieldValidations.push(fieldValidator);
    }

    createFieldValidation(key, validator, functionValidator) {
        return {field: key, validator: validator, functionValidator: functionValidator};
    }

    isRequiredConfigured (fieldConfig) {
        if (fieldConfig.required) {
            return true;
        } 
        return false;
    }

    integerOnlyConfigured (fieldConfig) {
        if (fieldConfig.integerOnly) {
            return true;
        } 
        return false;
    }

    containsFieldValidation(state, key, validator) {
        for (var i in state.fieldValidations) {
            if (state.fieldValidations[i].field == key && 
                state.fieldValidations[i].validator == validator) {
                return true;
            }
        }
        return false;
    }

    removeFieldValidation(state, key, validator) {
        for (var i in state.fieldValidations) {
            if (state.fieldValidations[i].field == key && 
                state.fieldValidations[i].validator == validator) {
                state.fieldValidations.splice(i, 1);
            }
        }
    }

    removeValidationErrors(state, key) {
        for (var field in state.validationErrors) {
            if (field == key) {
                delete state.validationErrors[field];
            }
        }
    }

    getFunctionsValidators(state){
        return state.fieldValidations.map((fieldValidator) => {
            return fieldValidator.functionValidator;
        });
    }

    shouldDisplayError() {
        return this.props.showError;
    }

    handlerField () {
        return (e) => {

            e.preventDefault();
            let value = this.formatValue(e.target.value);
            let newState = update(this.state, {value: {$set: value}});
            newState.validationErrors = run(newState, this.state.fieldValidations);
            this.changeParent(value);
            
            if (this.props.addError) {
                this.props.addError(this.props.id, newState.validationErrors);    
            }
            this.setState(newState);
        }
    }

    onBlur () {
        return (e) => {
            e.preventDefault();
            let value = this.removeWhitespaces(e.target.value);
            let newState = update(this.state, {value: {$set: value}});
            this.changeParent(value);
            if (this.props.addError) {
                this.props.addError(this.props.id, newState.validationErrors);    
            }
            this.setState(newState);
        }
    }

    removeWhitespaces (value) {
        return value.trim();
    }

    formatValue (value) {
        if (this.props.formatValue) {
            return this.props.formatValue(value)
        }
        return value;
    }

    changeParent (value) {
        if (this.props.valueChange) {
            this.props.valueChange(value);
        }
    }

    clear () {
        if (this.state.value != '') {
            var value = '';
            if (this.state.defaultValue != null) {
                value = this.state.defaultValue;
            }

            let newState = update(this.state, {value: {$set: value}});
            newState.validationErrors = run(newState, this.state.fieldValidations);

            if (!$.isEmptyObject(newState.validationErrors) && this.props.addError) {
                this.props.addError(this.props.id, newState.validationErrors);
            }
        
            this.setState(newState);
        }
    }

    forceClear () {
        var value = '';
        if (this.state.defaultValue != null) {
            value = this.state.defaultValue;
        }
        let newState = update(this.state, {value: {$set: value}});
        newState.validationErrors = run(newState, this.state.fieldValidations);

        if (!$.isEmptyObject(newState.validationErrors) && this.props.addError) {
            this.props.addError(this.props.id, newState.validationErrors);
        }

        this.setState(newState);
    }

    setDefaultValue (value) {
        this.state.defaultValue = value;
		if (this.state.value == null || this.state.value == '') {
            let newState = update(this.state, {value: {$set: value}});
            newState.validationErrors = {};
            if (this.props.addError) {
                this.props.addError(this.props.id, newState.validationErrors);
            }
			this.setState(newState);
		}
	}
}