import React, { Component } from 'react';
import update from 'immutability-helper';
import ValidatorComponent from '../../../../shared/infra/ValidatorComponent';
import BaseComponent from '../../../../shared/infra/BaseComponent';
import {required, integerOnly} from '../../../../shared/util/Rules';
import {ruleRunner, run} from '../../../../shared/util/RuleRunner';
import MultiplesInputsRules from '../../../ui/input/dynamic/MultiplesInputsRules';
import InputError from "../../../ui/input/InputError";
import './Style.css';

export default class TransformacaoMultiplesInputs extends ValidatorComponent {
	
	constructor(props) {
		super(props);
		this.state.rows = [];

		this.state.fields = [
			{
				key:'de', 
				label:'De',
				required: true
			},
			{
				key:'para', 
				label:'Para',
				required: true
			}
		]
		
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
			for (let item in this.state.fields) {
				this.props.addError(this.state.fields[item].key+index, this.state.validationErrors);
			}
		}
	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.selectedValue && !$.isEmptyObject(nextProps.selectedValue)) {
			var rows = [];
			for (var index in nextProps.selectedValue) {
				var element = nextProps.selectedValue[index];
				let newRow = {id: null};
				for (var i in this.state.fields) {
					var key = this.state.fields[i].key;
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
		for (var i in this.state.fields) {
			var fieldConfig = this.state.fields[i];

			if (rowIndex > 0) {
				newRow[fieldConfig.key+rowIndex] = '';
			}
		}
		this.adicionarFieldsValidations(rowIndex);
		return newRow;
	}

	// Adiciona os fields validators de Required
	adicionarFieldsValidations(rowIndex) {
		for (let item in this.state.fields) {
			let key = this.state.fields[item].key + rowIndex;
			let label = this.state.fields[item].label;
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
		for (var i in this.state.fields) {
			var key = this.state.fields[i].key;
			newRow[key+0] = '';
		}

		this.state.fieldValidations = [];

		this.adicionarFieldsValidations(0);
		
		this.state.rows = [newRow];
	}

    clearAllErrors () {
    	for (let rowId in this.state.rows) {
    		for (let i in this.state.fields) {
    			let key = this.state.fields[i].key + rowId;
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
		let newState = update(state, {rows: {[index]:  {[field.key+index]: {$set: value}}}});
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

	//Ação do usuário para remover a ultima linha, deve-se retirar os erros field Validator e do FormValidator
	removeRow() {
		var lastIndex = this.state.rows.length - 1;
		for (var index in this.state.fields) {
			var fieldConfig = this.state.fields[index];
			this.removeFieldValidation(this.state, fieldConfig.key+lastIndex, 'required');
			this.props.removeError(fieldConfig.key+lastIndex);
		}

		let newState = update(this.state, {rows: {$splice: [[-1, 1]]}});

		this.setState(newState);
	}

	isCamposPreenchidos () {
		for (var index in this.state.rows) {
			var row = this.state.rows[index];
			for (var i in this.state.fields) {
				var fieldConfig = this.state.fields[i];
				
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

	getFields () {
		return this.state.rows;
	}

	fieldsText (field, index) {
		let readOnly = this.props.readOnly;
		var value = this.state.rows[index][field.key+index] ? this.state.rows[index][field.key+index] : '';
		var key = field.key;
		return (
			
			<td key={key} id={key+'Item'} style={{width: '20%'}}>
				<div className="form-group">
					<input 
						id={key+index}
						type='text'
						maxLength={30}
						className="form-control"
						value={value}
						onChange={e => this.handlerFields(e.target.value, field, index)}
						readOnly={readOnly}/>
					<InputError 
						visible={this.shouldDisplayError()}
						errorMessage={this.errorFor(key + index)} />
				</div>	
			</td>	
		);
	}

	headers() {
		var headers = this.state.fields.map((field) => {
			var key = field.key;
			return (
				<th id={key + 'Id'} key={key} style={{width: '20%'}}>
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
				var fields = this.state.fields.map((field) => {
					return this.fieldsText(field, index);
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