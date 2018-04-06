import React, { Component } from 'react';
import { Link } from 'react-router';
import TextInput from '../../../ui/input/TextInput';
import Button from '../../../ui/button/Button';
import PerfilConciliacaoService from '../../../../shared/services/PerfilConciliacao';
import {constants} from '../../../../shared/util/Constants';
import './Style.css';

export default class Detalhe extends Component {

	constructor(props) {
		super(props);
		this.state = {
			perfil: {
				id: null, 
				identificacao: {
					codigo: '', 
					descricao: '', 
					sistema: {codigo: '', nome: ''},
					tipoLayout: {codigo: '', nome: ''},
					layout: '',
					status: {codigo: '', nome: ''}}, 
				configuracao: {configuracoesCampos: []},
				regras: []},
			servicos: []};
			
		this.generateConfiguracoes = this.generateConfiguracoes.bind(this);
		this.generateRegras = this.generateRegras.bind(this);
		this.clear = this.clear.bind(this);
		this.inativar = this.inativar.bind(this);

		this.showPesquisa = this.showPesquisa.bind(this);
		this.showCadastro = this.showCadastro.bind(this);
	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.template.state.elementoSelecionado && 
			nextProps.template.state.elementoSelecionado.id && 
			nextProps.template.state.elementoSelecionado.id != '') {
			this.setState({perfil: nextProps.template.state.elementoSelecionado});
		}
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
			this.props.template.showCadastro(false);
		}
	}

	ativar () {
		return (event) => {
			event.preventDefault();
			
			this.props.template.confirmMessage('Ativação', 'Deseja realmente Ativar o Perfil de Conciliação?', () => {
				PerfilConciliacaoService
					.ativar(this.state.perfil.id)
					.then (data => {
						this.props.template.alterDatatableElement(data);
						this.setState({perfil: data});
						this.props.template.successMessage('Perfil de Conciliação Ativado com Sucesso!');
					}).catch (error => {
						this.props.template.errorMessage(error);
					})
			})
			return;
		}
	}

	inativar () {
		return (event) => {
			event.preventDefault();
			
			this.props.template.confirmMessage('Inativação', 'Deseja realmente inativar o Perfil de Conciliação?', () => {
				PerfilConciliacaoService
					.inativar(this.state.perfil.id)
					.then (data => {
						this.props.template.alterDatatableElement(data);
						this.setState({perfil: data});
						this.props.template.successMessage('Perfil de Conciliação Inativado com Sucesso!');
					}).catch (error => {
						this.props.template.errorMessage(error);
					})
			})
			return;
		}
	}

	generateConfiguracoes () {
		
		var configuracoesCampos = this.state.perfil.configuracao.configuracoesCampos.map((configuracaoCampo, indexConfiguracaoCampo) => {
			var campoCarga = configuracaoCampo.campoCarga != null ? configuracaoCampo.campoCarga.label : '';
			var campoEquivalente = configuracaoCampo.campoEquivalente != null ? configuracaoCampo.campoEquivalente.label : '';
			return (<tr key={indexConfiguracaoCampo}>
						<td style={{'textAlign' : 'center'}}>{configuracaoCampo.chave ? <div className="fa fa-check"></div> : ''}</td>
						<td style={{'textAlign' : 'center'}}>{configuracaoCampo.conciliavel ? <div className="fa fa-check"></div> : ''}</td>
						<td style={{'textAlign' : 'center'}}>{configuracaoCampo.informativo ? <div className="fa fa-check"></div> : ''}</td>
						<td>{campoCarga}</td>
						<td>{configuracaoCampo.campoImportacao}</td>
						<td>{campoEquivalente}</td>
					</tr>)
		});

		return (<div className="row">
					<div className="col-lg-12">
						<div className="panel-group" id="accordionDetalhe">
							<div className="page-header" style={{fontWeight : "bold", fontSize : "15px"}}>
								Configurações
							</div>
							<table id="cabecalho" className="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>Chave</th>
										<th>Conciliável</th>
										<th>Informativo</th>
										<th>Campo Carga</th>
										<th>Campo Importação</th>
										<th>Campo Equivalente</th>
									</tr>
								</thead>
								<tbody>
									{configuracoesCampos}
								</tbody>
							</table>
						</div>
					</div>
				</div>);
	}

	generateRegras () {
		if (this.state.perfil.regras != null && this.state.perfil.regras.length > 0) {
			var regras = this.state.perfil.regras.map((regra, indexRegra) => {

				var modulo = regra.modulo.nome;
				var campo = regra.campoCarga != null ? regra.campoCarga.label : regra.campoImportacao;
				var condicao = regra.condicao.nome;
				var filtro = regra.filtro;
				return (<tr key={indexRegra}>
							<td>{modulo}</td>
							<td>{campo}</td>
							<td>{condicao}</td>
							<td>{filtro}</td>
						</tr>)
			});

			return (<div className="row">
						<div className="col-lg-12">
							<div className="page-header" style={{fontWeight : "bold", fontSize : "15px"}}>
								Regras
							</div>
							<table id="cabecalhoRegras" className="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>Módulo</th>
										<th>Campo</th>
										<th>Condição</th>
										<th>Filtro</th>
									</tr>
								</thead>
								<tbody>
									{regras}
								</tbody>
							</table>
						</div>
					</div>);
		}
		return null;
	}

	clear () {
		this.setState({
			perfil: {
				id: null, 
				identificacao: {
					codigo: '', 
					descricao: '', 
					sistema: {codigo: '', nome: ''},
					tipoLayout: {codigo: '', nome: ''},
					layout: '',
					status: {codigo: '', nome: ''}}, 
				configuracao: {
					configuracoesCampos: []},
				regras: []
			}
		});
	}

	render(){
		
		var rowsConfiguracoes = this.generateConfiguracoes();
		var rowsRegras = this.generateRegras();
		var consolidarDados = this.state.perfil.configuracao.consolidarDados ? 'Sim' : 'Não';
		return (
			
			<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Perfil de Conciliação - Detalhe
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
								<div className="page-header" style={{fontWeight : "bold", fontSize : "15px"}}>
									Identificação
								</div>
								<table id="cabecalho" className="table table-bordered">
									<thead>
										<tr>
											<th>Código</th>
											<th>Descrição</th>
											<th>Sistema</th>
											<th>Tipo do Layout</th>
											<th>Layout</th>
											<th>{this.state.perfil.configuracao.localizacaoCampos != null ? this.state.perfil.configuracao.localizacaoCampos.nome : ''}</th>
											<th>Serviço</th>
											<th>Consolidar Dados</th>
											<th>Status</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>{this.state.perfil.identificacao.codigo}</td>
											<td>{this.state.perfil.identificacao.descricao}</td>
											<td>{this.state.perfil.identificacao.sistema.nome}</td>
											<td>{this.state.perfil.identificacao.tipoLayout.nome}</td>
											<td>{this.state.perfil.identificacao.layout.codigo}</td>
											<td>{this.state.perfil.configuracao.identificacao}</td>
											<td>{this.state.perfil.configuracao.servico}</td>
											<td>{consolidarDados}</td>
											<td>{this.state.perfil.identificacao.status.nome}</td>
										</tr>
									</tbody>
								</table>

								{rowsConfiguracoes}
								{rowsRegras}

								<div className="sessaoBotoes">
									<button type="button" className={this.state.perfil.identificacao.status.codigo == 'ITV' ? 'displayButton' + ' btn btn-social btn-success': 'notDisplayButton'} id="btnAtivar" onClick={this.ativar()}><i className="fa fa-check"></i>Ativar</button>
									&nbsp;&nbsp;
									<button type="button" className={this.state.perfil.identificacao.status.codigo == 'ATV' ? 'displayButton' + ' btn btn-social btn-danger' : 'notDisplayButton'} id="btnInativar" onClick={this.inativar()}><i className="glyphicon glyphicon-ban-circle"></i>Inativar</button>
								</div>
								
							</div>
						</div>
					</div>
				</div>
			</div>
		);
	}
}