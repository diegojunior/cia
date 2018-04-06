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
	    	tiposLayouts :  [],
			tiposTransformacoes: [],
			tipoLayout: '',			
			layout: '',
			sessao: '',
			campo: '',
			tipoTransformacao: ''
	    }
	    this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);

	    this.changeTipoLayout= this.changeTipoLayout.bind(this);
		this.changeLayout= this.changeLayout.bind(this);
		this.changeSessao = this.changeSessao.bind(this);
		this.changeCampo= this.changeCampo.bind(this);
		this.changeTipoTransformacao= this.changeTipoTransformacao.bind(this);
	}

	componentWillMount () {
		this.state.tiposLayouts = constants.getTiposLayouts();
		this.state.tiposTransformacoes = constants.getTiposTransformacoes();
	}

	componentWillUpdate (nextProps, nextState) {
		if (nextProps.template.isCadastroAtivo()) {
			
			nextState.tipoLayout = '';
			nextState.layout = '';
			nextState.sessao = '';
			nextState.campo = '';
			nextState.tipoTransformacao = '';

			this.refs.formValidator.clearValues();
		}
	}

	consultar () {
		return (event) => {
			event.preventDefault();

			var tipoLayoutConstant = this.state.tipoLayout != null && 
				this.state.tipoLayout != ''? 
				constants.getConstantTipoLayoutPorCodigo(this.state.tipoLayout.codigo): 
				null;
			
			var tipoTransformacaoConstant = this.state.tipoTransformacao != null && 
				this.state.tipoTransformacao != ''? 
				constants.getConstantTipoTransformacaoPorCodigo(this.state.tipoTransformacao.codigo): 
				null;

			this.props.template.consultar(tipoLayoutConstant, this.state.layout, 
				this.state.sessao, this.state.campo, tipoTransformacaoConstant);
		}
	}

	limpar () {
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
		
			let newState = update(this.state, {tipoLayout: {$set: ''}, layout: {$set: ''}, sessao: {$set: ''}, campo: {$set: ''}, tipoTransformacao: {$set: ''}});
			this.setState(newState);

		}
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

	changeSessao (value) {
		let newState = update(this.state, {sessao: {$set: value}});
		this.setState(newState);
	}

	changeCampo (value) {
		let newState = update(this.state, {campo: {$set: value}});
		this.setState(newState);
	}

	changeTipoTransformacao (value) {

		var tipoTransformacao = constants.getTipoTransformacaoPorCodigo(value);

		let newState = update(this.state, {tipoTransformacao: {$set: tipoTransformacao}});
		this.setState(newState);
	}
	
   clear () {
		let newState = update(this.state, {tipoLayout: {$set: ''}, layout: {$set: ''}, sessao: {$set: ''}, campo: {$set: ''}, tipoTransformacao: {$set: ''}});
		this.setState(newState);
	}
	
    render () {		

        return (
        		
	    	<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Transformação - Pesquisa
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
										chave={true}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeLayout} />

									<TextInput 
										id="sessao"
										label="Sessão"
										type="text"
										maxLength="30"
										chave={true}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeSessao} />

									<TextInput 
										id="campo"
										label="Campo"
										type="text"
										maxLength="30"
										chave={true}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeCampo} />

									<SelectInput 
										id="tipoTransformacao"
										placeholder="Selecione..."
										name={'tipoTransformacao'}
										label="Tipo de Transformação"
										options={this.state.tiposTransformacoes}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeTipoTransformacao} />

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