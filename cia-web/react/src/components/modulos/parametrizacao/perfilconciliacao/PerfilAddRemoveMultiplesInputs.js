import React, { Component } from 'react';
import update from 'immutability-helper';
import ValidatorComponent from '../../../../shared/infra/ValidatorComponent';
import BaseComponent from '../../../../shared/infra/BaseComponent';
import {required, integerOnly} from '../../../../shared/util/Rules';
import {ruleRunner, run} from '../../../../shared/util/RuleRunner';
import MultiplesInputsRules from './PerfilMultiplesInputsRules';
import InputError from "../../../ui/input/InputError";
import './Style.css';

export default class PerfilAddRemoveMultiplesInputs extends ValidatorComponent {
	
	constructor(props) {
		super(props);
		this.state.rows = [];
		
		this.addRow = this.addRow.bind(this);
		this.removeLocalRow = this.removeLocalRow.bind(this);
		this.initialRow = this.initialRow.bind(this);
		this.limpar = this.limpar.bind(this);
		this.handlerFieldRow = this.handlerFieldRow.bind(this);
	}

	componentWillMount () {
		this.state.rows.push(this.createRow());
	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.selectedValue && !$.isEmptyObject(nextProps.selectedValue)) {
			var rows = [];
			for (var index in nextProps.selectedValue) {
				var element = nextProps.selectedValue[index];
				let newRow = {id: null};
				for (var i in this.props.fieldsConfig) {
					var fieldConfig = this.props.fieldsConfig[i];
					let value = element[fieldConfig.key];
					newRow[fieldConfig.key+index] =  value == null ? '': value;
				}

				rows.push(newRow);				
			}
			this.setState({rows: rows, fieldValidations: [], validationErrors: {}});
		} else if (nextProps.clearValue) {
		 	this.limpar();
		}
	}

	createRow () {
		let newRow = {id: null};
		let rowIndex = this.state.rows.length ? this.state.rows.length : 0;
		for (var i in this.props.fieldsConfig) {
			var fieldConfig = this.props.fieldsConfig[i];
			if (fieldConfig.initialValue) {
				newRow[fieldConfig.key+rowIndex] = fieldConfig.initialValue;
			}
		}
		return newRow;
	}

	limpar () {
		this.initialRow();
		let newState = update(this.state, {value: {$set: ''}});
		this.setState(newState);
	}

	initialRow () {
		let newRow = {id: null};			
		for (var i in this.props.fieldsConfig) {
			var fieldConfig = this.props.fieldsConfig[i];
			newRow[fieldConfig.key+0] = fieldConfig.initialValue;
		}

		this.state.rows = [newRow];
	}

	handlerFields (value, field, rowIndex) {
        let newState = this.changeRowField(this.state, value, field, rowIndex);
		let key = field.key+rowIndex;
		this.props.valueChange(newState.rows);
		this.setState(newState);
    }

	handlerFieldRow	 (value, fieldConfig, index, onBlurCheck) {
        let newState = this.changeRowField(this.state, value, fieldConfig, index);
		let newStateWithAllInputRules = this.refs.inputsRules
			.apply(newState, this.props.fieldsConfig, fieldConfig, index, onBlurCheck);
		this.setState(newStateWithAllInputRules);
    }

	changeRowField(state, value, fieldConfig, index) {
		let newState;
		if (fieldConfig.type == 'select') {
			newState = update(this.state, {rows: {[index]:  {[fieldConfig.key+index]: {codigo: {$set: value}}}}});
		} else {
			newState = update(this.state, {rows: {[index]:  {[fieldConfig.key+index]: {$set: value}}}});
		}
		
		return newState;
	}

	addRow() {
		let newRow = this.createRow();
		let newState = update(this.state, {rows: {$push: [newRow]}});
		this.setState(newState);
	}

	removeLocalRow() {
		var lastIndex = this.state.rows.length ? this.state.rows.length - 1 : 0;
		let newState = update(this.state, {rows: {$splice: [[-1, 1]]}});
		this.setState(newState);
	}

	getFields () {
		return this.state.rows;
	}

	generateHeaders() {

		var cells = this.props.fieldsConfig.map((fieldConfig, index) => {
			var id = fieldConfig.key + 'Header';
			return (
				<th id={id} key={fieldConfig.key} style={fieldConfig.styleColumnHeader}>
					<div>
						{fieldConfig.chave ? 
						<label className="control-label">{fieldConfig.label}&nbsp;<div className="fa fa-key" style={{color: '#c3ae09'}}></div></label> :
						<label className="control-label">{fieldConfig.label}</label>}
					</div>
				</th>);
		});
		return <tr>{cells}</tr>;
	}

	generateRows() {
		var newRows = this.state.rows.map((item, index) => {
			
			var fields = this.props.fieldsConfig.map((fieldConfig) => {
				if (fieldConfig.type == 'select') {
					return this.fieldSelect(fieldConfig, index);
				}

				if (fieldConfig.type == 'checkbox') {
					return this.fieldCheckbox(fieldConfig, index);
				}
			});
			return <tr key={index} className="odd gradeX">{fields}</tr>
		});
		return newRows;
	}

	fieldCheckbox (fieldConfig, index) {
		let readOnly = this.checkReadOnlyFields();
		var id = fieldConfig.key + 'Body';
		var value = this.state.rows[index][fieldConfig.key+index] ? this.state.rows[index][fieldConfig.key+index] : false;
		return (
			<td key={fieldConfig.key+index} id={id} style={fieldConfig.styleColumn}>
				<div className="form-group" style={{display: 'flex', justifyContent: 'center'}}>
					<input 
						id={fieldConfig.key+index}
						type="checkbox"
						value={value}
						checked={value}
						onChange={e => this.handlerFieldRow(e.target.checked, fieldConfig, index)}
						disabled={readOnly}/>
				</div>	
			</td>);
	}

	fieldSelect (fieldConfig, index) {
		let readOnly = this.checkReadOnlyFields();
		let options = fieldConfig.options(index);
		var id = fieldConfig.key + 'Body';
		return (
			
			<td key={fieldConfig.key+index} id={id} style={fieldConfig.styleColumn}>
				<div className="form-group">
					<select 
						id={fieldConfig.key+index} 
						name={fieldConfig.key+index}
						value={this.state.rows[index][fieldConfig.key+index].codigo}
						disabled={readOnly}
						onChange={e => this.handlerFieldRow(e.target.value, fieldConfig, index)}
						className="form-control">
						<option value="">{fieldConfig.placeholder}</option>
						{options.map((opt, index) => {
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
				</div>
			</td>);
				
	}

	checkReadOnlyFields () {
		//TODO VERIFICAR FORMA GENERICA PARA SETAR READONLY NOS FIELDS.
		if (this.props.readOnly != undefined) {
			if (this.props.readOnly) {
				return true;
			}
			return false;
		} 
		if (this.props.selectedValue && !$.isEmptyObject(this.props.selectedValue)) {
			for (var index in this.props.selectedValue) {
				var element = this.props.selectedValue[index];
				if (element.id != null) {
					return true;
				}
			}
		}
		return false;
	}

	render() {
		var headerComponents = this.generateHeaders();
		var rowComponents = this.generateRows();

		var className = this.props.className ? this.props.className : "control-group default-height";

		var headerClassName = this.props.headerClassName ? this.props.headerClassName : "control-group";

		var display = {'display': this.props.show == undefined || this.props.show ? '' : 'none'};

		var displayPlusMinus = {'display': this.props.displayPlusMinus == undefined || this.props.displayPlusMinus ? '' : 'none'};
	
		return (
			<div style={display} className="cia-animation-fadein">
				
				<label className="control-label">{this.props.label}</label>

				<div style={displayPlusMinus}>
					<button id="btnPlus" type="button" className="btn btn-" aria-label="Left Align" onClick={this.addRow}>
						<span className="glyphicon glyphicon-plus"></span>
					</button>
					&nbsp;
					<button id="btnMinus" type="button" className="btn btn-" aria-label="Left Align" onClick={this.removeLocalRow}>
						<span className="glyphicon glyphicon-minus"></span>
					</button>
				</div>
				
				<div className={headerClassName}>
					<table style={{'margin': '0', 'padding': '0'}} className="table table-striped table-hover table-bordered">
						<thead>{headerComponents}</thead>
					</table>
				</div>

				<div className={className} style={{'maxHeight': '500px', 'overflowY': 'scroll'}}>
					<table className="table table-striped table-hover table-bordered">
						<tbody>{rowComponents}</tbody>
					</table>
				</div>
				
				<MultiplesInputsRules ref="inputsRules" />
				<BaseComponent ref="baseComponent" />
			</div>
		)
	}
}