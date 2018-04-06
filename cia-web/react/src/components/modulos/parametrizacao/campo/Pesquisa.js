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
	    	codigo :  '',
	    	tipo : '',
			tipos : constants.getTiposValoresDominioEnum()
	    }
	    this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);

	    this.changeCodigo= this.changeCodigo.bind(this);
	    this.changeTipo = this.changeTipo.bind(this);
	}

	componentWillUpdate (nextProps, nextState) {
		if (nextProps.template.isCadastroAtivo()) {
			nextState.codigo = '';
			nextState.tipo = '';
			this.refs.formValidator.clearValues();
		}
	}

	consultar () {
		return (event) => {
			event.preventDefault();
			this.props.template.consultar(this.state.codigo, this.state.tipo.codigo);
		}
	}

	limpar () {
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
		
			let newState = update(this.state, {codigo: {$set: ''}, tipo: {$set: ''}});
			this.setState(newState);

		}
	}

	changeCodigo (value) {
		let newState = update(this.state, {codigo: {$set: value}});
		this.setState(newState);
	}

	changeTipo (value) {
		let tipo = constants.getTipoValorDominioPorCodigo(value);
		if (tipo == null) {
			tipo = '';
		}
		let newState = update(this.state, {tipo: {$set: tipo}});
		this.setState(newState);
	}
	
   clear () {
		let newState = update(this.state, {codigo: {$set: ''}, tipo: {$set: ''}});
		this.setState(newState);
	}
	
    render () {		

        return (
        		
	    	<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Campo - Pesquisa
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
										chave={true}
										showError={false}
										maxLength="30"
										valueChange={this.changeCodigo} />

									<SelectInput 
										id="tipoPesquisa"
										placeholder="Selecione..."
										name={'tipo'}
										label="Tipo"
										options={this.state.tipos}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeTipo} />

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