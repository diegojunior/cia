import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import Picklist from '../../ui/picklist/Picklist';
import FormValidator from '../../ui/form/FormValidator';
import Span from '../../ui/form/Span';
import RadioInput from '../../ui/input/RadioInput';
import DatePicker from '../../ui/date/DatePicker';
import SelectInput from '../../ui/input/SelectInput';
import ModalNotificacao from '../../ui/modal/ModalNotificacao';
import Button from '../../ui/button/Button';
import CargaService from '../../../shared/services/Carga';
import ClienteService from '../../../shared/services/Cliente';
import GrupoService from '../../../shared/services/Grupo';
import {constants} from '../../../shared/util/Constants';
import './Style.css';

export default class CargaExecucao extends Component {

	constructor(props) {
		super(props);

		this.state = {				
				carga: {id: null, data: null, sistema: null, tipoExecucao: null, servicos: [], clientes: [], grupos: []},				
				
				sistemas: [],

				servicos: [],
				clientes: [],
				grupos: [],

				servicosOuTodos: [],
				opcaoServicoSelecionado: 0,

				clientesGruposOuTodos: [],
				opcaoClienteSelecionado: 0,

				filterClientes: '',
				filterGrupos: '',

				aguardar: false,

				showErrors: false
		};

		this.executar = this.executar.bind(this);
		this.limpar = this.limpar.bind(this);
		this.clear = this.clear.bind(this);

		this.changeData = this.changeData.bind(this);
		this.changeSistema = this.changeSistema.bind(this);

		this.onChangeServicos = this.onChangeServicos.bind(this);
		this.onChangeClientes = this.onChangeClientes.bind(this);
		this.onChangeGrupos = this.onChangeGrupos.bind(this);		
		
		this.onChangeCodigoClienteFilter = this.onChangeCodigoClienteFilter.bind(this);
		this.onChangeCodigoGrupoFilter = this.onChangeCodigoGrupoFilter.bind(this);

		this.onChangeServicosOuTodos = this.onChangeServicosOuTodos.bind(this);
		this.onChangeClientesGruposOuTodos = this.onChangeClientesGruposOuTodos.bind(this);

		this.showPesquisa = this.showPesquisa.bind(this);
	}

	componentWillMount () {
		this.state.sistemas = constants.getSistemasEnum();
		this.state.clientesGruposOuTodos = [{codigo: 'Clientes'}, {codigo: 'Grupos'}, {codigo: 'Todos'}];
		this.state.servicosOuTodos = [{codigo: 'Serviços'}, {codigo: 'Todos'}];
	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.template.state.clear){
			this.clear();
		}
	}
	
    showPesquisa () {
    	return (event) => {
    		event.preventDefault();
	    	this.props.template.clear();
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
		this.refs.servicosOuTodos.selecionar(0);
		this.refs.clientesGruposOuTodos.selecionar(0);
		this.state.opcaoClienteSelecionado = 0;
		this.state.opcaoServicoSelecionado = 0;
		this.refs.servicos.clearFilter();
		this.refs.clientes.clearFilter();
		this.refs.grupos.clearFilter();

		let cargaClear = {id: null, data: null, sistema: null, tipoExecucao: null, servicos: [], clientes: [], grupos: []};

		let newState = update(this.state, {carga: {$set: cargaClear},
										   servicos: {$set: []},
										   clientes: {$set: []},
										   grupos: {$set: []},
										   opcaoClienteSelecionado: {$set: 0},
										   opcaoServicoSelecionado: {$set: 0},
										   showErrors: {$set: false}});
		this.setState(newState);
	}

	executar () {
		return (event) => {
			event.preventDefault();
			this.setState({showErrors: true});

			if(this.isValid()) {
				this.changeLoadButton(true);

				this.state.carga.tipoExecucao =  constants.getTipoExecucaoCargaEnum()[this.state.opcaoClienteSelecionado];
				if (this.state.opcaoServicoSelecionado == 1) {
					this.state.carga.servicos = this.state.servicos;
				}

				CargaService
					.executar(this.state.carga)
						.then (data => {
							this.changeLoadButton(false);
							this.props.template.successMessage('Carga Executada com Sucesso.');
							this.props.template.clear();
							this.clear();
						}).catch(error => {
							this.changeLoadButton(false);
							this.props.template.errorMessage(error);
						});
			}
		}
	}

	changeLoadButton (value) {
		this.setState({aguardar: value});
	}

	isValid () {
		var isValid = this.state.opcaoServicoSelecionado == 0 && !$.isEmptyObject(this.state.carga.servicos);

		if (isValid) {
			if (this.state.opcaoClienteSelecionado == 0 && !$.isEmptyObject(this.state.carga.clientes)){
				return this.refs.formValidator.isValid();
			} else if (this.state.opcaoClienteSelecionado == 1 && !$.isEmptyObject(this.state.carga.grupos)) {
				return this.refs.formValidator.isValid();
			}
		}

		return !$.isEmptyObject(this.state.carga.data) && !$.isEmptyObject(this.state.carga.sistema);
	}

	changeData (value) {
		let newState = update(this.state, {carga: {data: {$set: value}}});
		this.setState(newState);
	}

	changeSistema (value) {
		this.refs.servicosOuTodos.selecionar(0);
		this.refs.clientesGruposOuTodos.selecionar(0);
		this.state.opcaoClienteSelecionado = 0;
		this.state.opcaoServicoSelecionado = 0;
		this.refs.servicos.clearFilter();
		this.refs.clientes.clearFilter();
		this.refs.grupos.clearFilter();

		if(value != null && value != '') {
			this.state.carga.sistema = constants.getSistemasPorCodigo(value);
			
			let constantSistema = constants.getConstantSistemasPorCodigo(this.state.carga.sistema.codigo);
			let codigo = '';
			CargaService
					.loadServicosAndClientesAndGrupos(constantSistema, codigo)
					.then (response => {
						var newState = update(this.state, {servicos: {$set: response.servicos},
						clientes: {$set: response.clientes}, grupos: {$set: response.grupos}});

						this.setState(newState);
					}).catch(error => {
						this.props.template.infoMessage(error);
					});
		} else {
			this.state.servicos = [];
			this.state.clientes = [];
			this.state.grupos = [];

			this.state.carga.servicos = [];
			this.state.carga.clientes = [];

			var newState = update(this.state, {carga: {sistema: {$set: ''}}});
			this.setState(newState);
		}
	}

	onChangeServicos(options) {
		var newState = update(this.state, {carga: {servicos: {$set: options}}});
		this.setState(newState);
	}

	onChangeClientes(options) {
		this.state.carga.grupos = [];
		var newState = update(this.state, {carga: {clientes: {$set: options}}});
		this.setState(newState);
	}

	onChangeGrupos(options) {
		this.state.carga.clientes = [];
		var newState = update(this.state, {carga: {grupos: {$set: options}}});
		this.setState(newState);
	}

	onChangeCodigoClienteFilter (value) {
		if (this.state.carga.sistema != null) {
			let constantSistema = constants.getConstantSistemasPorCodigo(this.state.carga.sistema.codigo);
			ClienteService.getBy(constantSistema, value, 0, 10)
				.then (data => {
					
					var newState = update(this.state, {clientes: {$set: data}});
					this.setState(newState);

				}).catch(error => {
					console.log("ERROR ONFILTER CHANGE ", error);
					this.props.template.errorMessage(error);
				});
		}
	}

	onChangeCodigoGrupoFilter (value) {
		if (this.state.carga.sistema != null) {
			let constantSistema = constants.getConstantSistemasPorCodigo(this.state.carga.sistema.codigo);
			GrupoService.getBy(constantSistema, value, 0, 10)
				.then (data => {
					
					var newState = update(this.state, {grupos: {$set: response}});
					this.setState(newState);

				}).catch(error => {
					console.log("ERROR ONFILTER CHANGE ", error);
					this.props.template.errorMessage(error);
				});
		}
	}

	onChangeServicosOuTodos (value) {
		this.setServicosOuTodos(value);
		this.setClientesOuGrupos(this.state.opcaoClienteSelecionado);
		this.setState({opcaoServicoSelecionado : value});
	}

	onChangeClientesGruposOuTodos (value) {
		this.setClientesOuGrupos(value);
		this.setServicosOuTodos(this.state.opcaoServicoSelecionado);
		this.setState({opcaoClienteSelecionado : value});
	}

	setServicosOuTodos (value) {
		if (value == 1) {
			this.refs.servicos.clearFilter();
			this.refs.servicos.clear();
		}
	}

	setClientesOuGrupos (value) {
		this.state.carga.tipoExecucao = constants.getTipoExecucaoCargaEnum()[value];
		if (value == 0) {
			this.state.carga.grupos = [];
			this.refs.grupos.clearFilter();
			this.refs.grupos.clear();
		} else if (value == 1) {
			this.state.carga.clientes = [];
			this.refs.clientes.clearFilter();
			this.refs.clientes.clear();
		} else if (value == 2) {
			this.refs.clientes.clearFilter();
			this.refs.clientes.clear();
			this.refs.grupos.clearFilter();
			this.refs.grupos.clear();
			this.state.carga.clientes = [];
			this.state.carga.grupos = [];
		}
	}
	
    render(){
    	
        return (

        	<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Carga - Execução
    					</div>
    				</div>
    				
					<div className="col-lg-12">
						<div className="navigationAlign">
							<button type="button" className="btn btn-social btn-twitter" id="renderPesquisa" onClick={this.showPesquisa()}><i className="fa fa-long-arrow-left"></i>Voltar</button>
						</div>
						
						<div className="panel panel-primary" id="cadPanel">
							<div className="panel-heading">
								Execução
							</div>
							<div className="panel-body">

								<FormValidator ref="formValidator">

									<DatePicker
										id="data"
										label="Data"
										className="col-sm-5 col-md-5 col-lg-5"
										todayButtonLabel="Hoje"
										required={true}
										valueChange={this.changeData} 
										showError={this.state.showErrors} />

									<SelectInput
										id="sistema" 
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Sistema"
										name={'sistema'}
										options={this.state.sistemas}
										required={true}
										valueChange={this.changeSistema}
										showError={this.state.showErrors} />

									<RadioInput 
										id="servicosOuTodos"
										ref="servicosOuTodos"
										className="col-sm-5 col-md-5 col-lg-5"
										required={true}
										options={this.state.servicosOuTodos}
										onChange={this.onChangeServicosOuTodos} />

									<Picklist 
										id="idServicos"
										ref="servicos"
										className="col-sm-5 col-md-5 col-lg-5"
										labelValidator={"Serviços"}
										options={this.state.servicos} 
										required={true}
										show={this.state.opcaoServicoSelecionado == 0}
										textProp="codigo"
										valueProp="id"
										onChange={this.onChangeServicos}
										showError={this.state.showErrors && this.state.opcaoServicoSelecionado == 0} />

									<Picklist 
										id="idTodosServicos"
										ref="todosServicos"
										className="col-sm-5 col-md-5 col-lg-5"
										options={[]} 
										required={false}
										show={this.state.opcaoServicoSelecionado == 1}
										readOnly={true}
										textProp="codigo"
										valueProp="idLegado"
										showError={this.state.showErrors && this.state.opcaoServicoSelecionado == 1} />

									<RadioInput 
										id="clientesGruposOuTodos"
										ref="clientesGruposOuTodos"
										className="col-sm-5 col-md-5 col-lg-5"
										required={true}
										options={this.state.clientesGruposOuTodos}
										onChange={this.onChangeClientesGruposOuTodos} />

									<Picklist 
										id="idClientes"
										ref="clientes"
										className="col-sm-5 col-md-5 col-lg-5"
										labelValidator={"Clientes"}
										options={this.state.clientes} 
										required={this.state.opcaoClienteSelecionado == 0}
										show={this.state.opcaoClienteSelecionado == 0}
										textProp="codigo"
										valueProp="idLegado"
										onChange={this.onChangeClientes}
										onFilterChange={this.onChangeCodigoClienteFilter}
										showError={this.state.showErrors && this.state.opcaoClienteSelecionado == 0} />

									<Picklist 
										id="idGrupos"
										ref="grupos"
										className="col-sm-5 col-md-5 col-lg-5"
										labelValidator={"Grupos"}
										options={this.state.grupos} 
										required={this.state.opcaoClienteSelecionado == 1}
										show={this.state.opcaoClienteSelecionado == 1}
										textProp="codigo"
										valueProp="idLegado"
										onChange={this.onChangeGrupos}
										onFilterChange={this.onChangeCodigoGrupoFilter}
										showError={this.state.showErrors && this.state.opcaoClienteSelecionado == 1} />

									<Picklist 
										id="idTodosClientes"
										ref="todosClientes"
										className="col-sm-5 col-md-5 col-lg-5"
										options={[]} 
										required={false}
										show={this.state.opcaoClienteSelecionado == 2}
										readOnly={true}
										textProp="codigo"
										valueProp="idLegado"
										showError={false} />

									<Button
										className="btn btn-social btn-success"
										id="btnExecutar"
										show={!this.state.aguardar}
										onClick={this.executar()}
										classFigure="fa fa-flash"
										label="Executar" />

									<Button
										className="btn btn-social btn-success"
										id="btnAguardar"
										disabled={true}
										show={this.state.aguardar}
										classFigure="fa fa-flash"
										label="Aguarde..." />

								</FormValidator>
								
							</div>
						</div>
					</div>
    			</div>
    		</div>
        );
    }
}