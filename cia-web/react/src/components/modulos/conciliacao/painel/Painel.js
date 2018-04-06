import { Link } from 'react-router';
import React, { Component } from 'react';
import update from 'immutability-helper';
import Button from '../../../ui/button/Button';
import TextInput from '../../../ui/input/TextInput';
import DatePicker from '../../../ui/date/DatePicker';
import SelectInput from '../../../ui/input/SelectInput';
import Datatable from './Datatable';
import FormValidator from '../../../ui/form/FormValidator';
import Div from '../../../ui/form/Div';
import {constants} from '../../../../shared/util/Constants';
import BaseComponent from '../../../../shared/infra/BaseComponent';
import PainelConciliacaoService from '../../../../shared/services/Painel';
import PerfilService from '../../../../shared/services/PerfilConciliacao';
import {sort} from '../../../../shared/util/Utils';

import './Style.css';

export default class PainelConciliacao extends BaseComponent {

	constructor(props) {
		super(props);

		this.state = {
				conciliacoes: [],
				dataFilter: null,
				monitoramento: {ok: 0, naoExecutados: 0, divergentes: 0, pendentes: 0, gravados: 0},
				rowIndex: -1,
				renderDatatable: true,
				showErrors: false
		};

		this.changeData = this.changeData.bind(this);
		this.consultar = this.consultar.bind(this);

		this.changeLineIndex = this.changeLineIndex.bind(this);

		this.consultarOK = this.consultarOK.bind(this);
		this.consultarDivergentes = this.consultarDivergentes.bind(this);
		this.consultarNaoExecutados = this.consultarNaoExecutados.bind(this);
		this.consultarPendentes = this.consultarPendentes.bind(this);

		this.refresh = this.refresh.bind(this);
	}
	
	changeLineIndex (index) {
    }

	consultar () {
		PainelConciliacaoService
			.getConciliacoes(this.state.dataFilter)
			.then (data => {
				this.state.conciliacoes = data;
				if (data.length > 0) {
					this.atualizaMonitoramento(data);
				} else {
					this.atualizaMonitoramento(null);
					this.infoMessage('Nenhum Perfil Encontrado.');
				}
				this.setState({renderDatatable: true});
			}).catch(error => {
				console.log(error);
				this.errorMessage('Ocorreu erro ao consultar os Perfis.');
			});
	}

	atualizaMonitoramento (dados) {
		var ok = 0;
		var naoExecutados = 0;
		var divergentes = 0;
		var pendentes = 0;
		var gravados = 0;
		if (dados != null) {
			var total = dados.length;
			for (var index in dados) {
				var conciliacao = dados[index];
				if (conciliacao.statusConciliacao.codigo == 'AN' || 
					conciliacao.statusConciliacao.codigo == 'ER') {
					pendentes++;
				}
				if (conciliacao.statusConciliacao.codigo == 'DI') {
					divergentes++;
				}
				if (conciliacao.statusConciliacao.codigo == 'OK') {
					ok++;
				}
				if (conciliacao.statusConciliacao.codigo == 'NE') {
					naoExecutados++;
				}
				if (conciliacao.statusConciliacao.codigo == 'GR' ||
					conciliacao.statusConciliacao.codigo == 'GD') {
					gravados++;
				}
			}
			ok = Math.floor(ok * 100 / total);
			naoExecutados = Math.floor(naoExecutados * 100 / total);
			divergentes = Math.floor(divergentes * 100 / total);
			pendentes = Math.floor(pendentes * 100 / total);
			gravados = Math.floor(gravados * 100 / total);
		}

		var isPossuiResto = (ok + naoExecutados + divergentes + pendentes + gravados) < 100;
		if (isPossuiResto) {
			ok = ok > naoExecutados && ok > divergentes && ok > pendentes && ok > gravados ? ok + 1 : ok;
			naoExecutados = naoExecutados > ok && naoExecutados > divergentes && naoExecutados > pendentes && naoExecutados > gravados ? naoExecutados + 1 : naoExecutados;
			divergentes = divergentes > ok && divergentes > naoExecutados && divergentes > pendentes && divergentes > gravados ? divergentes + 1 : divergentes;
			pendentes = pendentes > ok && pendentes > divergentes && pendentes > naoExecutados && pendentes > gravados? pendentes + 1 : pendentes;
			gravados = gravados > ok && gravados > divergentes && gravados > naoExecutados && gravados > pendentes ? gravados + 1 : gravados;
		}

		this.state.monitoramento = {ok: ok, naoExecutados: naoExecutados, 
			divergentes: divergentes, pendentes: pendentes, gravados: gravados};
	}

	getConciliacoes (){
		var conciliacoes = [];
		for (var index in this.state.selecionados) {
			var i = this.state.selecionados[index];
			conciliacoes.push(this.state.conciliacoes[i]);
		}
		return conciliacoes;
	}

	changeData (value) {
		this.state.dataFilter = value;
		this.atualizaMonitoramento(null);
		PainelConciliacaoService
			.getConciliacoes(this.state.dataFilter)
			.then (data => {
				this.state.conciliacoes = data;
				if (data.length > 0) {
					this.atualizaMonitoramento(data);
				} else {
					this.infoMessage('Nenhum Perfil Encontrado.');
				}
				this.setState({renderDatatable: true});
			}).catch(error => {
				console.log(error);
				this.errorMessage('Ocorreu erro ao consultar os Perfis.');
			});
		
	}

	consultarOK () {
		return (event) => {
			event.preventDefault();
			this.setState({showErrors: true});
			if(this.refs.formValidator.isValid()) {
				PainelConciliacaoService
					.getConciliacoes(this.state.dataFilter)
					.then (data => {
						this.state.conciliacoes = [];
						if (data.length > 0) {
							for (var index in data) {
								var conciliacao = data[index];
								if (conciliacao.statusConciliacao.codigo == 'OK') {
									this.state.conciliacoes.push(conciliacao);
								}
							}
						}
						this.setState({renderDatatable: true});
					}).catch(error => {
						console.log(error);
						this.errorMessage('Ocorreu erro ao consultar os Perfis.');
						this.setState({renderDatatable: true});
					});
			}
		}
	}

	consultarNaoExecutados () {
		return (event) => {
			event.preventDefault();
			this.setState({showErrors: true});
			if(this.refs.formValidator.isValid()) {
				PainelConciliacaoService
					.getConciliacoes(this.state.dataFilter)
					.then (data => {
						this.state.conciliacoes = [];
						if (data.length > 0) {
							for (var index in data) {
								var conciliacao = data[index];
								if (conciliacao.statusConciliacao.codigo == 'NE') {
									this.state.conciliacoes.push(conciliacao);
								}
							}
						}
						this.setState({renderDatatable: true});
					}).catch(error => {
						console.log(error);
						this.errorMessage('Ocorreu erro ao consultar os Perfis.');
						this.setState({renderDatatable: true});
					});
			}
		}
	}

	consultarDivergentes () {
		return (event) => {
			event.preventDefault();
			this.setState({showErrors: true});
			if(this.refs.formValidator.isValid()) {
				PainelConciliacaoService
					.getConciliacoes(this.state.dataFilter)
					.then (data => {
						this.state.conciliacoes = [];
						if (data.length > 0) {
							for (var index in data) {
								var conciliacao = data[index];
								if (conciliacao.statusConciliacao.codigo == 'DI') {
									this.state.conciliacoes.push(conciliacao);
								}
							}
						}
						this.setState({renderDatatable: true});
					}).catch(error => {
						console.log(error);
						this.errorMessage('Ocorreu erro ao consultar os Perfis.');
						this.setState({renderDatatable: true});
					});
			}
		}
	}

	consultarPendentes () {
		return (event) => {
			event.preventDefault();
			this.setState({showErrors: true});
			if(this.refs.formValidator.isValid()) {
				PainelConciliacaoService
					.getConciliacoes(this.state.dataFilter)
					.then (data => {
						this.state.conciliacoes = [];
						if (data.length > 0) {
							for (var index in data) {
								var conciliacao = data[index];
								if (conciliacao.statusConciliacao.codigo == 'AN' || 
									conciliacao.statusConciliacao.codigo == 'ER') {
									this.state.conciliacoes.push(conciliacao);
								}
							}
						}
						this.setState({renderDatatable: true});
					}).catch(error => {
						console.log(error);
						this.errorMessage('Ocorreu erro ao consultar os Perfis.');
						this.setState({renderDatatable: true});
					});
			}
		}
	}

	consultarGravados () {
		return (event) => {
			event.preventDefault();
			this.setState({showErrors: true});
			if(this.refs.formValidator.isValid()) {
				PainelConciliacaoService
					.getConciliacoes(this.state.dataFilter)
					.then (data => {
						this.state.conciliacoes = [];
						if (data.length > 0) {
							for (var index in data) {
								var conciliacao = data[index];
								if (conciliacao.statusConciliacao.codigo == 'GR' || 
									conciliacao.statusConciliacao.codigo == 'GD') {
									this.state.conciliacoes.push(conciliacao);
								}
							}
						}
						this.setState({renderDatatable: true});
					}).catch(error => {
						console.log(error);
						this.errorMessage('Ocorreu erro ao consultar os Perfis.');
						this.setState({renderDatatable: true});
					});
			}
		}
	}

	refresh () {
		return (event) => {
			event.preventDefault();
			this.setState({showErrors: true});
			if(this.refs.formValidator.isValid()) {
				PainelConciliacaoService
				.getConciliacoes(this.state.dataFilter)
				.then (data => {
					this.state.conciliacoes = data;
					if (data.length > 0) {
						this.atualizaMonitoramento(data);
					} else {
						this.atualizaMonitoramento(null);
						this.infoMessage('Nenhum Perfil Encontrado.');
					}
					this.setState({renderDatatable: true});
				}).catch(error => {
					console.log(error);
					this.errorMessage('Ocorreu erro ao consultar os Perfis.');
				});
			}
		}
	}
	
    render(){
    	
        return (

        	<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Painel de Conciliação
    					</div>
    				</div>
					<div className="col-lg-12">
						<div className="page-header">
							<FormValidator ref="formValidator">
								<DatePicker
									id="data"
									label="Data"
									className="col-sm-3 col-md-3 col-lg-3"
									todayButtonLabel="Hoje"
									dateFormat="DD/MM/YYYY" 
									required={true}
									noRow={true}
									valueChange={this.changeData} 
									showError={this.state.showErrors} />
								<Div styleName={{'height' : '20px'}} />
								<Button
									className="btn btn-outline btn-social btn-default"
									id="btnRefresh"
									onClick={this.refresh()}
									classFigure="fa fa-retweet"
									label="Atualizar" />
							</FormValidator>
							<div className="row"><br /></div>
						</div>					
						<div className="row"><br /></div>

						<div className="col-custom-largura col-md-6 cia-animation-fadein">
							<div className="panel panel-primary">
								<div className="panel-heading">
									<div className="row">
										<div className="col-xs-3">
											<i className="fa fa-exclamation-circle fa-5x"></i>
										</div>
										<div className="col-xs-9 text-right">
											<div className="huge">{this.state.monitoramento.naoExecutados + '%'}</div>
											<div>Não Executados</div>
										</div>
									</div>
								</div>
								<a href="#" onClick={this.consultarNaoExecutados()}>
									<div className="panel-footer">
										<span className="pull-left">Consultar</span>
										<span className="pull-right"><i className="fa fa-arrow-circle-down"></i></span>
										<div className="clearfix"></div>
									</div>
								</a>
							</div>
						</div>

						<div className="col-custom-largura col-md-6 cia-animation-fadein">
							<div className="panel panel-grey">
								<div className="panel-heading">
									<div className="row">
										<div className="col-xs-3">
											<i className="fa fa-ellipsis-h fa-5x"></i>
										</div>
										<div className="col-xs-9 text-right">
											<div className="huge">{this.state.monitoramento.pendentes + '%'}</div>
											<div>Pendentes</div>
										</div>
									</div>
								</div>
								<a href="#" onClick={this.consultarPendentes()}>
									<div className="panel-footer">
										<span className="pull-left">Consultar</span>
										<span className="pull-right"><i className="fa fa-arrow-circle-down"></i></span>
										<div className="clearfix"></div>
									</div>
								</a>
							</div>
						</div>

						<div className="col-custom-largura col-md-6 cia-animation-fadein">
							<div className="panel panel-red">
								<div className="panel-heading">
									<div className="row">
										<div className="col-xs-3">
											<i className="fa fa-times fa-5x"></i>
										</div>
										<div className="col-xs-9 text-right">
											<div className="huge">{this.state.monitoramento.divergentes + '%'}</div>
											<div>Divergentes</div>
										</div>
									</div>
								</div>
								<a href="#" onClick={this.consultarDivergentes()}>
									<div className="panel-footer">
										<span className="pull-left">Consultar</span>
										<span className="pull-right"><i className="fa fa-arrow-circle-down"></i></span>
										<div className="clearfix"></div>
									</div>
								</a>
							</div>
						</div>

						<div className="col-custom-largura col-md-6 cia-animation-fadein">
							<div className="panel panel-green">
								<div className="panel-heading">
									<div className="row">
										<div className="col-xs-3">
											<i className="fa fa-check fa-5x"></i>
										</div>
										<div className="col-xs-9 text-right">
											<div className="huge">{this.state.monitoramento.ok + '%'}</div>
											<div>OK</div>
										</div>
									</div>
								</div>
								<a href="#" onClick={this.consultarOK()}>
									<div className="panel-footer">
										<span className="pull-left">Consultar</span>
										<span className="pull-right"><i className="fa fa-arrow-circle-down"></i></span>
										<div className="clearfix"></div>
									</div>
								</a>
							</div>
						</div>

						<div className="col-custom-largura col-md-6 cia-animation-fadein">
							<div className="panel panel-gold">
								<div className="panel-heading">
									<div className="row">
										<div className="col-xs-3">
											<i className="fa fa-floppy-o fa-5x"></i>
										</div>
										<div className="col-xs-9 text-right">
											<div className="huge">{this.state.monitoramento.gravados + '%'}</div>
											<div>Gravados</div>
										</div>
									</div>
								</div>
								<a href="#" onClick={this.consultarGravados()}>
									<div className="panel-footer">
										<span className="pull-left">Consultar</span>
										<span className="pull-right"><i className="fa fa-arrow-circle-down"></i></span>
										<div className="clearfix"></div>
									</div>
								</a>
							</div>
						</div>

						<br />
							<Datatable 
								id="dataTablePerfilsConciliacao"
								data={this.state.conciliacoes}
								render={this.state.renderDatatable}
								changeLineIndex={this.changeLineIndex}
								rowIndex={this.state.rowIndex} />
					</div>
    			</div>
				<BaseComponent />
    		</div>
        );
    }
}