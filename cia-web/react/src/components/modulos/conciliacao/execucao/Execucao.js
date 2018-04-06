import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../ui/form/FormValidator';
import TextInput from '../../../ui/input/TextInput';
import DatePicker from '../../../ui/date/DatePicker';
import SelectInput from '../../../ui/input/SelectInput';
import Datatable from '../../../ui/datatable/Datatable';
import Button from '../../../ui/button/Button';
import ConciliacaoService from '../../../../shared/services/Conciliacao';
import PerfilService from '../../../../shared/services/PerfilConciliacao';
import {constants} from '../../../../shared/util/Constants';
import './Style.css';

export default class ConciliacaoExecucao extends Component {

	constructor(props) {
		super(props);

		this.state = {
				conciliacoes: [],
				selecionados: [],
				dataFilter: null,
				perfilFilter: null,
				perfis: [],
				rowIndex: -1,
				renderDatatable: true,
				showErrors: false,
				aguardar: false
		};

		this.consultar = this.consultar.bind(this);
		this.executar = this.executar.bind(this);
		this.clear = this.clear.bind(this);
		this.changeData = this.changeData.bind(this);
		this.changePerfil = this.changePerfil.bind(this);
		this.checkElement = this.checkElement.bind(this);
		this.checkAll = this.checkAll.bind(this);
		this.showPesquisa = this.showPesquisa.bind(this);
	}

	componentWillMount () {
		PerfilService
			.getAtivos()
			.then (data => {
				if (data.length > 0) {
					this.state.perfis = this.getPerfis(data);
				}
			}).catch(error => {
				this.props.template.errorMessage(error);
				this.state.perfis = [];
			});
	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.template.state.clear){
			this.clear();
		}
	}

	checkElement (index, checked) {
        if (checked) {
			this.state.selecionados.push(index);
        } else {
            for (var i in this.state.selecionados) {
				var element = this.state.selecionados[i];
				if(index == element) {
					this.state.selecionados.splice(i, 1);
					break;
                }
            }
		}
		return true;
	}
	
	checkAll (checked) {
		this.state.selecionados.length = 0;
        if (checked) {
			for (var index in this.state.conciliacoes) {
				this.state.selecionados.push(index);
			}
		}
		return true;
	}
	
	changeLoadButton (value) {
		this.setState({aguardar: value});
	}
	
    showPesquisa () {
    	return (event) => {
    		event.preventDefault();
	    	
	    	this.props.template.clear();
	        this.props.template.showPesquisa();
    	}
    }

	clear () {
		this.refs.formValidator.clearValues();
		this.refs.dataTablePerfilsConciliacao.clearCheckAll();
		this.setState({selecionados: [], conciliacoes: [], 
			rowIndex: -1, showErrors: false, renderDatatable: true});
	}

	consultar () {
		
		return (event) => {
			event.preventDefault();

			this.setState({showErrors: true});
			if(this.refs.formValidator.isValid()) {
				ConciliacaoService
					.consultarParaExecucao(this.state.dataFilter, this.state.perfilFilter)
					.then (data => {
						if (data.length > 0) {
							this.state.conciliacoes = data;
							this.setState({renderDatatable: true});
						} else {
							this.props.template.infoMessage('Nenhum Perfil encontrado.');
						}
					}).catch(error => {
						this.props.template.errorMessage(error);
					});
			}
		}
	}

	executar () {
		return (event) => {
			event.preventDefault();

			this.setState({showErrors: false, renderDatatable: false});
			var conciliacoes = this.getConciliacoes();
			if(this.isValid(conciliacoes)) {
				this.changeLoadButton(true);
				ConciliacaoService
				.executar(conciliacoes)
				.then (data => {
					this.changeLoadButton(false);
					this.props.template.successMessage('Conciliação(ões) Executada(s) com Sucesso.');
					this.clear();
				}).catch(error => {
					this.changeLoadButton(false);
					this.props.template.errorMessage(error);
				});
				
			}
		}
	}

	getConciliacoes (){
		var conciliacoes = [];
		for (var index in this.state.selecionados) {
			var i = this.state.selecionados[index];
			conciliacoes.push(this.state.conciliacoes[i]);
		}
		return conciliacoes;
	}

	isValid(conciliacoes) {
		if (conciliacoes.length == 0) {
			this.props.template.warningMessage('Nenhum elemento selecionado.');
			return false;
		}
		for (var index in conciliacoes) {
			var conciliacao = conciliacoes[index];
			if (conciliacao.carga == null) {
				this.props.template.warningMessage('Carga não executada.');
				return false;
			}
			if (conciliacao.importacao == null) {
				this.props.template.warningMessage('Importação não executada.');
				return false;
			}
		}
		return true;
	}

	changeData (value) {
		let newState = update(this.state, {dataFilter: {$set: value}});
		this.setState(newState);
	}
	
	changePerfil (value) {
		let newState = update(this.state, {perfilFilter: {$set: value}});
		this.setState(newState);
	}

	getPerfis(data) {
		var perfis = [];
		for (var index in data) {
			var perfil = data[index];
			perfis.push({id: perfil.id, codigo: perfil.identificacao.codigo});
		}
		return perfis;
	}
	
    render(){
    	
        return (

        	<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Conciliação - Execução
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
										dateFormat="DD/MM/YYYY" 
										required={true}
										valueChange={this.changeData} 
										showError={this.state.showErrors} />

									<SelectInput
										id="perfil" 
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Perfil"
										name={'Perfil'}
										options={this.state.perfis}
										valueChange={this.changePerfil}
										showError={this.state.showErrors} />

									<Button
										className="btn btn-social btn-primary"
										id="btnConsultar"
										onClick={this.consultar()}
										classFigure="fa fa-search"
										label="Consultar" />

									<Datatable 
										id="dataTablePerfilsConciliacao"
										ref="dataTablePerfilsConciliacao"
										header={["Perfil", "Layout", "Carga", "Importação"]} 
										data={this.state.conciliacoes}
										metaData={["perfil", "layout", "statusCarga.nome", "statusImportacao.nome"]}
										disableCheckboxColumn={false} 
										lengthChange={false}
										searching={false}
										render={this.state.renderDatatable}
										checkElement={this.checkElement}
										checkAll={this.checkAll}
										rowIndex={this.state.rowIndex} />

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