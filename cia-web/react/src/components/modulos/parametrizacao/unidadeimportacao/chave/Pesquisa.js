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
	    this.state = {
	    	codigo :  '',
	    	descricao : '',
	    	tipoLayout: '',
			layout: '',
			codigoUnidade: '',
			tiposLayout: constants.getTiposLayouts(),
	    }
	    
	    this.limpar = this.limpar.bind(this);
	    this.changeCodigo= this.changeCodigo.bind(this);
	    this.changeDescricao = this.changeDescricao.bind(this);
	    this.changeTipoLayout = this.changeTipoLayout.bind(this);
		this.changeLayout = this.changeLayout.bind(this);
		this.changeCodigoIdentificador = this.changeCodigoIdentificador.bind(this);
	}

	componentWillUpdate (nextProps, nextState) {
		if (nextProps.template.isCadastroAtivo()) {
			nextState.codigo = '';
			nextState.descricao = '';
			nextState.layout = '';
			this.refs.formValidator.clearValues();
		}
	}

	limpar () {
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
		
			let newState = update(this.state, {codigo: {$set: ''}, descricao: {$set: ''}, layout: {$set: ''}, codigoIdentificador: {$set: ''}});
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

	changeTipoLayout (value) {
		let tipoLayout = constants.getConstantTipoLayoutPorCodigo(value);
		let newState = update(this.state, {tipoLayout: {$set: tipoLayout}});
		this.setState(newState);
	}

	changeLayout (value) {
		let newState = update(this.state, {layout: {$set: value}});
		this.setState(newState);
	}

	changeCodigoIdentificador (value) {
		let newState = update(this.state, {codigoIdentificador: {$set: value}});
		this.setState(newState);
	}
	
   clear () {
		let newState = update(this.state, {codigo: {$set: ''}, descricao: {$set: ''}, layout: {$set: ''}, codigoIdentificador: {$set: ''}});
		this.setState(newState);
	}
	
    render () {		

        return (
        		
	    	<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Unidade de Importação por Chave - Pesquisa
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

									<SelectInput
											id="tipoLayoutId" 
											placeholder="Selecione..."
											className="col-sm-5 col-md-5 col-lg-5"
											label="Tipo de Layout"
											name={'tipoLayout'}
											options={this.state.tiposLayout}
											selectedValue={this.state.tipoLayout}
											valueChange={this.changeTipoLayout} />

									<TextInput 
										id="layoutPesquisa"
										label="Layout"
										type="text"
										maxLength="30"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeLayout} />

									<TextInput 
										id="codigoIdentificadorPesquisa"
										label="Código Identificador"
										type="text"
										maxLength="30"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeCodigoIdentificador} />

									<ButtonCrud 
										showIncluir={false} 
										showAlterar={false}
										showConsultar={true}
										label="Limpar"
										consultar={this.props.template.consultar(this.state.codigo, this.state.descricao, this.state.tipoLayout, this.state.layout, this.state.codigoIdentificador)}
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