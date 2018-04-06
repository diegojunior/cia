import React, { Component } from 'react';
import { Link } from 'react-router';
import FormValidator from '../../../../ui/form/FormValidator';
import ButtonCrud from '../../../../ui/button/ButtonCrud';
import TextInput from '../../../../ui/input/TextInput';
import SelectInput from '../../../../ui/input/SelectInput';
import update from 'immutability-helper';
import {constants} from '../../../../../shared/util/Constants';
import './Style.css';

export default class Pesquisa extends Component {

	constructor(props) {
		super(props);
		this.state = {codigoFilter: '', 
					  descricaoFilter: '', 
					  codigoIdentificadorFilter: '', 
					  statusFilter: null,
			          status: constants.getStatus()
					 };

		this.consultar = this.consultar.bind(this);
		this.limpar = this.limpar.bind(this);

		this.clear = this.clear.bind(this);

		this.changeCodigoFilter = this.changeCodigoFilter.bind(this);
		this.changeDescricaoFilter = this.changeDescricaoFilter.bind(this);
		this.changeCodigoIdentificadorFilter = this.changeCodigoIdentificadorFilter.bind(this);
		this.changeStatusFilter = this.changeStatusFilter.bind(this);
	}

	showCadastro () {
		return (event) => {
			this.props.template.showCadastro(false);
		}
	}

	componentDidMount () {
		this.state.statusFilter = constants.getStatus()[0];
		this.refs.statusFilter.setDefaultValue(constants.getStatus()[0].codigo);
	}

	componentWillUpdate (nextProps, nextState) {
		if (nextProps.template.isCadastroAtivo()) {
			nextState.codigoFilter = '';
			nextState.descricaoFilter = '';
			nextState.codigoIdentificadorFilter = '';
			nextState.statusFilter = constants.getStatus()[0];

			this.refs.formValidator.clearValues();
		}
	}

	consultar () {
		return (event) => {
			event.preventDefault();
			var statusConstant = this.state.statusFilter != null && 
				this.state.statusFilter != ''? 
				constants.getConstantStatusCodigo(this.state.statusFilter.codigo): 
				null;
				
			this.props.template.consultar(this.state.codigoFilter, this.state.descricaoFilter, this.state.codigoIdentificadorFilter, statusConstant);
		}
	}

	limpar () {
		return (event) => {
			event.preventDefault();
			this.clear();
		}
	}

	clear () {
		this.refs.formValidator.clearValues();
		this.refs.statusFilter.setDefaultValue(constants.getStatus()[0].codigo);
		let newState = update(this.state, {codigoFilter: {$set: ''}, 
										   descricaoFilter: {$set: ''}, 
										   codigoIdentificadorFilter: {$set: ''}, 
										   statusFilter: {$set: constants.getStatus()[0]}});
		this.setState(newState);
	}

	changeCodigoFilter (value) {
		let newState = update(this.state, {codigoFilter: {$set: value}});
		this.setState(newState);
	}

	changeDescricaoFilter (value) {
		let newState = update(this.state, {descricaoFilter: {$set: value}});
		this.setState(newState);
	}

	changeCodigoIdentificadorFilter (value) {
		let newState = update(this.state, {codigoIdentificadorFilter: {$set: value}});
		this.setState(newState);
	}

	changeStatusFilter (value) {
		this.refs.formValidator.setClear(false);
		let status = constants.getStatusCodigo(value);
		let newState = update(this.state, {statusFilter: {$set: status}});
		this.setState(newState);
	}
		
	render () {		
		
		return (
				
			<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Layout Delimitador - Pesquisa
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
										id="codigoFilter"
										label="Código"
										maxLength="30"
										chave={true}
										type="text"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeCodigoFilter} />
	
									<TextInput 
										id="descricaoFilter"
										label="Descrição"
										maxLength="60"
										type="text"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeDescricaoFilter} />

									<TextInput 
										id="codigoIdentificadorFilter"
										label="Código da Linha"
										type="text"
										maxLength="30"
										chave={true}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeCodigoIdentificadorFilter} />
	
									<SelectInput
										id="idStatus" 
										ref="statusFilter"
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Status"
										name={'status'}
										options={this.state.status}
										valueChange={this.changeStatusFilter}
										showError={false} />
													
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