import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import DatePicker from '../../../ui/date/DatePicker';
import FormValidator from '../../../ui/form/FormValidator';
import ButtonCrud from '../../../ui/button/ButtonCrud';
import SelectInput from '../../../ui/input/SelectInput';
import TextInput from '../../../ui/input/TextInput';
import {constants} from '../../../../shared/util/Constants';

export default class NotificacaoCargaPesquisa extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	data :  null,
			codigoPerfil : null,
			status: null,
			statusConciliacao : constants.getStatusConciliacoesEnum(),
			servicos: []
		}

		this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);

	    this.changeData = this.changeData.bind(this);
		this.changeCodigoPerfil = this.changeCodigoPerfil.bind(this);
		this.changeStatus = this.changeStatus.bind(this);
	}

	componentWillUpdate (nextProps, nextState) {
		if (!nextProps.template.isPesquisaAtiva()) {
			nextState.data = null;
			nextState.codigoPerfil = null;
			nextState.status = null;
			this.refs.formValidator.clearValues();
		}
	}

	consultar () {
		return (event) => {
			event.preventDefault();

			let statusConstant = this.state.status != null 
				? constants.getConstantStatusConciliacaoPorCodigo(this.state.status.codigo)
				: null;

			this.props.template.consultar(this.state.data, this.state.codigoPerfil, statusConstant);
		}
	}

	limpar () {		
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
			this.state.servicos = [];
			let newState = update(this.state, {data: {$set: null}, codigoPerfil: {$set: null}, status: {$set: null}});
			this.setState(newState);

		}
	}

	changeData (value) {
		let newState = update(this.state, {data: {$set: value}});
		this.setState(newState);
	}

	changeCodigoPerfil (value) {
		let newState = update(this.state, {codigoPerfil: {$set: value}});
		this.setState(newState);	
	}

	changeStatus (value) {
		
		if(value != null && value != '') {
		
			var newState = update(this.state, {status: {$set: constants.getStatusConciliacaoPorCodigo(value)}});
			this.setState(newState);
					
		} else {
			let newState = update(this.state, {status: {$set: null}});
			this.setState(newState);
		}
	}
	
   clear () {
   		this.state.servicos = [];
		let newState = update(this.state, {data: {$set: null}, codigoPerfil: {$set: null}, status: {$set: null}});
		this.setState(newState);
	}
	
    render () {
        return (

        	<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Notificação de Conciliação - Pesquisa
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
										id="dataConciliacao"
										label="Data"
										className="col-sm-5 col-md-5 col-lg-5"
										todayButtonLabel="Hoje"
										dateFormat="DD/MM/YYYY" 
										valueChange={this.changeData} 
										showError={false} />

									<TextInput 
										id="PerfilConciliacaoId"
										label="Perfil"
										type="text"
										maxLength="30"
										showError={false}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeCodigoPerfil} />	

									<SelectInput 
										id="statusConciliacaoId"
										placeholder="Selecione..."
										name={'status'}
										label="Status"
										options={this.state.statusConciliacao}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeStatus} />

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