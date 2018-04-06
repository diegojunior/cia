import React, { Component } from 'react';
import update from 'immutability-helper';
import ValidatorComponent from '../../../../shared/infra/ValidatorComponent';
import BaseComponent from '../../../../shared/infra/BaseComponent';
import {required, integerOnly} from '../../../../shared/util/Rules';
import {ruleRunner, run} from '../../../../shared/util/RuleRunner';
import MultiplesInputsRules from './MultiplesInputsRules';
import InputError from "../InputError";
import './Style.css';

export default class BaseAddRemoveMultiplesInputs extends ValidatorComponent {
	
	constructor(props) {
		super(props);

		this.state.rows = [];

		this.addRow = this.addRow.bind(this);
		this.removeRow = this.removeRow.bind(this);
		this.initialRow = this.initialRow.bind(this);

		this.limpar = this.limpar.bind(this);
		this.isValid = this.isValid.bind(this);

		this.handlerFieldRow = this.handlerFieldRow.bind(this);
	}

	shouldDisplayError() {
        return this.props.showError && !$.isEmptyObject(this.state.validationErrors);
    }

	componentWillMount () {
		this.state.rows.push(this.createRow());

		this.state.validationErrors = run(this.state, this.getFunctionsValidators(this.state));
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

    isValid() {
    	return $.isEmptyObject(this.state.validationErrors);
    }

	getFields () {
		return this.state.rows;
	}

    adicionarFieldsValidations(rowIndex, exception){
    	
    	for(var index in this.props.fieldsConfig) {

    		var fieldConfig = this.props.fieldsConfig[index];

    		if (exception) {
    			let status = false;
    			for (let index in exception) {
    				if (exception[index] == (fieldConfig.key+rowIndex)) {
    					status = true;
    				}
    			}
    			if (status) continue;
    		}
			
			if (fieldConfig.integerOnly) {
				this.addFieldValidation(this.state, fieldConfig.key+rowIndex, 'integerOnly', 
					ruleRunner(fieldConfig.key+rowIndex, fieldConfig.label, integerOnly));
    		}

    		if (fieldConfig.required) {
    			this.addFieldValidation(this.state, fieldConfig.key+rowIndex, 'required', 
    				ruleRunner(fieldConfig.key+rowIndex, fieldConfig.label, required));
    		}   			
    	}
    }

	limpar () {
		
		this.initialRow();

		let newState = update(this.state, {value: {$set: ''}});
		
		newState.validationErrors = run(newState, this.getFunctionsValidators(newState));
		
		this.setState(newState);
	}

	handlerFieldRow	 (value, fieldConfig, index, onBlurCheck) {
        
		let newState = this.changeRowField(this.state, value, fieldConfig, index);

		newState.validationErrors = run(newState, this.getFunctionsValidators(newState));

		let newStateWithAllInputRules = this.refs.inputsRules
			.apply(newState, this.props.fieldsConfig, fieldConfig, index, onBlurCheck);

		if ($.isEmptyObject(newStateWithAllInputRules.validationErrors)) {
			this.props.valueChange(newStateWithAllInputRules.rows);
			if (this.props.checkRowDuplicity) {
				newStateWithAllInputRules = this.props.checkRowDuplicity(newStateWithAllInputRules);
			}
		}

		this.setState(newStateWithAllInputRules);
    }

	changeRowField(state, value, fieldConfig, index) {
		this.manageFieldValidator(state, fieldConfig, index, value);

		let newState;
		if (fieldConfig.type == 'select') {
			newState = update(this.state, {rows: {[index]:  {[fieldConfig.key+index]: {codigo: {$set: value}}}}});
		} else {
			newState = update(this.state, {rows: {[index]:  {[fieldConfig.key+index]: {$set: value}}}});
		}
		
		return newState;
	}

	createRow () {
		let newRow = {id: null};
		let rowIndex = this.state.rows.length;
		var exception = [];
		for (var i in this.props.fieldsConfig) {
			var fieldConfig = this.props.fieldsConfig[i];

			if (fieldConfig.setPrevValue && rowIndex > 0) {
				var lastRow = Number(rowIndex) - 1;
				for (var x in this.props.fieldsConfig) {
					var fg = this.props.fieldsConfig[x];
					if (fg.getValue) {
						newRow[fieldConfig.key+rowIndex] = Number(this.state.rows[lastRow][fg.key+lastRow]) + 1;
						exception.push(fieldConfig.key+rowIndex);
						break;
					}
				}
			} else {
				if (fieldConfig.initialValue) {
					newRow[fieldConfig.key+rowIndex] = fieldConfig.initialValue;
					if (fieldConfig.type != 'select') {
						exception.push(fieldConfig.key+rowIndex);	
					}
					
				}
			}
		}

		this.adicionarFieldsValidations(rowIndex, exception);

		return newRow;
	}

	initialRow () {
		let newRow = {id: null};
		let exception = [];				
		for (var i in this.props.fieldsConfig) {
			var fieldConfig = this.props.fieldsConfig[i];
			if (fieldConfig.initialValue) {
				if (fieldConfig.type != 'select') {
					exception.push(fieldConfig.key+0);
				}
				
			}
			newRow[fieldConfig.key+0] = fieldConfig.initialValue;
		}

		this.state.fieldValidations = [];

		this.adicionarFieldsValidations(0, exception);
		
		this.state.rows = [newRow];
	}

	addRow() {

		if (this.isCamposPreenchidos()) {

			let newRow = this.createRow();
			let newState = update(this.state, {rows: {$push: [newRow]}});

			newState.validationErrors = run(newState, this.getFunctionsValidators(newState));

			this.setState(newState);
		}
	}

	isCamposPreenchidos () {
		for (var index in this.state.rows) {
			var row = this.state.rows[index];
			for (var i in this.props.fieldsConfig) {
				var fieldConfig = this.props.fieldsConfig[i];
				
				if (fieldConfig.required && 
					(row[fieldConfig.key+index] == '' || row[fieldConfig.key+index].codigo == '')) {
						this.refs.baseComponent.infoMessage('Para Inserir Novos Campos, '+
							'é Necessário Preencher Todos Obrigatórios');
					return false;
				}
			}
		}
		return true;
	}

	removeRow() {
		
		var lastIndex = this.state.rows.length - 1;
		for (var index in this.props.fieldsConfig) {
			var fieldConfig = this.props.fieldsConfig[index];
			if(fieldConfig.required) {
				this.removeFieldValidation(this.state, fieldConfig.key+lastIndex, 'required');
			}
			if(fieldConfig.integerOnly) {
				this.removeFieldValidation(this.state, fieldConfig.key+lastIndex, 'integerOnly');	
			}
		}

		let newState = update(this.state, {rows: {$splice: [[-1, 1]]}});

		this.setState(newState);
	}

	generateHeaders() {

		var cells = this.props.fieldsConfig.map((fieldConfig, index) => {
			var id = fieldConfig.key + 'Header';
			return (
				<th id={id} key={fieldConfig.key} style={fieldConfig.styleColumnHeader}>
					<div className={fieldConfig.required ? "formRequired": ""}>
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

				return this.fieldText(fieldConfig, index);
			});
			return <tr key={index} className="odd gradeX">{fields}</tr>
		});
		return newRows;
	}

	fieldText (fieldConfig, index) {
		let readOnly = fieldConfig.readOnly ? true : this.checkReadOnlyFields();
		var id = fieldConfig.key + 'Body';
		var value = this.state.rows[index][fieldConfig.key+index] ? this.state.rows[index][fieldConfig.key+index] : '';
		return (
			<td key={fieldConfig.key+index} id={id} style={fieldConfig.styleColumn}>
				<div className="form-group">
					<input 
						id={fieldConfig.key+index}
						placeholder={fieldConfig.placeholder}
						type={fieldConfig.type}
						maxLength={fieldConfig.maxLength}
						className="form-control"
						value={value}
						onBlur={e => this.handlerFieldRow(e.target.value, fieldConfig, index, true)}
						onChange={e => this.handlerFieldRow(e.target.value, fieldConfig, index)}
						readOnly={readOnly}/>
					<InputError 
						visible={this.shouldDisplayError()}
						errorMessage={this.errorFor(fieldConfig.key+index)} />
				</div>	
			</td>);
	}

	fieldCheckbox (fieldConfig, index) {
		let readOnly = this.checkReadOnlyFields();
		var id = fieldConfig.key + 'Body';
		return (
			<td key={fieldConfig.key+index} id={id} style={fieldConfig.styleColumn}>
				<div className="form-group" style={{display: 'flex', justifyContent: 'center'}}>
					<input 
						id={fieldConfig.key+index}
						type="checkbox"
						value={this.state.rows[index][fieldConfig.key+index]}
						checked={this.state.rows[index][fieldConfig.key+index]}
						onChange={e => this.handlerFieldRow(e.target.checked, fieldConfig, index)}
						disabled={readOnly}/>
					<InputError 
						visible={this.shouldDisplayError()}
						errorMessage={this.errorFor(fieldConfig.key+index)} />
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
					<InputError 
						visible={this.shouldDisplayError()}
						errorMessage={this.errorFor(fieldConfig.key+index)} />
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
					<button type="button" className="btn btn-" aria-label="Left Align" onClick={this.addRow}>
						<span className="glyphicon glyphicon-plus"></span>
					</button>
					&nbsp;
					<button type="button" className="btn btn-" aria-label="Left Align" onClick={this.removeRow}>
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