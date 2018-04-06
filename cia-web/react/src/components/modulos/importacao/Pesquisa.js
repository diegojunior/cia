import React, { Component } from 'react';
import { Link } from 'react-router';
import FormValidator from '../../ui/form/FormValidator';
import ButtonCrud from '../../ui/button/ButtonCrud';
import DatePicker from '../../ui/date/DatePicker';
import TextInput from '../../ui/input/TextInput';
import SelectInput from '../../ui/input/SelectInput';
import Agente from '../../../shared/services/Agente';
import {constants} from '../../../shared/util/Constants';
import update from 'immutability-helper';
import './Style.css';

export default class ImportacaoPesquisa extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	dataReferencia :  '',
	    	sistema: '',
	    	corretora: '',
	    	tipoLayout: '',
	    	layout: '',
	    	sistemas: constants.getSistemasEnum(),
	    	tiposLayouts: constants.getTiposLayouts()	    	
	    }
	    this.limpar = this.limpar.bind(this);
	    this.handlerDataReferencia = this.handlerDataReferencia.bind(this);
	    this.handlerSistema = this.handlerSistema.bind(this);
	    this.handlerCorretora = this.handlerCorretora.bind(this);
	    this.handlerLayout = this.handlerLayout.bind(this);
	    this.handlerTipoLayout = this.handlerTipoLayout.bind(this);

	    this.showExecucao = this.showExecucao.bind(this);
	}

	componentWillUpdate (nextProps, nextState) {
		if (nextProps.template.isCadastroAtivo()) {
			nextState.dataReferencia = '';
			this.refs.formValidator.clearValues();
		}
	}

	limpar () {
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
		
			let newState = update(this.state, {dataReferencia: {$set: ''}});
			this.setState(newState);

		}
	}

	handlerDataReferencia (value) {
		let newState = update(this.state, {dataReferencia: {$set: value}});
		this.setState(newState);
	}

	handlerSistema (value) {
		let sistema = constants.getSistemasPorCodigo(value);
		let newState = update(this.state, {sistema: {$set: sistema}});
		this.setState(newState);
	}

	handlerCorretora (value) {
		let newState = update(this.state, {corretora: {$set: value}});
		this.setState(newState);
	}

	handlerTipoLayout (value) {
		let tipoLayout = constants.getTipoLayoutPorCodigo(value);
		let newState = update(this.state, {tipoLayout: {$set: tipoLayout}});
		this.setState(newState);
	}

	handlerLayout (value) {
		let newState = update(this.state, {layout: {$set: value}});
		this.setState(newState);
	}

	clear () {
		let newState = update(this.state, {dataReferencia: {$set: ''}});
		this.setState(newState);
	}

   getDataReferencia () {
   		return this.state.dataReferencia;
   }

   showExecucao () {
		return (event) => {
			this.props.template.showCadastro(false);
		}
	}
	
	render () {	
		return (

			<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Importação - Pesquisa
    					</div>
    				</div>
				
					<div className="col-lg-12">
					
						<div className="navigationAlign">
	    					<button type="button" className="btn btn-social btn-twitter" id="renderExecucao" onClick={this.showExecucao()}><i className="glyphicon glyphicon-import"></i>Importar</button>
	    				</div>
	
	    				<div className="panel panel-primary" id="filterPanel">

		        		    <div className="panel-heading">
		        		        Pesquisa
		        		    </div>
		        		    <div className="panel-body">

								<FormValidator ref="formValidator">

									<DatePicker
										id="dataId"
										label="Data"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										selectedValue={this.state.dataReferencia}
										valueChange={this.handlerDataReferencia} />

									<SelectInput
										id="sistemaId" 
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Sistema"
										showError={false}
										name={'sistema'}
										options={this.state.sistemas}
										valueChange={this.handlerSistema}/>

									<TextInput 
										id="corretora"
										label="Corretora"
										type="text"
										maxLength="30"
										showError={false}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.handlerCorretora} />

									<SelectInput
										id="tipoLayoutId" 
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Tipo de Layout"
										name={'tipoLayout'}
										options={this.state.tiposLayouts}
										valueChange={this.handlerTipoLayout}
										showError={false} />

									<TextInput 
										id="layoutId"
										label="Layout"
										type="text"
										maxLength="30"
										showError={false}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.handlerLayout} />

									<ButtonCrud 
										showIncluir={false} 
										showAlterar={false}
										showConsultar={true}
										label="Limpar"
										consultar={this.props.template.consultar(this.getDataReferencia(), this.state.sistema, this.state.corretora, this.state.tipoLayout, this.state.layout)}
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