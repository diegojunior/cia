import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import DatePicker from '../../../ui/date/DatePicker';
import FormValidator from '../../../ui/form/FormValidator';
import ButtonCrud from '../../../ui/button/ButtonCrud';
import SelectInput from '../../../ui/input/SelectInput';
import TextInput from '../../../ui/input/TextInput';
import ConfiguracaoServico from '../../../../shared/services/ConfiguracaoServico';
import {constants} from '../../../../shared/util/Constants';

export default class NotificacaoImportacaoPesquisa extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	data :  null,
			sistema : '',
			tipoLayout: null,
			codigoLayout: null,
			sistemas: constants.getSistemasEnum(),
			tiposLayout: constants.getTiposLayouts()
		}

		this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);

	    this.changeData = this.changeData.bind(this);
		this.changeSistema = this.changeSistema.bind(this);
		this.changeTipoLayout = this.changeTipoLayout.bind(this);
		this.changeCodigoLayout = this.changeCodigoLayout.bind(this);
	}

	componentWillUpdate (nextProps, nextState) {
		if (!nextProps.template.isPesquisaAtiva()) {
			nextState.data = null;
			nextState.sistema = '';
			nextState.tipoLayout = null;
			nextState.codigoLayout = null;
			this.refs.formValidator.clearValues();
		}
	}

	consultar () {
		return (event) => {
			event.preventDefault();

			let sistemaConstant = this.state.sistema != null ? constants.getConstantSistemasPorCodigo(this.state.sistema.codigo): null;

			let tipoLayoutConstant = this.state.tipoLayout != null ? constants.getConstantTipoLayoutPorCodigo(this.state.tipoLayout.codigo): null;

			this.props.template.consultar(this.state.data, sistemaConstant, tipoLayoutConstant, this.state.codigoLayout);
		}
	}

	limpar () {		
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
			let newState = update(this.state, {data: {$set: null}, sistema: {$set: ''}, tipoLayout: {$set: null}, codigoLayout: {$set: null}});
			this.setState(newState);

		}
	}

	changeData (value) {
		let newState = update(this.state, {data: {$set: value}});
		this.setState(newState);
	}

	changeSistema (value) {
		let newState = null;
		if(value != null && value != '') {
			
			newState = update(this.state, {sistema: {$set: constants.getSistemasPorCodigo(value)}});
			
		} else {
			
			newState = update(this.state, {sistema: {$set: ''}});

		}
		this.setState(newState);
	}

	changeTipoLayout (value) {
		let newState = null;
		if(value != null && value != '') {
			
			newState = update(this.state, {tipoLayout: {$set: constants.getTipoLayoutPorCodigo(value)}});
			
		} else {
			
			newState = update(this.state, {tipoLayout: {$set: ''}});
		}
		this.setState(newState);
	}

	changeCodigoLayout (value) {
		let newState = update(this.state, {codigoLayout: {$set: value}});
		this.setState(newState);
	}


	
	
   clear () {
   		this.state.servicos = [];
		let newState = update(this.state, {data: {$set: null}, sistema: {$set: ''}, tipoLayout: {$set: null}, codigoLayout: {$set: null}});
		this.setState(newState);
	}
	
    render () {
    	
        return (

        	<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Notificação de Importação - Pesquisa
    					</div>
    				</div>
    				
					<div className="col-lg-12">
					
						<div className="panel panel-primary" id="filterPanel">
							<div className="panel-heading">
								Pesquisa
							</div>
							<div className="panel-body">

								<FormValidator ref="formValidator">
									
									<DatePicker
										id="dataImportacao"
										label="Data"
										className="col-sm-5 col-md-5 col-lg-5"
										todayButtonLabel="Hoje"
										dateFormat="DD/MM/YYYY" 
										valueChange={this.changeData} 
										showError={false} />

									<SelectInput 
										id="sistemaImportacao"
										placeholder="Selecione..."
										name={'sistema'}
										label="Sistema"
										options={this.state.sistemas}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeSistema} />

									<SelectInput 
										id="idTipoLayout"
										label="Tipo Layout" 
										className="col-sm-5 col-md-5 col-lg-5"
										name={'tipoLayout'}
										options={this.state.tiposLayout} 
										valueChange={this.changeTipoLayout}
										showError={false} />

									<TextInput 
										id="layoutImportacaoId"
										label="Layout"
										type="text"
										maxLength="30"
										showError={false}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeCodigoLayout} />

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