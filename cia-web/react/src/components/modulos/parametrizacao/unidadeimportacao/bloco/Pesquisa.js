import React, { Component } from 'react';
import { Link } from 'react-router';
import FormValidator from '../../../../ui/form/FormValidator';
import ButtonCrud from '../../../../ui/button/ButtonCrud';
import TextInput from '../../../../ui/input/TextInput';
import update from 'immutability-helper';
import {constants} from '../../../../../shared/util/Constants';
import './Style.css';

export default class Pesquisa extends Component {
	
	constructor(props) {
	    super(props);
	    this.state = {
	    	codigo :  '',
	    	descricao : '',
			layout: ''
	    }
	    
		this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);
	    this.changeCodigo= this.changeCodigo.bind(this);
	    this.changeDescricao = this.changeDescricao.bind(this);
		this.changeLayout = this.changeLayout.bind(this);
	}

	componentWillUpdate (nextProps, nextState) {
		if (nextProps.template.isCadastroAtivo()) {
			nextState.codigo = '';
			nextState.descricao = '';
			nextState.layout = '';
			this.refs.formValidator.clearValues();
		}
	}

	consultar () {
		return (event) => {
			event.preventDefault();
			this.props.template.consultar(this.state.codigo, this.state.descricao, this.state.layout);
		}
	}

	limpar () {
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
		
			let newState = update(this.state, {codigo: {$set: ''}, descricao: {$set: ''}, layout: {$set: ''}});
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

	changeLayout (value) {
		let newState = update(this.state, {layout: {$set: value}});
		this.setState(newState);
	}
	
   clear () {
		let newState = update(this.state, {codigo: {$set: ''}, descricao: {$set: ''}, layout: {$set: ''}});
		this.setState(newState);
	}
	
    render () {		

        return (
        		
	    	<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Unidade de Importação por Bloco - Pesquisa
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
										maxLength="30"
										chave={true}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeCodigo} />

									<TextInput 
										id="descricaoPesquisa"
										label="Descrição"
										type="text"
										maxLength="60"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeDescricao} />

									<TextInput 
										id="layoutPesquisa"
										label="Layout"
										type="text"
										maxLength="30"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeLayout} />

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