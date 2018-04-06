import React, { Component } from 'react';
import { Link } from 'react-router';
import LayoutXmlService from '../../../../../shared/services/LayoutXml';
import Wizard from '../../../../ui/form/Wizard';
import Identificacao from './Identificacao';
import Sessao from './Sessao';
import Confirmacao from './Confirmacao';
import {constants} from '../../../../../shared/util/Constants';
import './Style.css';

export default class Cadastro extends Component {

	constructor(props) {
		super(props);
		this.state = {layout: {identificacao: null, sessoes: null}};

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

	next (step1, step2) {
		
		this.setState({layout: 
			{identificacao: step1.identificacao,
			 sessoes: step2.sessoes}});
	}

	finish (step1, step2, step3) {
		var layoutXml = {
			id: null, 
			codigo: step3.layout.identificacao.codigo, 
			descricao: step3.layout.identificacao.descricao, 
			tagRaiz: step3.layout.identificacao.tagRaiz, 
			status: constants.getStatus()[0], 
			sessoes: step2.sessoes};
		return LayoutXmlService.incluir(layoutXml)
			.then (data => {
				this.props.template.successMessage('Layout XML incluído com Sucesso.');
				return data;
			}).catch (error => {
				this.props.template.errorMessage(error);
				return error;
			});
	}

	cancel () {
		this.refs.wizardLayoutXml.clear();
	}

	render(){
	    
		return (
			<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Layout XML - Cadastro
    					</div>
    				</div>
    				
					<div className="col-lg-12">
						<div className="navigationAlign">
							<button type="button" className="btn btn-social btn-twitter" id="renderPesquisa" onClick={this.showPesquisa()}><i className="fa fa-long-arrow-left"></i>Voltar</button>
						</div>

						<Wizard id="wizardLayoutXml"
							ref="wizardLayoutXml"
							next={this.next}
							finish={this.finish}
							cancel={this.cancel}>

							<Identificacao label="Identificação" 
								warningMessage={this.props.template.warningMessage}
								errorMessage={this.props.template.errorMessage}/>

							<Sessao label="Sessões do Layout XML"
								identificacao={this.state.layout.identificacao}
								infoMessage={this.props.template.infoMessage}
								warningMessage={this.props.template.warningMessage}
								successMessage={this.props.template.successMessage}
								errorMessage={this.props.template.errorMessage}
								confirmMessage={this.props.template.confirmMessage} />
							
							<Confirmacao label="Confirmação" 
								layout={this.state.layout} />

						</Wizard>

					</div>
    			</div>
    		</div>
		);
	}
}