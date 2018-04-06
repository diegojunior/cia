import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../ui/form/FormValidator';
import TextInput from '../../../ui/input/TextInput';
import SelectInput from '../../../ui/input/SelectInput';
import PerfilConciliacaoService from '../../../../shared/services/PerfilConciliacao';
import LayoutService from '../../../../shared/services/Layout';
import {constants} from '../../../../shared/util/Constants';
import {sort} from '../../../../shared/util/Utils';
import './Style.css';

export default class Identificacao extends Component {

	constructor(props) {
		super(props);
		
		this.state = {
			identificacao: {codigo: '', descricao: '', sistema: null, status: null, tipoLayout: null, layout: null},
			sistemas: constants.getSistemasEnum(),
			tiposLayouts: constants.getTiposLayouts(),
			layouts: [],

			showErrors: false
		};

		this.changeCodigo = this.changeCodigo.bind(this);
		this.changeDescricao = this.changeDescricao.bind(this);
		this.changeSistema = this.changeSistema.bind(this);
		this.changeTipoLayout = this.changeTipoLayout.bind(this);
		this.changeLayout = this.changeLayout.bind(this);

		this.showErrors = this.showErrors.bind(this);
		this.clear = this.clear.bind(this);
	}

	showErrors (isShowErrors) {
		this.setState({showErrors: isShowErrors});
	}

	isValidForm() {
		return this.refs.formValidator.isValid();
	}

	changeCodigo (value) {
		var newState = update(this.state, {identificacao: {codigo: {$set: value}}});
		this.setState(newState);
	}

	changeDescricao (value) {
		var newState = update(this.state, {identificacao: {descricao: {$set: value}}});
		this.setState(newState);
	}

	changeSistema (value) {
		if(value != null && value != '') {
			var newState = update(this.state, {identificacao: {sistema: {$set: constants.getSistemasPorCodigo(value)}}});
			this.setState(newState);
		} else {
			var newState = update(this.state, {identificacao: {sistema: {$set: ''}}});
			this.setState(newState);
		}
	}

	changeTipoLayout (value) {
		let tipoLayout = constants.getTipoLayoutPorCodigo(value);

		this.state.layouts = [];

		this.refs.layout.clear();

		if (value != '') {
			let tipoLayoutConstant = constants.getConstantTipoLayoutPorCodigo(value);
		
			LayoutService.getBy(tipoLayoutConstant)
				.then(data => {;
					if (data.length == 0) {
						this.props.warningMessage('Layouts não encontrados.');
					}
					sort(data, 'codigo');
					this.state.layouts = data;
					let newState = update(this.state, {identificacao: {tipoLayout: {$set: tipoLayout}}});

					this.setState(newState);
				}).catch(error => {
					this.props.errorMessage(error);

					let newState = update(this.state, {identificacao: {tipoLayout: {$set: tipoLayout}}});
					this.setState(newState);
				});

		} else {
			let newState = update(this.state, {identificacao: {tipoLayout: {$set: tipoLayout}}});
			this.setState(newState);
		}
	}
	
	changeLayout (value) {

		if (value != '') {
			var layout = this.getLayout(value);

			let newState = update(this.state, {identificacao: {layout: {$set: layout}}});
			this.setState(newState);

		} else {
			let newState = update(this.state, {identificacao: {layout: {$set: null}}});
			this.setState(newState);
		}
	}

	getLayout(value) {
		for (var index in this.state.layouts) {
			var layout = this.state.layouts[index];
			if (layout.codigo == value) {
				return layout;
			}
		}
		return {};
	}

	formatValue (value) {
		return value.toUpperCase();
	}

	clear () {
		this.state.identificacao.codigo = '';
		this.state.identificacao.descricao = '';
		this.state.identificacao.sistema = null;
		this.state.identificacao.status = null;
		this.state.identificacao.tipoLayout = null;
		this.state.identificacao.layout = null;
		this.state.layouts = [];
		this.refs.formValidator.clearValues();

		var newState = update(this.state, {showErrors: {$set: false}});
		this.setState(newState);
	}

	render(){
	    
		return (
			<div className="cia-animation-fadein">
    			<div className="row">
    				
					<div className="col-lg-12">
						<div className="panel panel-default" id="cadPanel">
							<div className="panel-body">
								<FormValidator ref="formValidator">
									
									<TextInput
										id="codigo" 
										ref="codigo"
										type="text"
										label="Código"
										className="col-sm-5 col-md-5 col-lg-5"
										maxLength="30"
										required={true}
										chave={true}
										formatValue={this.formatValue}										
										showError={this.state.showErrors}
										valueChange={this.changeCodigo} />

									<TextInput
										id="descricao" 
										ref="descricao"
										type="text"
										label="Descrição"
										className="col-sm-5 col-md-5 col-lg-5"
										maxLength="60"
										showError={this.state.showErrors}
										valueChange={this.changeDescricao} />

									<SelectInput 
										id="sistema"
										ref="sistema"
										placeholder="Selecione..."
										name={'sistema'}
										label="Sistema"
										options={this.state.sistemas}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeSistema}
										required={true}
										showError={this.state.showErrors} />

									<SelectInput 
										id="tipoLayout"
										ref="tipoLayout"
										placeholder="Selecione..."
										name={'tipoLayout'}
										label="Tipo de Layout"
										options={this.state.tiposLayouts}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeTipoLayout}
										required={true}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="layout"
										ref="layout"
										placeholder="Selecione..."
										name={'layout'}
										label="Layout"
										options={this.state.layouts}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeLayout} 
										required={true}
										showError={this.state.showErrors}/>

								</FormValidator>
							</div>
						</div>
					</div>
    			</div>
    		</div>
		);
	}
}