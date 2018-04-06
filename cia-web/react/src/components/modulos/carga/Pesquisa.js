import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import DatePicker from '../../ui/date/DatePicker';
import FormValidator from '../../ui/form/FormValidator';
import ButtonCrud from '../../ui/button/ButtonCrud';
import TextInput from '../../ui/input/TextInput';
import SelectInput from '../../ui/input/SelectInput';
import {constants} from '../../../shared/util/Constants';
import './Style.css';

export default class CargaPesquisa extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	data :  '',
			sistema : '',
			statusCarga : '',
			sistemas : constants.getSistemasEnum(),
			statusCargas : constants.getStatusCargasEnum()
		}

		this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);

	    this.changeData = this.changeData.bind(this);
		this.changeSistema = this.changeSistema.bind(this);
		this.changeStatusCarga = this.changeStatusCarga.bind(this);

		this.showExecucao = this.showExecucao.bind(this);

	}

	componentWillUpdate (nextProps, nextState) {
		if (!nextProps.template.isPesquisaAtiva()) {
			nextState.data = '';
			nextState.sistema = '';
			nextState.statusCarga = '';
			this.refs.formValidator.clearValues();
		}
	}

	showExecucao () {
		return (event) => {
			this.props.template.showCadastro(false);
		}
	}

	consultar () {
		return (event) => {
			event.preventDefault();

			var sistemaConstant = this.state.sistema != null && 
				this.state.sistema != ''? 
				constants.getConstantSistemasPorCodigo(this.state.sistema.codigo): 
				null;
			
			var statusCargaConstant = this.state.statusCarga != null && 
				this.state.statusCarga != ''? 
				constants.getConstantStatusCargaPorCodigo(this.state.statusCarga.codigo): 
				null;

			this.props.template.consultar(this.state.data, sistemaConstant, statusCargaConstant);
		}
	}

	limpar () {		
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
		
			let newState = update(this.state, {data: {$set: ''}, sistema: {$set: ''}, statusCarga: {$set: ''}});
			this.setState(newState);

		}
	}

	changeData (value) {
		let newState = update(this.state, {data: {$set: value}});
		this.setState(newState);
	}

	changeSistema (value) {
		if (value != null && value != '') {
			let sistema = constants.getSistemasPorCodigo(value);
			let newState = update(this.state, {sistema: {$set: sistema}});
			this.setState(newState);
		} else {
			let newState = update(this.state, {sistema: {$set: ''}});
			this.setState(newState);
		}
	}

	changeStatusCarga (value) {
		if (value != null && value != '') {
			let status = constants.getStatusCargaPorCodigo(value);
			let newState = update(this.state, {statusCarga: {$set: status}});
			this.setState(newState);
		} else {
			let newState = update(this.state, {statusCarga: {$set: ''}});
			this.setState(newState);
		}
	}
	
   clear () {
		let newState = update(this.state, {data: {$set: ''}, sistema: {$set: ''}, statusCarga: {$set: ''}});
		this.setState(newState);
	}
	
    render(){
    	
        return (

        	<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Carga - Pesquisa
    					</div>
    				</div>
    				
					<div className="col-lg-12">
						<div className="navigationAlign">
							<button type="button" className="btn btn-social btn-twitter" id="renderExecucao" onClick={this.showExecucao()}><i className="fa fa-flash"></i>Executar</button>
						</div>
					
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
										valueChange={this.changeData} />

									<SelectInput 
										id="sistema"
										placeholder="Selecione..."
										name={'Sistema'}
										label="Sistema"
										options={this.state.sistemas}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeSistema} />

									<SelectInput 
										id="statusCarga"
										placeholder="Selecione..."
										name={'statusCarga'}
										label="Status"
										options={this.state.statusCargas}
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changeStatusCarga} />

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