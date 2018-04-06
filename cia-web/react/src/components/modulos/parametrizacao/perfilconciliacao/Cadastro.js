import React, { Component } from 'react';
import { Link } from 'react-router';
import PerfilConciliacaoService from '../../../../shared/services/PerfilConciliacao';
import Wizard from '../../../ui/form/Wizard';
import Identificacao from './Identificacao';
import ConfiguracaoConciliacao from './ConfiguracaoConciliacao';
import Regras from './Regras';
import Confirmacao from './Confirmacao';
import './Style.css';

export default class Cadastro extends Component {

	constructor(props) {
		super(props);
		this.state = {perfil: {identificacao: null, configuracao: null, regras: null}};

		this.showPesquisa = this.showPesquisa.bind(this);
		this.showCadastro = this.showCadastro.bind(this);

		this.next = this.next.bind(this);
		this.finish = this.finish.bind(this);
		this.cancel = this.cancel.bind(this);
	}

	showPesquisa () {
    	return (event) => {
    		event.preventDefault();

	    	this.props.template.clear();
	        this.props.template.showPesquisa();
    	}
	}

	showCadastro () {
    	return (event) => {
    		event.preventDefault();

	    	this.props.template.clear();
	        this.props.template.showCadastro();
    	}
	}

	next (step1, step2, step3) {
		
		this.setState({perfil: 
			{identificacao: step1.identificacao,
			 configuracao: step2,
			 regras: step3.regras}});
	}

	finish (step1, step2, step3, step4) {
		return PerfilConciliacaoService.incluir(step4.perfil)
			.then (data => {
				this.props.template.successMessage('Perfil de Conciliação incluído com Sucesso.');
				return data;
			}).catch (error => {
				this.props.template.errorMessage(error);
				return error;
			});
	}

	cancel () {
		this.refs.wizardPerfilConciliacao.clear();
	}

	render(){
	    
		return (
			<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Perfil de Conciliação - Cadastro
    					</div>
    				</div>
    				
					<div className="col-lg-12">
						<div className="navigationAlign">
							<button type="button" className="btn btn-social btn-twitter" id="renderPesquisa" onClick={this.showPesquisa()}><i className="fa fa-long-arrow-left"></i>Voltar</button>
						</div>

						<Wizard id="wizardPerfilConciliacao"
							ref="wizardPerfilConciliacao"
							numberOfSteps={4}
							next={this.next}
							finish={this.finish}
							cancel={this.cancel}>

							<Identificacao label="Identificação" 
								warningMessage={this.props.template.warningMessage}
								errorMessage={this.props.template.errorMessage}/>

							<ConfiguracaoConciliacao label="Configuração"
								identificacao={this.state.perfil.identificacao}
								infoMessage={this.props.template.infoMessage}
								warningMessage={this.props.template.warningMessage}
								successMessage={this.props.template.successMessage}
								errorMessage={this.props.template.errorMessage}
								confirmMessage={this.props.template.confirmMessage} />

							<Regras label="Regras"
								identificacao={this.state.perfil.identificacao}
								configuracao={this.state.perfil.configuracao}
								infoMessage={this.props.template.infoMessage}
								warningMessage={this.props.template.warningMessage}
								successMessage={this.props.template.successMessage}
								errorMessage={this.props.template.errorMessage} />
							
							<Confirmacao label="Confirmação" 
								perfil={this.state.perfil} />

						</Wizard>

					</div>
    			</div>
    		</div>
		);
	}
}