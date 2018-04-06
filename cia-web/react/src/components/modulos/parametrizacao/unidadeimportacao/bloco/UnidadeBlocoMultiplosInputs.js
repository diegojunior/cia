import ValidatorComponent from "../../../../../shared/infra/ValidatorComponent";
import React, { Component } from 'react';
import update from 'immutability-helper';
import BaseComponent from '../../../../../shared/infra/BaseComponent';
import {required, integerOnly} from '../../../../../shared/util/Rules';
import {ruleRunner, run} from '../../../../../shared/util/RuleRunner';
import MultiplesInputsRules from '../../../../ui/input/dynamic/MultiplesInputsRules';
import InputError from "../../../../ui/input/InputError";

export default class UnidadeBlocoMultiplosInputs extends ValidatorComponent {

    constructor (props) {
        super(props);
        this.state.rows = [];
		
		this.addRow = this.addRow.bind(this);
		this.removeRow = this.removeRow.bind(this);
		this.limpar = this.limpar.bind(this);

		this.handlerFields = this.handlerFields.bind(this);
    }

    //Na inicialição do component deve-se criar uma linha vazia com os fields validators
	componentWillMount () {
		this.state.rows.push(this.createRow());
		this.state.validationErrors = run(this.state, this.getFunctionsValidators(this.state));
	}

	//Após a criação da linha deve-se adicionar os erros de validação no FormValidator
	componentDidMount() {
		let index = this.state.rows.length-1;
		if (!$.isEmptyObject(this.state.validationErrors) && this.props.addError) {
			for (let item in this.props.fields) {
				this.props.addError(this.props.fields[item].key+index, this.state.validationErrors);
			}
		}
    }
    
    componentWillReceiveProps (nextProps) {
		if (nextProps.selectedValue && !$.isEmptyObject(nextProps.selectedValue)) {
			var rows = [];
			for (var index in nextProps.selectedValue) {
				var element = nextProps.selectedValue[index];
				let newRow = {id: null};
				for (var i in this.props.fields) {
					var key = this.props.fields[i].key;
					let value = element[key];
					newRow[key+index] =  value == null ? '': value;
				}

				rows.push(newRow);				
			}

			this.setState({rows: rows, fieldValidations: [], validationErrors: {}});
		} else if (nextProps.clearValue) {
			this.limpar();
		}
    }
	
	//cria uma nova linha no component e cria os respectivos fields validators
    createRow () {
		let newRow = {id: null};
		let rowIndex = this.state.rows.length;
		for (var i in this.props.fields) {
			var fieldConfig = this.props.fields[i];
			newRow[fieldConfig.key+rowIndex] = {id: null, codigo: ''};
		}
		this.adicionarFieldsValidations(rowIndex);
		return newRow;
	}

	// Adiciona os fields validators de Required
	adicionarFieldsValidations(rowIndex) {
		for (let item in this.props.fields) {
			let key = this.props.fields[item].key + rowIndex;
			let label = this.props.fields[item].label;
			this.addFieldValidation(this.state, key, 'required', ruleRunner(key, label, required));	
		}    	
    }

    limpar () {

    	this.initialRow();
		let newState = update(this.state, {value: {$set: ''}});
		newState.validationErrors = run(newState, this.getFunctionsValidators(newState));
		this.setState(newState);
	}
	
	initialRow () {
		let newRow = {id: null};		
		for (var i in this.props.fields) {
			var key = this.props.fields[i].key;
			newRow[key+0] = {id: null, codigo: ''};
		}

		this.state.fieldValidations = [];

		this.adicionarFieldsValidations(0);
		
		this.state.rows = [newRow];
	}

    clearAllErrors () {
    	for (let rowId in this.state.rows) {
    		for (let i in this.props.fields) {
    			let key = this.props.fields[i].key + rowId;
    			this.props.removeError(key);
    		}
    	}
    }

    handlerFields (value, field, rowIndex) {
        
		let newState = this.changeRowField(this.state, value, field, rowIndex);

		let key = field.key+rowIndex;

		newState.validationErrors = run(newState, this.getFunctionsValidators(newState));

		if ($.isEmptyObject(newState.validationErrors)) {
			this.props.valueChange(newState.rows);			
        }
        
        newState = this.props.checkRowDuplicity(newState)
		
		this.props.removeError(key);

		if (this.validationErrorContainKey(key, newState.validationErrors)) {
			this.props.addError(key, newState.validationErrors)
		}

		this.setState(newState);
    }

    validationErrorContainKey (key, validationErrors) {
    	for (var keyValidation in validationErrors) {
    		if (keyValidation == key) {
    			return true;
    		}
    	}
    	return false;
    }

	changeRowField(state, value, field, index) {
		this.manageFieldValidator(state, field, index, value);
		let newState = update(state, {rows: {[index]:  {[field.key+index]: {codigo: {$set: value}}}}});
		return newState;
	}

	//Ação do usuário para incluir uma nova linha, nessa nova linha deve-se seguir a infra de 
	// popular os fields validators e também adicionar os errors iniciais no FormValidator.
	//Deve-se também se necessário validar o preenchimento completo da ultima linha antes de
	//adicionar a nova linha.
	addRow() {
		if (this.isCamposPreenchidos()) {

			let newRow = this.createRow();
			let newState = update(this.state, {rows: {$push: [newRow]}});

			newState.validationErrors = run(newState, this.getFunctionsValidators(newState));

			let index = newState.rows.length-1;
			if (!$.isEmptyObject(newState.validationErrors) && this.props.addError) {
				for (let item in this.state.fields) {
					this.props.addError(this.state.fields[item].key+index, newState.validationErrors);
				}
			}

			this.setState(newState);
		}
	}

	isCamposPreenchidos () {
		for (var index in this.state.rows) {
			var row = this.state.rows[index];
			for (var i in this.props.fields) {
				var fieldConfig = this.props.fields[i];
				
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

    //Ação do usuário para remover a ultima linha, deve-se retirar os erros field Validator e do FormValidator
	removeRow() {
		var lastIndex = this.state.rows.length - 1;
		for (var index in this.props.fields) {
			var fieldConfig = this.props.fields[index];
			this.removeFieldValidation(this.state, fieldConfig.key+lastIndex, 'required');
			this.props.removeError(fieldConfig.key+lastIndex);
		}

		let newState = update(this.state, {rows: {$splice: [[-1, 1]]}});

		this.setState(newState);
	}

	removeRowToState(stateParam) {
		
		var lastIndex = stateParam.rows.length - 1;
		for (var index in this.props.fields) {
			var fieldConfig = this.props.fields[index];
			if(fieldConfig.required) {
				this.removeFieldValidation(stateParam, fieldConfig.key+lastIndex, 'required');
			}
		}

		let newState = update(stateParam, {rows: {$splice: [[-1, 1]]}});

		return newState;
	}

    getFields () {
		return this.state.rows;
	}

    fieldSelect (field, index) {
		let readOnly = this.checkReadOnlyFields();
		let options = field.options(index);
		var id = field.key + 'Body';
		return (
			
			<td key={field.key+index} id={id} style={{width: '50%'}}>
				<div className="form-group">
					<select 
						id={field.key+index} 
						name={field.key+index}
						value={this.state.rows[index][field.key+index].codigo}
						disabled={readOnly}
						onChange={e => this.handlerFields(e.target.value, field, index)}
						className="form-control">
						<option value="">{field.placeholder}</option>
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
						errorMessage={this.errorFor(field.key+index)} />
				</div>
			</td>);
				
    }
    
    checkReadOnlyFields () {
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

    headers() {
		var headers = this.props.fields.map((field) => {
			var key = field.key;
			return (
				<th id={key + 'Id'} key={key} style={{width: '50%'}}>
					<div className="formRequired">
						<label className="control-label">{field.label}&nbsp;<div className="fa fa-key" style={{color: '#c3ae09'}}></div></label>
					</div>
				</th>
			);
		});
		return <tr>{headers}</tr>
	}

	rows() {
		var newRows = this.state.rows.map((item, index) => {
				var fields = this.props.fields.map((field) => {
					return this.fieldSelect(field, index);
				});

				return <tr key={index} className="odd gradeX">{fields}</tr>
			});
		return newRows;
	}

	render () {
        var headerComponents = this.headers();
        
		var rowComponents = this.rows();

		var className = 'control-group limitsAddRemoveMultiplesInputs';

		var headerClassName = 'ontrol-group header-table';

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
					<button id="btnMinus" type="button" className="btn btn-" aria-label="Left Align" onClick={this.removeRow}>
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