import React, { Component } from 'react';
import { Link } from 'react-router';
import FormValidator from '../../../ui/form/FormValidator';
import ButtonCrud from '../../../ui/button/ButtonCrud';
import TextInput from '../../../ui/input/TextInput';
import SelectInput from '../../../ui/input/SelectInput';
import update from 'immutability-helper';
import {constants} from '../../../../shared/util/Constants';
import './Style.css';

export default class Pesquisa extends Component {

	constructor(props) {
		super(props);
		
	    this.state = {
	    	codigo: '',
			descricao: '',
			sistema: null,
			tipoLayout: null,			
			layout: '',
			statusPerfil: null,
			
			sistemas: [],
			tiposLayouts :  [],
			status: []
		}
		
	    this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);

		this.changeCodigo = this.changeCodigo.bind(this);
		this.changeDescricao = this.changeDescricao.bind(this);
		this.changeSistema = this.changeSistema.bind(this);
	    this.changeTipoLayout = this.changeTipoLayout.bind(this);
		this.changeLayout = this.changeLayout.bind(this);
		this.changeStatus = this.changeStatus.bind(this);
		
		this.showCadastro = this.showCadastro.bind(this);
	}

	componentWillMount () {
		this.state.sistemas = constants.getSistemasEnum();
		this.state.tiposLayouts = constants.getTiposLayouts();
		this.state.status = constants.getStatus();
	}

	componentDidMount () {
		this.state.statusPerfil = constants.getStatus()[0];
		this.refs.statusFilter.setDefaultValue(constants.getStatus()[0].codigo);
	}

	componentWillUpdate (nextProps, nextState) {
		if (nextProps.template.isCadastroAtivo()) {
			
			nextState.codigo = '';
			nextState.descricao = '';
			nextState.sistema = null;
			nextState.tipoLayout = null;
			nextState.layout = '';
			nextState.statusPerfil = constants.getStatus()[0];

			this.refs.formValidator.clearValues();
		}
	}

	showCadastro () {
		return (event) => {
			this.props.template.showCadastro(false);
		}
	}

	consultar () {
		return (event) => {
			event.preventDefault();

			var sistemaConstant = this.state.sistema != null && 
				this.state.sistema != ''? 
				constants.getConstantSistemasPorCodigo(this.state.sistema.codigo): 
				null;

			var tipoLayoutConstant = this.state.tipoLayout != null && 
				this.state.tipoLayout != ''? 
				constants.getConstantTipoLayoutPorCodigo(this.state.tipoLayout.codigo): 
				null;

			var statusConstant = this.state.statusPerfil != null && 
				this.state.statusPerfil != ''? 
				constants.getConstantStatusCodigo(this.state.statusPerfil.codigo): 
				null;

			this.props.template.consultar(this.state.codigo, this.state.descricao, sistemaConstant, 
				tipoLayoutConstant, this.state.layout, statusConstant);
		}
	}

	limpar () {
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();

			this.refs.statusFilter.setDefaultValue(constants.getStatus()[0].codigo);
		
			let newState = update(this.state, {codigo: {$set: ''}, descricao: {$set: ''}, sistema: {$set: null}, 
				tipoLayout: {$set: null}, layout: {$set: ''}, statusPerfil: {$set: constants.getStatus()[0]}});
			this.setState(newState);

		}
	}

	changeCodigo (value) {
		let newState = update(this.state, {codigo: {$set: value}});
		this.setState(newState);
	}

	changeDescricao (value) {
		let newState = update(this.state, {descricao: {$set: value}});
		this.setState(newState);
	}

	changeSistema (value) {
		let sistema = constants.getSistemasPorCodigo(value);

		let newState = update(this.state, {sistema: {$set: sistema}});
		this.setState(newState);
	}

	changeTipoLayout (value) {
		let tipoLayout = constants.getTipoLayoutPorCodigo(value);

		let newState = update(this.state, {tipoLayout: {$set: tipoLayout}});
		this.setState(newState);
	}

	changeLayout (value) {
		let newState = update(this.state, {layout: {$set: value}});
		this.setState(newState);
	}

	changeStatus (value) {
		this.refs.formValidator.setClear(false);
		let status = constants.getStatusCodigo(value);

		let newState = update(this.state, {statusPerfil: {$set: status}});
		this.setState(newState);
	}
		
	render () {		
		
		return (
				
			<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Perfil de Conciliação - Pesquisa
						</div>
					</div>
					<div className="col-lg-12">
					
						<div className="navigationAlign">
							<button type="button" className="btn btn-social btn-twitter" id="renderCadastro" onClick={this.showCadastro()}><i className="fa fa-plus"></i>Cadastrar</button>
						</div>

						<div className="panel panel-primary" id="filterPanel">

							<div className="panel-heading">
								Pesquisa
							</div>
							<div className="panel-body">

								<FormValidator ref="formValidator">

									<TextInput 
										id="codigo"
										label="Código"
										type="text"
										maxLength="30"
										chave={true}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeCodigo} />

									<TextInput 
										id="descricao"
										label="Descrição"
										type="text"
										maxLength="60"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeDescricao} />

									<SelectInput 
										id="sistema"
										placeholder="Selecione..."
										name={'sistema'}
										label="Sistema"
										options={this.state.sistemas}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeSistema} />

									<SelectInput 
										id="tipoLayout"
										placeholder="Selecione..."
										name={'tipoLayout'}
										label="Tipo de Layout"
										options={this.state.tiposLayouts}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeTipoLayout} />
									
									<TextInput 
										id="layout"
										label="Layout"
										type="text"
										maxLength="30"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeLayout} />

									<SelectInput 
										id="status"
										ref="statusFilter"
										placeholder="Selecione..."
										name={'Status'}
										label="Status"
										options={this.state.status}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeStatus} />

									<ButtonCrud 
										showIncluir={false} 
										showAlterar={false}
										showConsultar={true}
										label="Limpar"
										consultar={this.consultar()}
										clear={this.limpar()} />

								</FormValidator>
									
							</div>
						</div>		        		
					</div>
				</div>
			</div>
		
		);
	}
}