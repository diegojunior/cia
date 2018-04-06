import React, { Component } from 'react';
import update from 'immutability-helper';
import ValidatorComponent from '../../../../../shared/infra/ValidatorComponent';
import DominioService from '../../../../../shared/services/Dominio';
import BaseComponent from '../../../../../shared/infra/BaseComponent';
import {required, integerOnly} from '../../../../../shared/util/Rules';
import {ruleRunner, run} from '../../../../../shared/util/RuleRunner';
import MultiplesInputsRules from './MultiplesInputsRules';
import InputError from "../../../../ui/input/InputError";
import './Style.css';

export default class MultiplesInputs extends ValidatorComponent {
	
	constructor(props) {
		super(props);

		this.state.rows = [];

		this.state.fieldsConfig = [];
		this.state.dominios = [];
		this.state.dominiosRestantes = [];

		this.addRow = this.addRow.bind(this);
		this.removeCurrentRow = this.removeCurrentRow.bind(this);
		this.initialRow = this.initialRow.bind(this);

		this.limpar = this.limpar.bind(this);
		this.isValid = this.isValid.bind(this);

		this.getDominios = this.getDominios.bind(this);

		this.checkRowDuplicity = this.checkRowDuplicity.bind(this);

		this.handlerFieldRow = this.handlerFieldRow.bind(this);
	}

	shouldDisplayError() {
        return this.props.showError && !$.isEmptyObject(this.state.validationErrors);
	}
	
	componentWillMount () {
	
		this.loadDominios();
		
		this.state.fieldsConfig = [
			{	key:'dominio', 
				label:'Campo', 
				type: 'select', 
				initialValue: {id: null, codigo: '', tipo: {}}, 
				placeholder: "Selecione...", 
				options: this.getDominios, 
				required: true, 
				chave: true,
				fieldReadOnly: "pattern",
				integerOnly: false, 
				readOnly: false,
				setPrevValue: false,				
				styleColumnHeader: {'width': '20%'},
				styleColumn: {'width': '20%'}
			},
			{	key:'descricao', 
				label:'Descrição', 
				type: 'text', 
				initialValue: '', 
				placeholder: "", 
				required: false, 
				integerOnly: false, 
				readOnly: false,
				setPrevValue: false,
				maxLength: '60',
				styleColumnHeader: {'width': '22%'},
				styleColumn: {'width': '22%'}
				
			},
			{	key:'posicaoInicial', 
				label:'Posição Inicial', 
				type: 'text', 
				initialValue: 1, 
				placeholder: "Somente Números Inteiros", 
				required: true, 
				integerOnly: true, 
				readOnly: true,
				setPrevValue: true,
				subtractWithNextAndSetFollowing: true,
				maxLength: '4',				
				styleColumnHeader: {'width': '19%'},
				styleColumn: {'width': '19%'}
			},
			{	key:'posicaoFinal', 
				label:'Posição Final', 
				type: 'text',
				initialValue: '', 
				placeholder: "Somente Números Inteiros", 
				required: true, 
				integerOnly: true, 
				readOnly: false,
				setPrevValue: false,
				getValue: true,
				isBiggerThanBefore: true,
				subtractWithPrevAndSetFollowing: true,
				maxLength: '4',				
				styleColumnHeader: {'width': '19%'},
				styleColumn: {'width': '19%'}
			},
			{	key:'tamanho', 
				label:'Tamanho', 
				type: 'text', 
				initialValue: '', 
				required: false, 
				integerOnly: true, 
				readOnly: true,
				setPrevValue: false,
				maxLength: '4',
				styleColumnHeader: {'width': '10%'},
				styleColumn: {'width': '10%'}
			},
			{	key:'pattern', 
				label:'Formato', 
				type: 'text',
				initialValue: '', 
				placeholder: "Formato", 
				required: false, 
				integerOnly: false, 
				readOnly: false,
				maxLength: '30',
				styleColumnHeader: {'width': '10%'},
				styleColumn: {'width': '8.92%'}
			}
		];

		this.state.rows.push(this.createRow());

		this.state.validationErrors = run(this.state, this.getFunctionsValidators(this.state));
	}

	componentWillReceiveProps (nextProps) {

		if (nextProps.selectedValue && !$.isEmptyObject(nextProps.selectedValue)) {
			var rows = [];
			
			for (var index in nextProps.selectedValue) {
				var element = nextProps.selectedValue[index];
				let newRow = {id: null};
				for (var i in this.state.fieldsConfig) {
					var fieldConfig = this.state.fieldsConfig[i];
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
	
	loadDominios () {
		DominioService.getAll()
			.then (data => {
				if (data.length > 0) {
					this.state.dominiosRestantes = data;
					var newState = update(this.state, {dominios: {$push: data}});
					this.setState(newState);
				}
			});
	}

	getDominios() {
		return this.state.dominiosRestantes;
	}

	getDominio (codigo) {
		for (var index in this.state.dominios) {
			var dominio = this.state.dominios[index];			
			if(codigo == dominio.codigo) {
				return dominio;
			}
		}
		return null;
	}

    isValid() {
    	return $.isEmptyObject(this.state.validationErrors);
    }

    adicionarFieldsValidations(rowIndex, exception){

    	for(var index in this.state.fieldsConfig) {

    		var fieldConfig = this.state.fieldsConfig[index];

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
			.apply(newState, this.state.fieldsConfig, fieldConfig, index, onBlurCheck);

		if ($.isEmptyObject(newStateWithAllInputRules.validationErrors)) {
			newStateWithAllInputRules = this.checkRowDuplicity(newStateWithAllInputRules);
		}

		this.setState(newStateWithAllInputRules);
	}

	checkRowDuplicity (stateParam) {

		var fields = stateParam.rows;

		var campos = [];

		for (var indexField in fields) {
			var field = fields[indexField];
			
			var dominio = this.getDominio(field['dominio'+indexField].codigo);
			var formato = field['pattern'+indexField] && field['pattern'+indexField].hasOwnProperty("value") ? 
				field['pattern'+indexField].value : field['pattern'+indexField];
			var campo = {id: field['id'], 
						dominio: dominio, 
						descricao: field['descricao'+indexField], 
						posicaoInicial: field['posicaoInicial'+indexField], 
						posicaoFinal: field['posicaoFinal'+indexField], 
						tamanho: field['tamanho'+indexField],
						pattern: formato
						};

			if (dominio != null
				&& this.campoDuplicated(campos, campo)) {
				
				this.refs.baseComponent.errorMessage('Chave(s) Duplicada(s).');
				return this.removeRow(stateParam);

			} else {
				campos.push(campo);
			}
			
		}
		return stateParam;
	}

	campoDuplicated (campos, campo) {
		for (let index in campos) {
			let campoAdded = campos[index];
			if (campoAdded.id == campo.id
				&& campoAdded.dominio.codigo == campo.dominio.codigo) {
				return true;
			}
		}
		return false;
	}


	getCampos () {

		var campos = [];
		var ordem = 0;

		for (var indexField in this.state.rows) {
			var field = this.state.rows[indexField];

			var dominio = this.getDominio(field['dominio'+indexField].codigo);
			var formato = field['pattern'+indexField] && field['pattern'+indexField].hasOwnProperty("value") ? 
				field['pattern'+indexField].value : field['pattern'+indexField];
			var campo = {id: field['id'], 
						ordem: ordem++,
						dominio: dominio, 
						descricao: field['descricao'+indexField], 
						posicaoInicial: field['posicaoInicial'+indexField], 
						posicaoFinal: field['posicaoFinal'+indexField], 
						tamanho: field['tamanho'+indexField],
						pattern: formato};
			campos.push(campo);
		}

		return campos;
	}

	changeRowField(state, value, fieldConfig, index) {
		this.manageFieldValidator(state, fieldConfig, index, value);

		let newState;
		if (fieldConfig.type == 'select') {
			newState = update(this.state, {rows: {[index]:  {[fieldConfig.key+index]: {codigo: {$set: value}}}}});
		} else {
			if (fieldConfig.key == 'pattern') {
				let readOnly = this.state.rows[index][fieldConfig.key+index].readOnly;
				newState = update(this.state, {rows: {[index]:  {[fieldConfig.key+index]: {$set: {value: value, readOnly: readOnly}}}}});
			} else {
				newState = update(this.state, {rows: {[index]:  {[fieldConfig.key+index]: {$set: value}}}});
			}
		}
		
		return newState;
	}

	createRow () {
		let newRow = {id: null};
		let rowIndex = this.state.rows.length;
		var exception = [];
		for (var i in this.state.fieldsConfig) {
			var fieldConfig = this.state.fieldsConfig[i];

			if (fieldConfig.setPrevValue && rowIndex > 0) {
				var lastRow = Number(rowIndex) - 1;
				for (var x in this.state.fieldsConfig) {
					var fg = this.state.fieldsConfig[x];
					if (fg.getValue) {
						newRow[fieldConfig.key+rowIndex] = Number(this.state.rows[lastRow][fg.key+lastRow]) + 1;
						exception.push(fieldConfig.key+rowIndex);
						break;
					}
				}
			} else {
				if (fieldConfig.initialValue) {
					newRow[fieldConfig.key+rowIndex] = fieldConfig.initialValue;					
				} else if (fieldConfig.required) {
					if (fieldConfig.integerOnly) {
						newRow[fieldConfig.key+rowIndex] = 0;
					} else {
						newRow[fieldConfig.key+rowIndex] = '';
					}
				}
				if (fieldConfig.type != 'select') {
					exception.push(fieldConfig.key+rowIndex);	
				}
			}
		}

		this.adicionarFieldsValidations(rowIndex, exception);

		return newRow;
	}

	initialRow () {
		let newRow = {id: null};
		let exception = [];				
		for (var i in this.state.fieldsConfig) {
			var fieldConfig = this.state.fieldsConfig[i];
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
		var mensagem = 'Para Inserir Novos Campos, é Necessário Preencher Todos Obrigatórios';
		for (var index in this.state.rows) {
			var row = this.state.rows[index];
			for (var i in this.state.fieldsConfig) {
				var fieldConfig = this.state.fieldsConfig[i];
				
				if (fieldConfig.required) {
					var withValue = row[fieldConfig.key+index] && row[fieldConfig.key+index].hasOwnProperty("value");
					if (withValue && row[fieldConfig.key+index].value == '') {
						this.refs.baseComponent.infoMessage(mensagem);
						return false;
					} else {
						var withCodigo = row[fieldConfig.key+index] && row[fieldConfig.key+index].hasOwnProperty("codigo"); 
						if (row[fieldConfig.key+index] == '' || (withCodigo && row[fieldConfig.key+index].codigo == '')) {
							this.refs.baseComponent.infoMessage(mensagem);
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	removeCurrentRow() {
		
		var lastIndex = this.state.rows.length - 1;
		for (var index in this.state.fieldsConfig) {
			var fieldConfig = this.state.fieldsConfig[index];
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

	removeRow(stateParam) {
		
		var lastIndex = stateParam.rows.length - 1;
		for (var index in this.state.fieldsConfig) {
			var fieldConfig = this.state.fieldsConfig[index];
			if(fieldConfig.required) {
				this.removeFieldValidation(stateParam, fieldConfig.key+lastIndex, 'required');
			}
			if(fieldConfig.integerOnly) {
				this.removeFieldValidation(stateParam, fieldConfig.key+lastIndex, 'integerOnly');	
			}
		}

		let newState = update(stateParam, {rows: {$splice: [[-1, 1]]}});

		return newState;
	}

	generateHeaders() {

		var cells = this.state.fieldsConfig.map((fieldConfig, index) => {
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
			
			var fields = this.state.fieldsConfig.map((fieldConfig) => {
				if (fieldConfig.type == 'select') {
					return this.fieldSelect(fieldConfig, index);
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
		
		var value = this.state.rows[index][fieldConfig.key+index] ? 
			this.state.rows[index][fieldConfig.key+index] : '';
		if (this.state.rows[index][fieldConfig.key+index] && 
			this.state.rows[index][fieldConfig.key+index].hasOwnProperty("value")) {
			value = this.state.rows[index][fieldConfig.key+index].value;
		}
		
		if (this.state.rows[index][fieldConfig.key+index] && 
			this.state.rows[index][fieldConfig.key+index].hasOwnProperty("readOnly")) {
			readOnly = this.state.rows[index][fieldConfig.key+index].readOnly;
		}

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
					<button id="btnPlus" type="button" className="btn btn-" aria-label="Left Align" onClick={this.addRow}>
						<span className="glyphicon glyphicon-plus"></span>
					</button>
					&nbsp;
					<button id="btnMinus" type="button" className="btn btn-" aria-label="Left Align" onClick={this.removeCurrentRow}>
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