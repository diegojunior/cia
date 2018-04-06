import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../ui/form/FormValidator';
import TextInput from '../../../ui/input/TextInput';
import SelectInput from '../../../ui/input/SelectInput';
import ButtonCrud from '../../../ui/button/ButtonCrud';
import ConfiguracaoServicoService from '../../../../shared/services/ConfiguracaoServico';
import AddRemoveMultiplesInputs from '../../../ui/input/dynamic/AddRemoveMultiplesInputs';
import {constants} from '../../../../shared/util/Constants';
import './Style.css';

export default class Cadastro extends Component {
	
	constructor(props) {
		super(props);

		this.state = {
				configuracaoServico: {id: null, codigo: '', descricao: '', sistema: '', servico: '', tipoServico: '', campos: []},
				configuracaoServicoSelecionado: {id: null, codigo: '', descricao: '', sistema: '', servico: '', tipoServico: '', campos: []},
				
				sistemas: [],
				servicos: [],
				tiposServicos: [],
				
				showErrors: false,

				fieldsConfig: []
			};

		this.incluir = this.incluir.bind(this);
		this.alterar = this.alterar.bind(this);
		this.limpar = this.limpar.bind(this);

		this.refreshItens = this.refreshItens.bind(this);
		
		this.changeCodigo = this.changeCodigo.bind(this);
		this.changeDescricao = this.changeDescricao.bind(this);
		this.changeSistema = this.changeSistema.bind(this);
		this.changeServico = this.changeServico.bind(this);
		this.changeTipoServico = this.changeTipoServico.bind(this);

		this.showPesquisa = this.showPesquisa.bind(this);
	}

	componentWillMount () {
		this.state.sistemas = constants.getSistemasEnum();
		this.state.servicos = constants.getServicosEnum();
		this.state.tiposServicos = constants.getTiposServicosEnum();

		this.state.fieldsConfig = [
			{	key:'codigo', 
				label:'Campo', 
				type: 'text', 
				initialValue: '', 
				placeholder: "", 
				required: true, 
				maxLength: '30',
				styleColumnHeader: {'width': '25%'},
				styleColumn: {'width': '25%'}
			},
			{	key:'label', 
				label:'Label', 
				type: 'text', 
				initialValue: '', 
				placeholder: "", 
				required: false, 
				maxLength: '30',
				styleColumnHeader: {'width': '25%'},
				styleColumn: {'width': '23%'}
			}
		];
	}

	componentWillReceiveProps (nextProps) {
		
		if (nextProps.template.state.elementoSelecionado && 
			
			nextProps.template.state.elementoSelecionado.id && 
			nextProps.template.state.elementoSelecionado.id != '') {

			this.refs.formValidator.clearValues();
			
			this.setState({configuracaoServico: nextProps.template.state.elementoSelecionado, 
						   configuracaoServicoSelecionado: nextProps.template.state.elementoSelecionado});

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

		let configuracaoServicoClear = {id: null, codigo: '', descricao: '', sistema: '', servico: '', tipoServico: '', campos: []};
		let newState = update(this.state, {configuracaoServico: {$set: configuracaoServicoClear}, 
			configuracaoServicoSelecionado: {$set: configuracaoServicoClear}, showErrors: {$set: false}});
		this.setState(newState);
	}

	incluir () {
		
		return (event) => {
			event.preventDefault();

			this.refreshItens();

			this.setState({showErrors: true});

			if(this.refs.formValidator.isValid()) {
				ConfiguracaoServicoService
					.incluir(this.state.configuracaoServico)
						.then (data => {
							this.props.template.successMessage('Configuração Incluída com Sucesso.');
							this.props.template.addDatatableElement(data);
							this.props.template.clear();
						}).catch(error => {
							this.props.template.errorMessage(error);
						});
			}
		}
	}

	alterar () {

		return (event) => {
			event.preventDefault();

			this.refreshItens();

			this.state.configuracaoServicoSelecionado = {id: null, codigo: '', descricao: '', sistema: '', servico: '', tipoServico: '', campos: []};

			this.setState({showErrors: true});

			if(this.refs.formValidator.isValid()) {
				ConfiguracaoServicoService
					.alterar(this.state.configuracaoServico)
						.then(data => {
							this.props.template.successMessage('Configuração Alterada com Sucesso.');
							this.props.template.alterDatatableElement(data);
							this.props.template.clear();
							this.clear();	
						}).catch(error => {
							this.props.template.errorMessage(error);	
						});	
			}
		}
	}

	changeCodigo (value) {
		let newState = update(this.state, {configuracaoServico: {codigo: {$set: value}}});
		this.setState(newState);
	}

	changeDescricao (value) {
		let newState = update(this.state, {configuracaoServico: {descricao: {$set: value}}});
		this.setState(newState);
	}

	changeSistema (value) {
		if(value != null && value != '') {
			var newState = update(this.state, {configuracaoServico: {sistema: {$set: constants.getSistemasPorCodigo(value)}}});
			this.setState(newState);
		} else {
			var newState = update(this.state, {configuracaoServico: {sistema: {$set: ''}}});
			this.setState(newState);
		}
	}

	changeServico (value) {
		if(value != null && value != '') {
			var newState = update(this.state, {configuracaoServico: {servico: {$set: constants.getServicoPorCodigo(value)}}});
			this.setState(newState);
		} else {
			var newState = update(this.state, {configuracaoServico: {servico: {$set: ''}}});
			this.setState(newState);
		}
	}

	changeTipoServico (value) {
		if(value != null && value != '') {
			var newState = update(this.state, {configuracaoServico: {tipoServico: {$set: constants.getTipoServicoPorCodigo(value)}}});
			this.setState(newState);
		} else {
			var newState = update(this.state, {configuracaoServico: {tipoServico: {$set: ''}}});
			this.setState(newState);
		}
	}

	showInsertButtonStyle () {
		return this.state.configuracaoServico.id == null;
	}
	
	showUpdateButtonStyle () {
		return this.state.configuracaoServico.id != null;
	}

	readOnly () {
		return this.state.configuracaoServicoSelecionado.id != null;
	}

	refreshItens () {
		var fields = this.refs.addRemoveMultiplesInputs.getFields();
		
		var campos = [];
		for (var indexField in fields) {
			var field = fields[indexField];

			if (field['campo'+indexField] == '') {
				campos = [];
				break;
			}

			var campo = {
						 id: field['id'], 
						 codigo: field['codigo'+indexField], 
						 label: field['label'+indexField]
						};

			campos.push(campo);
		}

		this.state.configuracaoServico.campos = campos;
	}

    render () {
    	
    	return (

    		<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Configuração de Serviços - Cadastro
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
										id="codigo" 
										type="text"
										label="Código"
										className="col-sm-5 col-md-5 col-lg-5"
										maxLength="30"
										required={true}
										showError={this.state.showErrors}
										selectedValue={this.state.configuracaoServicoSelecionado.codigo}
										valueChange={this.changeCodigo} />

									<TextInput
										id="descricao" 
										type="text"
										label="Descrição"
										className="col-sm-5 col-md-5 col-lg-5"
										maxLength="60"
										showError={this.state.showErrors}
										selectedValue={this.state.configuracaoServicoSelecionado.descricao}
										valueChange={this.changeDescricao} />

									<SelectInput
										id="sistema" 
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Sistema"
										name={'sistema'}
										options={this.state.sistemas}
										required={true}
										valueChange={this.changeSistema}
										showError={this.state.showErrors} 
										selectedValue={this.state.configuracaoServicoSelecionado.sistema} />

									<SelectInput
										id="servico" 
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Serviço"
										name={'servico'}
										options={this.state.servicos}
										required={true}
										valueChange={this.changeServico}
										showError={this.state.showErrors} 
										selectedValue={this.state.configuracaoServicoSelecionado.servico} />

									<SelectInput
										id="tipoServico" 
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Tipo de Serviço"
										name={'tipoServico'}
										options={this.state.tiposServicos}
										required={true}
										valueChange={this.changeTipoServico}
										showError={this.state.showErrors} 
										selectedValue={this.state.configuracaoServicoSelecionado.tipoServico} />

									<AddRemoveMultiplesInputs 
										ref="addRemoveMultiplesInputs"
										fieldsConfig={this.state.fieldsConfig}
										valueChange={this.refreshItens} 
										selectedValue={this.state.configuracaoServicoSelecionado.campos}
										showError={this.state.showErrors} 
										className="control-group limitsAddRemoveMultiplesInputs"
										headerClassName="control-group header-table"
										readOnly={false}
										show={true} />
									
									<ButtonCrud 
										showIncluir={this.showInsertButtonStyle()} 
										showAlterar={this.showUpdateButtonStyle()}
										showConsultar={false}
										label="Novo"
										incluir={this.incluir()} 
										alterar={this.alterar()} 
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