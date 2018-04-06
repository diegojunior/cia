import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import DatePicker from '../../../ui/date/DatePicker';
import FormValidator from '../../../ui/form/FormValidator';
import ButtonCrud from '../../../ui/button/ButtonCrud';
import TextInput from '../../../ui/input/TextInput';
import SelectInput from '../../../ui/input/SelectInput';
import {constants} from '../../../../shared/util/Constants';
import './Style.css';

export default class ConciliacaoPesquisa extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	data :  '',
			perfil : '',
			status : '',
			statusConciliacao : constants.getStatusConciliacoesEnum()
		}

		this.limpar = this.limpar.bind(this);
		this.consultar = this.consultar.bind(this);

	    this.changeData = this.changeData.bind(this);
		this.changePerfil = this.changePerfil.bind(this);
		this.changeStatus = this.changeStatus.bind(this);

		this.showExecucao = this.showExecucao.bind(this);

	}

	componentWillUpdate (nextProps, nextState) {
		if (!nextProps.template.isPesquisaAtiva()) {
			nextState.data = '';
			nextState.perfil = '';
			nextState.status = '';
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
			
			var statusConstant = this.state.status != null && 
				this.state.status != ''? 
				constants.getConstantStatusConciliacaoPorCodigo(this.state.status.codigo): 
				null;

			this.props.template.consultar(this.state.data, this.state.perfil, statusConstant);
		}
	}

	limpar () {		
		
		return (event) => {
			event.preventDefault();

			this.refs.formValidator.clearValues();
		
			let newState = update(this.state, {data: {$set: ''}, perfil: {$set: ''}, status: {$set: ''}});
			this.setState(newState);

		}
	}

	changeData (value) {
		let newState = update(this.state, {data: {$set: value}});
		this.setState(newState);
	}

	changePerfil (value) {
		let newState = update(this.state, {perfil: {$set: value}});
		this.setState(newState);
	}

	changeStatus (value) {
		if (value != null && value != '') {
			let status = constants.getStatusConciliacaoPorCodigo(value);
			let newState = update(this.state, {status: {$set: status}});
			this.setState(newState);
		} else {
			let newState = update(this.state, {status: {$set: ''}});
			this.setState(newState);
		}
	}
	
   clear () {
		let newState = update(this.state, {data: {$set: ''}, perfil: {$set: ''}, status: {$set: ''}});
		this.setState(newState);
	}
	
    render(){
    	
        return (

        	<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Conciliação - Pesquisa
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
										dateFormat="DD/MM/YYYY" 
										valueChange={this.changeData} />

									<TextInput 
										id="perfil"
										name={'Perfil'}
										label="Perfil"
										className="col-sm-5 col-md-5 col-lg-5"
										showError={false}
										valueChange={this.changePerfil} />

									<SelectInput 
										id="status"
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