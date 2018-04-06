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
			
			tiposLayouts: [],
			camposValidacoes: [],
			locaisValidacoes: [],
			
			tipoLayout: '',
			layout: '',
			campoValidacao: '',
			localValidacao: ''
	    };

	    this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);

		this.changeTipoLayout = this.changeTipoLayout.bind(this);
		this.changeLayout = this.changeLayout.bind(this);

		this.changeCampoValidacao = this.changeCampoValidacao.bind(this);
		this.changeLocalValidacao = this.changeLocalValidacao.bind(this);
		
	}

	componentWillMount () {
		this.state.tiposLayouts = constants.getTiposLayouts();
		this.state.camposValidacoes = constants.getCamposValidacoes();
		this.state.locaisValidacoes = constants.getLocaisValidacoes();
	}

	componentWillUpdate (nextProps, nextState) {
		if (nextProps.template.isCadastroAtivo()) {
			
			nextState.tipoLayout = '';
			nextState.campoValidacao = '';
			nextState.localValidacao = '';
			nextState.layout = '';

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
			var campoValidacaoConstant = this.state.campoValidacao != null && 
				this.state.campoValidacao != ''? 
				constants.getConstantCampoValidacaoPorCodigo(this.state.campoValidacao.codigo): 
				null;
			var localValidacaoConstant = this.state.localValidacao != null && 
				this.state.localValidacao != ''? 
				constants.getConstantLocalValidacaoPorCodigo(this.state.localValidacao.codigo): 
				null;
			
			this.props.template.consultar(tipoLayoutConstant, this.state.layout, 
				campoValidacaoConstant, localValidacaoConstant);
		}
	}

	limpar () {
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
		
			this.clear();

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

	changeCampoValidacao (value) {
		let campoValidacao = constants.getCampoValidacaoPorCodigo(value);

		let newState = update(this.state, {campoValidacao: {$set: campoValidacao}});
		this.setState(newState);
	}

	changeLocalValidacao (value) {
		let localValidacao = constants.getLocalValidacaoPorCodigo(value);

		let newState = update(this.state, {localValidacao: {$set: localValidacao}});
		this.setState(newState);
	}

   clear () {
		let newState = update(this.state, {tipoLayout: {$set: ''}, layout: {$set: ''}, 
			campoValidacao: {$set: ''}, localValidacao: {$set: ''}});
		this.setState(newState);
	}
	
    render () {		

        return (
        		
	    	<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Validação de Arquivo - Pesquisa
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
										valueChange={this.changeTipoLayout} 
										showError={false}/>

									<TextInput 
										id="layout"
										label="Layout"
										type="text"
										maxLength="30"
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeLayout}
										showError={false} />
									
									<SelectInput 
										id="campoValidacao"
										placeholder="Selecione..."
										name={'campoValidacao'}
										label="Campo de Validação"
										chave={true}
										options={this.state.camposValidacoes}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeCampoValidacao} 
										showError={false}/>
										
									<SelectInput 
										id="localValidacao"
										placeholder="Selecione..."
										name={'localValidacao'}
										label="Local de Validação"
										chave={true}
										options={this.state.locaisValidacoes}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeLocalValidacao}
										showError={this.state.showErrors}/>

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