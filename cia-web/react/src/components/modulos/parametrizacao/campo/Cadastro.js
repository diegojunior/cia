import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../ui/form/FormValidator';
import TextInput from '../../../ui/input/TextInput';
import SelectInput from '../../../ui/input/SelectInput';
import ButtonCrud from '../../../ui/button/ButtonCrud';
import DominioService from '../../../../shared/services/Dominio';
import {constants} from '../../../../shared/util/Constants';
import './Style.css';

export default class Cadastro extends Component {
	
	constructor(props) {
		super(props);

		this.state = {
				dominio: {id: null, codigo: '', tipo: {}, status: ''},
				dominioSelecionado : {id: null, codigo: '', tipo: {}, status: ''},
				tipos: constants.getTiposValoresDominioEnum(),
				showErrors: false
			};

		this.incluir = this.incluir.bind(this);
		this.limpar = this.limpar.bind(this);
		this.changeTipoChanged = this.changeTipoChanged.bind(this);
		this.changeCodigoFieldChanged = this.changeCodigoFieldChanged.bind(this);
		this.showPesquisa = this.showPesquisa.bind(this);

	}

	componentWillReceiveProps (nextProps) {
		
		if (nextProps.template.state.elementoSelecionado && 
			
			nextProps.template.state.elementoSelecionado.id && 
			nextProps.template.state.elementoSelecionado.id != '') {

			this.refs.formValidator.clearValues();
			
			this.setState({dominio: nextProps.template.state.elementoSelecionado, 
						   dominioSelecionado: nextProps.template.state.elementoSelecionado});

		} else if (nextProps.template.state.clear){
			this.clear();

		}
    }

    showPesquisa () {
    	return (event) => {

    		event.preventDefault();
	    	
	    	this.props.template.clear();
			
			this.clear ();
	        
	        this.props.template.showPesquisa();

    	}
    }

    limpar () {		
		
		return (event) => {
			event.preventDefault();

			this.props.template.clear();

			this.clear();
		}
	}

	clear () {

		this.refs.formValidator.clearValues();

		let dominioClear = {id: null, codigo: '', tipo: {}};
		let newState = update(this.state, {dominio: {$set: dominioClear}, 
			dominioSelecionado: {$set: dominioClear}, showErrors: {$set: false}});
		this.setState(newState);
	}

	incluir () {
		
		return (event) => {
			event.preventDefault();

			this.setState({showErrors: true});

			if(this.refs.formValidator.isValid()) {
				DominioService
					.incluir(this.state.dominio)
						.then (data => {
							this.props.template.successMessage('Campo Incluído com Sucesso.');
							this.props.template.addDatatableElement(data);
							this.props.template.clear();
						}).catch(error => {
							this.props.template.errorMessage(error);
						});
				

				
			}
		}
	}

	changeCodigoFieldChanged (value) {
		let newState = update(this.state, {dominio: {codigo: {$set: value}}});
		this.setState(newState);
	}
	
	changeTipoChanged (value) {
		let tipo = constants.getTipoValorDominioPorCodigo(value);
      	let newState = update(this.state, {dominio: {tipo: {$set: tipo}}});
		this.setState(newState);
	}

	showInsertButtonStyle () {
		return this.state.dominio.id == null;
	}
	
	showUpdateButtonStyle () {
		return this.state.dominio.id != null;
	}

	readOnly () {
		return this.state.dominioSelecionado.id != null;
	}

    render () {
    	
    	return (

    		<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Campo - Cadastro
						</div>
					</div>
					<div className="col-lg-12">
						<div className="navigationAlign">
							<button type="button" className="btn btn-social btn-twitter" id="renderPesquisa" onClick={this.showPesquisa()}><i className="fa fa-long-arrow-left"></i>Voltar</button>
						</div>

						<div className="panel panel-primary" id="cadPanel">
							<div className="panel-heading">
								Cadastro
							</div>
							<div className="panel-body">

								<FormValidator ref="formValidator">
									
									<TextInput
										id="codigoId" 
										type="text"
										label="Código"
										className="col-sm-5 col-md-5 col-lg-5"
										maxLength="30"
										required={true}
										chave={true}
										readOnly={this.readOnly()}
										showError={this.state.showErrors}
										selectedValue={this.state.dominioSelecionado.codigo}
										valueChange={this.changeCodigoFieldChanged} />
										
									<SelectInput
										id="tipoId" 
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Tipo"
										name={'tipo'}
										options={this.state.tipos}
										selectedValue={this.state.dominioSelecionado.tipo}
										valueChange={this.changeTipoChanged}
										required={true}
										readOnly={this.readOnly()}
										showError={this.state.showErrors} />
									
									<ButtonCrud 
										showIncluir={this.showInsertButtonStyle()} 
										showAlterar={false}
										showConsultar={false}
										label="Novo"
										incluir={this.incluir()} 
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