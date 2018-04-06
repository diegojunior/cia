import React, {Component} from 'react';
import PropTypes from 'prop-types';
import InputError from "./InputError";
import ValidatorComponent from '../../../shared/infra/ValidatorComponent';
import {constants} from '../../../shared/util/Constants';
import update from 'immutability-helper';
import {ruleRunner, run} from '../../../shared/util/RuleRunner';
import {required} from '../../../shared/util/Rules';

export default class SelectInput extends ValidatorComponent {

	constructor (props) {
		super(props);
	}

	componentWillMount() {
		var label = this.props.label;
		if (this.props.labelValidator) {
			label = this.props.labelValidator;
		}
		if (this.props.required) {
			this.state.fieldValidations = [ruleRunner("value", label, required)];
		}
		this.state.validationErrors = run(this.state, this.state.fieldValidations);
	}

	componentDidMount() {
		if (!$.isEmptyObject(this.state.validationErrors)) {
			this.props.addError(this.props.id, this.state.validationErrors);
		}
	}

	componentWillReceiveProps (nextProps) {
		var label = nextProps.label;
		if (nextProps.labelValidator) {
			label = nextProps.labelValidator;
		}
		this.state.fieldValidations = [];
		if (nextProps.required) {
			this.state.fieldValidations = [ruleRunner("value", label, required)];
		}
		this.state.validationErrors = run(this.state, this.state.fieldValidations);
		
		if (nextProps.clearValue && 
			nextProps.selectedValue && 
			nextProps.selectedValue.codigo &&
			(!$.isEmptyObject(nextProps.selectedValue.codigo) || nextProps.selectedValue.codigo > 0)) {

			let newState = update(this.state, {value: {$set: nextProps.selectedValue.codigo}});
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
				<label className="control-label">{this.props.label}&nbsp;<div className="fa fa-key" style={{color: '#c3ae09'}}></div></label>: label;
		}

		var display = {'display': this.props.show == undefined || this.props.show ? 'block' : 'none'};

		var noRow = !this.props.noRow || this.props.noRow == false ? 'row' : '';

		return (

			<div className={this.props.required && label != ''? "form-group required cia-animation-fadein": "form-group"} style={display}>
				<div className={noRow}>
					<div className={this.props.className}>
						{label}
						<div className={this.props.uniqueName}>
							<select 
								id={this.props.id}
								name={this.props.name}
								value={this.state.value}
								onChange={this.handlerField()}
								disabled={this.props.readOnly}
								className="form-control">
							
								<option value="">{this.props.placeholder}</option>
								{this.props.options.map((opt, index) => {
									var text = '';
									if (opt.label) {
										text = opt.label;
									} else if (opt.nome) {
										text = opt.nome;
									} else {
										text = opt.codigo;
									}
									return (<option 
												key={index}
												value={opt.codigo}>{text}</option>
											);
								})}
							</select>
							
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

SelectInput.propTypes = {
	id: PropTypes.string.isRequired,
	name: PropTypes.string.isRequired,
	options: PropTypes.array.isRequired,
	selectedOption: PropTypes.string,
	placeholder: PropTypes.string,
}