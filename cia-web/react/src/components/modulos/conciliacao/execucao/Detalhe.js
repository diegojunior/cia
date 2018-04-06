import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import {format} from '../../../../shared/util/Utils';
import TextInput from '../../../ui/input/TextInput';
import Button from '../../../ui/button/Button';
import DatatableConciliacao from './Datatable';
import ConciliacaoService from '../../../../shared/services/Conciliacao';
import Export from './Export'
import {constants} from '../../../../shared/util/Constants';
import {copyObject} from '../../../../shared/util/Utils';
import './Style.css';

export default class ConciliacaoDetalhe extends Component {

	constructor(props) {
		super(props);
		this.state = {conciliacao: {id: null, data: null, perfil: null, status: null, lote: null}, 
					  conciliacaoSelecionada: {id: null, data: null, perfil: null, status: null, lote: null}};
		
		this.showPesquisa = this.showPesquisa.bind(this);
		this.gravar = this.gravar.bind(this);
		this.disableGravar = this.disableGravar.bind(this);
		this.drawDatatable = this.drawDatatable.bind(this);

		this.gerarLote = this.gerarLote.bind(this);
		this.todos = this.todos.bind(this);
		this.somenteOK = this.somenteOK.bind(this);
		this.somenteDivergente = this.somenteDivergente.bind(this);
		this.somenteChaveNaoIdentificada = this.somenteChaveNaoIdentificada.bind(this);
	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.template.state.elementoSelecionado && 
			nextProps.template.state.elementoSelecionado.id && 
			nextProps.template.state.elementoSelecionado.id != '') {
			var data = nextProps.template.state.elementoSelecionado.data;
			var idPerfil = nextProps.template.state.elementoSelecionado.perfil.id;
			ConciliacaoService.buscaConciliacaoCompleta(data, idPerfil)
				.then (data => {

					var lote = copyObject(data.lote);
					var loteSelecionado = copyObject(data.lote);
					var unidades = this.listUnidades(lote);
					var unidadesSelecionada = this.listUnidades(loteSelecionado);

					var loteCopy = {id: lote.id, configuracaoPerfil: lote.configuracaoPerfil, unidades: unidades};
					var loteSelecionadoCopy = {id: loteSelecionado.id, configuracaoPerfil: loteSelecionado.configuracaoPerfil, unidades: unidadesSelecionada};
				
					var conciliacaoCopy = {id: data.id, data: data.data, perfil: data.perfil, status: data.status, lote: loteCopy};
					var conciliacaoSelecionadaCopy = {id: data.id, data: data.data, perfil: data.perfil, status: data.status, lote: loteSelecionadoCopy};
					this.setState({conciliacaoSelecionada: conciliacaoSelecionadaCopy, conciliacao: conciliacaoCopy});
				
				}).catch(error => {
					console.log('ERROR ', error);
					this.props.template.errorMessage('Ocorreu erro ao carregar a Conciliação');
				});
		}
	}

	drawDatatable (id) {
		return (event) => {
    		event.preventDefault();
			this.refs[id].draw(id);
		}
	}

    showPesquisa () {
    	return (event) => {
    		event.preventDefault();
			
			this.clear();
	    	this.props.template.clear();
	        this.props.template.showPesquisa();
    	}
	}

	clear() {
		var conciliacaoClear = {id: null, data: null, perfil: null, status: null, lote: null};
		
		this.refs["idDatatable0"].destroy("idDatatable0");
		
		this.setState({conciliacao: conciliacaoClear,
					   conciliacaoSelecionada: conciliacaoClear});
	}

	gravar () {
		return (event) => {
			event.preventDefault();

			if (this.state.conciliacao.id != null) {
				this.props.template.confirmMessage('Confirmar', 'Deseja gravar a Conciliação?', () => {
					ConciliacaoService
						.gravar(this.state.conciliacao)
						.then (data => {
							this.props.template.successMessage('Conciliação Gravada com Sucesso!');
							this.setState({conciliacao: data});
						}).catch (error => {
							this.props.template.errorMessage('Ocorreu erro ao Gravar a Conciliação.');
						})
				});
			}
		}
	}

	disableGravar () {
		return this.state.conciliacao.id == null ||
			   this.state.conciliacao.status.codigo == 'ER' ||
			   this.state.conciliacao.status.codigo == 'GR' || 
			   this.state.conciliacao.status.codigo == 'GD';
	}

	listUnidades (lote) {
		var unidadesOK = [];
		var unidadesDivergentes = [];
		var unidadesSomenteImportacao = [];
		var unidadesSomenteCarga = [];
		for (var indexUnidade in lote.unidades) {
			var unidade = lote.unidades[indexUnidade];

			var isOK = this.getIsOK(unidade);
			if (isOK) {
				unidadesOK.push(copyObject(unidade));
				continue;
			}

			var isDivergente = this.getIsDivergente(unidade);
			if (isDivergente) {
				unidadesDivergentes.push(copyObject(unidade));
				continue;
			}

			var isSomenteImportacao = this.getIsSomenteImportacao(unidade);
			if (isSomenteImportacao) {
				unidadesSomenteImportacao.push(copyObject(unidade));
				continue;
			}

			var isSomenteCarga = this.getIsSomenteCarga(unidade);
			if (isSomenteCarga) {
				unidadesSomenteCarga.push(copyObject(unidade));
			}
		}

		var unidades = [];
		for (var index in unidadesOK) {
			unidades.push(unidadesOK[index]);
		}
		for (var index in unidadesDivergentes) {
			unidades.push(unidadesDivergentes[index]);
		}
		for (var index in unidadesSomenteImportacao) {
			unidades.push(unidadesSomenteImportacao[index]);
		}
		for (var index in unidadesSomenteCarga) {
			unidades.push(unidadesSomenteCarga[index]);
		}
		return unidades;
	}

	todos () {
		return (event) => {
			event.preventDefault();
			
			var unidades = this.listUnidades(this.state.conciliacao.lote);
			this.state.conciliacaoSelecionada.lote.unidades = [];
			for (var indexUnidade in unidades) {
				this.state.conciliacaoSelecionada.lote.unidades.push(unidades[indexUnidade]);
			}

			this.forceUpdate();
		}
	}

	somenteOK () {
		return (event) => {
			event.preventDefault();

			var lote = this.state.conciliacao.lote;
			var unidades = [];
			for (var indexUnidade in lote.unidades) {
				var unidadeConciliacao = lote.unidades[indexUnidade];
				if (unidadeConciliacao.status.codigo == 'OK') {
					unidades.push(copyObject(unidadeConciliacao));
				}
			}
			this.state.conciliacaoSelecionada.lote.unidades = [];
			for (var indexUnidade in unidades) {
				this.state.conciliacaoSelecionada.lote.unidades.push(unidades[indexUnidade]);
			}
			
			this.forceUpdate();
		}
	}

	somenteDivergente () {
		return (event) => {
			event.preventDefault();
	
			var lote = this.state.conciliacao.lote;
			var unidades = [];
			for (var indexUnidade in lote.unidades) {
				var unidadeConciliacao = lote.unidades[indexUnidade];
				if (unidadeConciliacao.status.codigo == 'DI') {
					unidades.push(copyObject(unidadeConciliacao));
				}
			}
			this.state.conciliacaoSelecionada.lote.unidades = [];
			for (var indexUnidade in unidades) {
				this.state.conciliacaoSelecionada.lote.unidades.push(unidades[indexUnidade]);
			}

			this.forceUpdate();
		}
	}

	somenteChaveNaoIdentificada () {
		return (event) => {
			event.preventDefault();

			var lote = this.state.conciliacao.lote;
			var unidadesSomenteImportacao = [];
			var unidadesSomenteCarga = [];
			for (var indexUnidade in lote.unidades) {
				var unidadeConciliacao = lote.unidades[indexUnidade];
				if (unidadeConciliacao.status.codigo == 'CN') {
					var isSomenteImportacao = this.getIsSomenteImportacao(unidadeConciliacao);
					if (isSomenteImportacao) {
						unidadesSomenteImportacao.push(copyObject(unidadeConciliacao));
					} else {
						unidadesSomenteCarga.push(copyObject(unidadeConciliacao));
					}
				}
			}
			this.state.conciliacaoSelecionada.lote.unidades = [];
			for (var indexUnidade in unidadesSomenteImportacao) {
				this.state.conciliacaoSelecionada.lote.unidades.push(unidadesSomenteImportacao[indexUnidade]);
			}
			for (var indexUnidade in unidadesSomenteCarga) {
				this.state.conciliacaoSelecionada.lote.unidades.push(unidadesSomenteCarga[indexUnidade]);
			}

			this.forceUpdate();
		}
	}

	getIsSomenteImportacao (unidade) {
		if (unidade.status.codigo == 'CN') {
			for (var index in unidade.camposConciliaveis) {
				var campo = unidade.camposConciliaveis[index];
				if (campo.status.codigo == 'SI') {
					return true;
				}
			}
		}
		return false;
	}

	getIsSomenteCarga (unidade) {
		if (unidade.status.codigo == 'CN') {
			for (var index in unidade.camposConciliaveis) {
				var campo = unidade.camposConciliaveis[index];
				if (campo.status.codigo == 'SC') {
					return true;
				}
			}
		}
		return false;
	}

	getIsOK (unidade) {
		return unidade.status.codigo == 'OK'; 
	}

	getIsDivergente (unidade) {
		return unidade.status.codigo == 'DI'; 
	}

	gerarLote () {

		var lote = this.state.conciliacaoSelecionada.lote;
		var headerChave = this.getHeaderChave(lote.configuracaoPerfil.configuracoesCampos);
		var headerConciliaveis = this.getHeaderConciliaveis(lote.configuracaoPerfil.configuracoesCampos);
		var headerInformativos = this.getHeaderInformativos(lote.configuracaoPerfil.configuracoesCampos);

		var headerLote = lote.configuracaoPerfil.identificacao + " - " + lote.configuracaoPerfil.servico;

		var loteGerado = (<div className="panel panel-default cia-animation-fadein" key={0}>
					<div className="panel-heading">
						<a data-toggle="collapse" data-parent="#accordionConciliacao" href={"#conciliacao0"} aria-expanded={"true"} aria-controls={"conciliacao0"}>
							{headerLote}
						</a>
					</div>
					<div id={"conciliacao0"} className={"panel-collapse collapse in"} aria-expanded={"true"}>
						<div className="panel-body">
							<div className="col-lg-12">
								<div style={{'textAlign': 'right'}}>
									<Button
										className="btn btn-outline btn-social btn-default"
										id="btnDefault"
										onClick={this.todos()}
										classFigure="fa fa-retweet"
										label="Todos" />
									<div style={{'display': 'inline-block', 'width':'5px'}}></div>
									<Button
										className="btn btn-outline btn-social btn-success"
										id="btnOK"
										onClick={this.somenteOK()}
										classFigure="fa fa-check"
										label="OK" />
									<div style={{'display': 'inline-block', 'width':'5px'}}></div>
									<Button
										className="btn btn-outline btn-social btn-danger"
										id="btnDivergente"
										onClick={this.somenteDivergente()}
										classFigure="fa fa-times"
										label="Divergente" />
									<div style={{'display': 'inline-block', 'width':'5px'}}></div>
									<Button
										className="btn btn-outline btn-social btn-warning"
										id="btnChaveNaoIdentificada"
										onClick={this.somenteChaveNaoIdentificada()}
										classFigure="fa fa-key"
										label="Chave Não Identificada" />
								</div>
								<br />
								<DatatableConciliacao
									ref={"idDatatable0"}
									id={"idDatatable0"}
									headerChave={headerChave} 
									headerConciliaveis={headerConciliaveis} 
									headerInformativos={headerInformativos} 
									data={lote.unidades} />
							</div>
						</div>
					</div>
				</div>);

		return (<div className="row">
					<div className="col-lg-12">
						<div className="panel-group" id="accordionConciliacao">
							{loteGerado}
						</div>
					</div>
				</div>);
	}

	getHeaderChave (configuracoesCampos) {
		var header = [];
		if (configuracoesCampos) {
			for (var index in configuracoesCampos) {
				var campo = configuracoesCampos[index];
				if (campo.chave) {
					header.push({nome: campo.campoImportacao, isEquivalente: false});
					if(campo.campoEquivalente && campo.campoEquivalente.codigo != '') {
						header.push({nome: campo.campoEquivalente.label, isEquivalente: true});
					}
				}
			}
		}
		return header;
	}

	getHeaderConciliaveis (configuracoesCampos) {
		var header = [];
		if (configuracoesCampos) {
			for (var index in configuracoesCampos) {
				var campo = configuracoesCampos[index];
				if (campo.conciliavel) {
					if(campo.campoEquivalente && campo.campoEquivalente.codigo != '') {
						header.push({nome: campo.campoImportacao, campoEquivalente: campo.campoEquivalente.label});
					} else {
						header.push({nome: campo.campoImportacao});
					}
				}
			}
		}
		return header;
	}

	getHeaderInformativos (configuracoesCampos) {
		var header = [];
		if (configuracoesCampos) {
			for (var index in configuracoesCampos) {
				var campo = configuracoesCampos[index];
				if (campo.informativo) {
					header.push(campo.campoCarga.label);
				}
			}
		}
		return header;
	}

    render(){

		var data = this.state.conciliacao.data != null && this.state.conciliacao.data != '' ? format(this.state.conciliacao.data) : '';

		var sistema = this.state.conciliacao.perfil != null && this.state.conciliacao.perfil.identificacao != null ? 
					  this.state.conciliacao.perfil.identificacao.sistema.nome : '';

		var layout = this.state.conciliacao.perfil != null && this.state.conciliacao.perfil.identificacao != null ? 
					 this.state.conciliacao.perfil.identificacao.layout.codigo : '';

		var perfil = this.state.conciliacao.perfil != null && this.state.conciliacao.perfil.identificacao != null ? 
					 this.state.conciliacao.perfil.identificacao.codigo : '';

		var status = this.state.conciliacao.status != null ? this.state.conciliacao.status.nome : '';

		var lote = '';
		if (this.state.conciliacao.id != null) {
			if (this.state.conciliacao.status.codigo != 'ER') {
				lote = this.gerarLote();
			}
		} else {
			lote = <div className="alert alert-info" style={{'textAlign': 'center'}}>Por favor aguarde. Carregando...</div>;			
		}

	    return (

        	<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Conciliação - Detalhe
    					</div>
    				</div>

					<div className="col-lg-12">
						<div className="navigationAlign">
							<button type="button" className="btn btn-social btn-twitter" id="renderPesquisa" onClick={this.showPesquisa()}><i className="fa fa-long-arrow-left"></i>Voltar</button>
						</div>

						<div className="panel panel-primary" id="cadPanel">
							<div className="panel-heading">
								Detalhe
							</div>
							<div className="panel-body">

								<div>
									<table id="cabecalho" className="table table-bordered">
										<thead>
											<tr>
												<th>Data</th>
												<th>Sistema</th>
												<th>Layout</th>
												<th>Perfil</th>
												<th>Status</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>{data}</td>
												<td>{sistema}</td>
												<td>{layout}</td>
												<td>{perfil}</td>
												<td>{status}</td>
											</tr>
										</tbody>
									</table>
								</div>

								{lote}

								<div style={{'display': 'flex'}}>
									<Button
										className="btn btn-social btn-success"
										id="btnGravar"
										onClick={this.gravar()}
										disabled={this.disableGravar()}
										classFigure="fa fa-save"
										label="Gravar" />
									<span style={{'width':'5px'}} />
									<Export conciliacao={this.state.conciliacao}/>
								</div>
							</div>
						</div>
					</div>
    			</div>
    		</div>
        );
    }
}