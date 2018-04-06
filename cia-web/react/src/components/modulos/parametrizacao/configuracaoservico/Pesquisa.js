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
				sistema: '', 
				servico: '', 
				tipoServico: '', 

				sistemas: [],
				servicos: [],
				tiposServicos: []
		};
				
	    this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);

		this.changeCodigo = this.changeCodigo.bind(this);
		this.changeDescricao = this.changeDescricao.bind(this);
		this.changeSistema = this.changeSistema.bind(this);
		this.changeServico = this.changeServico.bind(this);
		this.changeTipoServico = this.changeCodigo.bind(this);
	    
	}

	componentWillMount () {
		this.state.sistemas = constants.getSistemasEnum();
		this.state.servicos = constants.getServicosEnum();
		this.state.tiposServicos = constants.getTiposServicosEnum();
	}

	componentWillUpdate (nextProps, nextState) {
		if (nextProps.template.isCadastroAtivo()) {

			nextState.codigo = '';
			nextState.descricao = '';
			nextState.sistema = ''; 
			nextState.servico = '';
			nextState.tipoServico = '';

			this.refs.formValidator.clearValues();
		}
	}

	consultar () {
		return (event) => {
			event.preventDefault();

			var sistemaConstant = this.state.sistema != null && 
				this.state.sistema != ''? 
				constants.getConstantSistemasPorCodigo(this.state.sistema.codigo): 
				null;
			
			var servicoConstant = this.state.servico != null && 
				this.state.servico != ''? 
				constants.getConstantServicoPorCodigo(this.state.servico.codigo): 
				null;

			var tipoServicoConstant = this.state.tipoServico != null && 
				this.state.tipoServico != ''? 
				constants.getConstantTipoServicoPorCodigo(this.state.tipoServico.codigo): 
				null;

			this.props.template.consultar(this.state.codigo, this.state.descricao, 
				sistemaConstant, servicoConstant, tipoServicoConstant);
		}
	}

	limpar () {
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
		
			let newState = update(this.state, {codigo: {$set: ''}, descricao: {$set: ''}, 
				sistema: {$set: ''}, servico: {$set: ''}, tipoServico: {$set: ''}});
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
		if(value != null && value != '') {
			var newState = update(this.state, {sistema: {$set: constants.getSistemasPorCodigo(value)}});
			this.setState(newState);
		} else {
			var newState = update(this.state, {sistema: {$set: ''}});
			this.setState(newState);
		}
	}

	changeServico (value) {
		if(value != null && value != '') {
			var newState = update(this.state, {servico: {$set: constants.getServicoPorCodigo(value)}});
			this.setState(newState);
		} else {
			var newState = update(this.state, {servico: {$set: ''}});
			this.setState(newState);
		}
	}

	changeTipoServico (value) {
		if(value != null && value != '') {
			var newState = update(this.state, {tipoServico: {$set: constants.getTipoServicoPorCodigo(value)}});
			this.setState(newState);
		} else {
			var newState = update(this.state, {tipoServico: {$set: ''}});
			this.setState(newState);
		}
	}
	
   clear () {
		let newState = update(this.state, {codigo: {$set: ''}, descricao: {$set: ''}, 
			sistema: {$set: ''}, servico: {$set: ''}, tipoServico: {$set: ''}});
		this.setState(newState);
	}
	
    render () {		

        return (
        		
	    	<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Configuração de Serviços - Pesquisa
						</div>
					</div>
					<div className="col-lg-12">
					
						<div className="navigationAlign">
							<button type="button" className="btn btn-social btn-twitter" id="renderCadastro" onClick={this.props.template.showCadastro}><i className="fa fa-plus"></i>Cadastrar</button>
		        		</div>

		        		<div className="panel panel-primary" id="filterPanel">

		        		    <div className="panel-heading">
		        		        Pesquisa
		        		    </div>
		        		    <div className="panel-body">

								<FormValidator ref="formValidator">
									
									<TextInput 
										id="codigoPesquisa"
										label="Código"
										type="text"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										maxLength="30"
										valueChange={this.changeCodigo} />

									<TextInput 
										id="descricaoPesquisa"
										label="Descrição"
										type="text"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										maxLength="60"
										valueChange={this.changeDescricao} />

									<SelectInput 
										id="sistemaPesquisa"
										placeholder="Selecione..."
										name={'sistema'}
										label="Sistema"
										options={this.state.sistemas}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeSistema} />

									<SelectInput 
										id="servicoPesquisa"
										placeholder="Selecione..."
										name={'servico'}
										label="Serviço"
										options={this.state.servicos}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeServico} />

									<SelectInput 
										id="tipoServicoPesquisa"
										placeholder="Selecione..."
										name={'tipoServico'}
										label="Tipo de Serviço"
										options={this.state.tiposServicos}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeTipoServico} />

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