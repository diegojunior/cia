import React, { Component } from 'react';
import PropTypes from 'prop-types';
import update from 'immutability-helper';
import { ButtonGroup, Button } from 'react-bootstrap';
import ValidatorComponent from '../../../shared/infra/ValidatorComponent';
import {required} from '../../../shared/util/Rules';
import {ruleRunner, run} from '../../../shared/util/RuleRunner';
import InputError from "./InputError";

export default class RadioInput extends ValidatorComponent {

	constructor (props) {
		super(props);
		this.state.selecionado = 0;

		this.onChange = this.onChange.bind(this);
	}

	onChange(value) {
		return (event) => {
			event.preventDefault();
			if (this.props.onChange) {
				this.props.onChange(value);
			}
			this.setState({selecionado: value});
		}
	}

	selecionar (value) {
		this.setState({selecionado: value});
	}

	generateToggleButton () {
		var toggleButtons = this.props.options.map((opt, index) => {
			var text = '';
			var chave = opt.chave ? <div className="fa fa-key" style={{color: '#c3ae09'}}></div> : '';
			if (opt.label) {
				text = opt.label;
			} else if (opt.nome) {
				text = opt.nome;
			} else {
				text = opt.codigo;
			}
			return (<Button id={this.props.id+index} 
						disabled={this.props.readOnly}
						active={this.state.selecionado == index} 
						onClick={this.onChange(index)}
						key={index} 
						value={index}>
						<b>{text}&nbsp;{chave}</b>
					</Button>);
		});
		return (<ButtonGroup>
					{toggleButtons}
				</ButtonGroup>);
	}

	render() {
		var display = {'display': this.props.show == undefined || this.props.show ? 'initial' : 'none'};

		var radio = this.generateToggleButton();

		var required = this.props.required ? 
			<span style={{'color' : 'red', 'fontSize' : '14px', 'fontFamily' : 'FontAwesome', 'fontWeight' : 'normal'}}>*</span> : '';

		return (
			<div className={"form-group"} style={display}>
				<div className="row">
					<div className={this.props.className}>
						{radio} {required}
					</div>
					<InputError 
						visible={this.shouldDisplayError()}
						errorMessage={this.errorFor('value')} />
				</div>
			</div>
		);
	}
}