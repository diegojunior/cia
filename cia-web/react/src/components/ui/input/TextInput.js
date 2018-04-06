import React, { Component } from 'react';
import PropTypes from 'prop-types';
import update from 'immutability-helper';
import ValidatorComponent from '../../../shared/infra/ValidatorComponent';
import {required, integerOnly} from '../../../shared/util/Rules';
import {ruleRunner, run} from '../../../shared/util/RuleRunner';
import InputError from "./InputError";

export default class TextInput extends ValidatorComponent {

	constructor (props) {
		super(props);
	}

	componentWillMount () {
		var label = this.props.label;
		if (this.props.labelValidator) {
			label = this.props.labelValidator;
		}

		if (this.props.required) {
			this.state.fieldValidations = [ruleRunner("value", label, required)];
		}

		if (this.props.integerOnly) {
			this.state.fieldValidations = [ruleRunner("value", label, integerOnly)];
		}

		if (this.props.required && this.props.integerOnly) {
			this.state.fieldValidations = [ruleRunner("value", label, required, integerOnly)];
		}		
		
		this.state.validationErrors = run(this.state, this.state.fieldValidations);
		
	}

	componentDidMount() {
		
		if (!$.isEmptyObject(this.state.validationErrors) && this.props.addError) {
			this.props.addError(this.props.id, this.state.validationErrors);
		}

	}

	componentWillReceiveProps (nextProps) {

		var label = this.props.label;
		if (this.props.labelValidator) {
			label = this.props.labelValidator;
		}

		this.state.fieldValidations = [];
		if (this.props.required) {
			this.state.fieldValidations = [ruleRunner("value", label, required)];
		}

		if (this.props.integerOnly) {
			this.state.fieldValidations = [ruleRunner("value", label, integerOnly)];
		}

		if (this.props.required && this.props.integerOnly) {
			this.state.fieldValidations = [ruleRunner("value", label, required, integerOnly)];
		}		
		
		this.state.validationErrors = run(this.state, this.state.fieldValidations);

		if (nextProps.clearValue && 
			nextProps.selectedValue && 
			(!$.isEmptyObject(nextProps.selectedValue) || nextProps.selectedValue > 0)) {

			let newState = update(this.state, {value: {$set: nextProps.selectedValue}});
            newState.validationErrors = {};

			if (nextProps.addError) {
				nextProps.addError(nextProps.id, newState.validationErrors);
			}

			this.setState(newState);
		} else if (nextProps.clearValue) {
			this.clear();
		}
    }

	render() {
		var label = this.props.label && this.props.label != '' ? 
			<label className="control-label">{this.props.label}</label> :
			'';

		if (label != '') {
			label = this.props.chave ? 
				<label className="control-label">{this.props.label}&nbsp;<div className="fa fa-key" style={{color: '#c3ae09'}}></div></label> : label;
		}

		var display = {'display': this.props.show == undefined || this.props.show ? 'block' : 'none'};

		return (
			<div className={this.props.required && label != ''? "form-group required": "form-group"} style={display}>

				<div className="row">
					<div className={this.props.className}>
						{label}
						<div className={this.props.uniqueName}>
							<input 
								id={this.props.id}
								type={this.props.typeOf}
								readOnly={this.props.readOnly}
								maxLength={this.props.maxLength}
								placeholder={this.props.placeholder}
								className="form-control"
								onChange={this.handlerField()}
								onBlur={this.onBlur()}
			               		value={this.state.value} />
		
							<InputError 
								visible={this.shouldDisplayError()}
								errorMessage={this.errorFor('value')} />

						</div>
					</div>
				</div>
			</div>			
		);
	}
}

TextInput.propTypes = {
	id: PropTypes.string.isRequired,
	placeholder: PropTypes.string
}
