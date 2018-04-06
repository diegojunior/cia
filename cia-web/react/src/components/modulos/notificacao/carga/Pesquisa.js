import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import DatePicker from '../../../ui/date/DatePicker';
import FormValidator from '../../../ui/form/FormValidator';
import ButtonCrud from '../../../ui/button/ButtonCrud';
import SelectInput from '../../../ui/input/SelectInput';
import ConfiguracaoServico from '../../../../shared/services/ConfiguracaoServico';
import {constants} from '../../../../shared/util/Constants';

export default class NotificacaoCargaPesquisa extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	data :  null,
			sistema : '',
			servico: null,
			sistemas : constants.getSistemasEnum(),
			servicos: []
		}

		this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);

	    this.changeData = this.changeData.bind(this);
		this.changeSistema = this.changeSistema.bind(this);
		this.changeServico = this.changeServico.bind(this);
	}

	componentWillUpdate (nextProps, nextState) {
		if (!nextProps.template.isPesquisaAtiva()) {
			nextState.data = null;
			nextState.sistema = '';
			nextState.servico = null;
			nextState.servicos = [];
			this.refs.formValidator.clearValues();
		}
	}

	consultar () {
		return (event) => {
			event.preventDefault();

			let sistemaConstant = this.state.sistema != null ? constants.getConstantSistemasPorCodigo(this.state.sistema.codigo): null;

			this.props.template.consultar(this.state.data, sistemaConstant, this.state.servico);
		}
	}

	limpar () {		
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
			this.state.servicos = [];
			let newState = update(this.state, {data: {$set: null}, sistema: {$set: ''}, servico: {$set: ''}});
			this.setState(newState);

		}
	}

	changeData (value) {
		let newState = update(this.state, {data: {$set: value}});
		this.setState(newState);
	}

	changeSistema (value) {
		
		if(value != null && value != '') {
		
			this.state.sistema = constants.getSistemasPorCodigo(value);
			
			let constantSistema = constants.getConstantSistemasPorCodigo(this.state.sistema.codigo);

			ConfiguracaoServico
					.getBy(constantSistema)
					.then (data => {

						var newState = update(this.state, {servicos: {$set: data}});

						this.setState(newState);
					}).catch(error => {
						this.props.template.infoMessage(error);
					});
		} else {
			
			this.state.servicos = [];
			let newState = update(this.state, {sistema: {$set: ''}});

			this.setState(newState);
		}
	}

	changeServico (value) {
		let newState = update(this.state, {servico: {$set: value}});
		this.setState(newState);
	}
	
   clear () {
   		this.state.servicos = [];
		let newState = update(this.state, {data: {$set: null}, sistema: {$set: ''}, servico: {$set: ''}, servicos: []});
		this.setState(newState);
	}
	
    render () {
        return (

        	<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Notificação de Carga - Pesquisa
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
										id="data"
										label="Data"
										className="col-sm-5 col-md-5 col-lg-5"
										todayButtonLabel="Hoje"
										dateFormat="DD/MM/YYYY" 
										valueChange={this.changeData} 
										showError={false} />

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
										id="idServicos"
										label="Serviços" 
										className="col-sm-5 col-md-5 col-lg-5"
										name={'servico'}
										options={this.state.servicos} 
										valueChange={this.changeServico}
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