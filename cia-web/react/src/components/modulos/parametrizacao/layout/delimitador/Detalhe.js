import React, { Component } from 'react';
import { Link } from 'react-router';
import LayoutDelimitadorService from '../../../../../shared/services/LayoutDelimitador';
import './Style.css';

export default class Detalhe extends Component {

	constructor(props) {
		super(props);
		this.state = {layout: {identificacao: null, sessoes: []}};
			
		this.generateRows = this.generateRows.bind(this);
		this.clear = this.clear.bind(this);
		this.showPesquisa = this.showPesquisa.bind(this);
		this.showCadastro = this.showCadastro.bind(this);
		this.ativar = this.ativar.bind(this);
		this.inativar = this.inativar.bind(this);
	}

	componentWillReceiveProps(nextProps) {
		this.state.layout = {identificacao: null, sessoes: []};
		if (nextProps.template.state.elementoSelecionado != null) {
			this.state.layout.identificacao = {id: nextProps.template.state.elementoSelecionado.id,
											   codigo: nextProps.template.state.elementoSelecionado.codigo,
											   descricao: nextProps.template.state.elementoSelecionado.descricao,
											   tipoDelimitador: nextProps.template.state.elementoSelecionado.tipoDelimitador,
											   status: nextProps.template.state.elementoSelecionado.status};
			if (nextProps.template.state.elementoSelecionado.sessoes != null) {
				for (var index in nextProps.template.state.elementoSelecionado.sessoes) {
					var sessao = nextProps.template.state.elementoSelecionado.sessoes[index];
					this.state.layout.sessoes.push(sessao);
				}
			}
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
			
			this.props.template.confirmMessage('Ativação', 'Deseja realmente Ativar o Layout?', () => {
				LayoutDelimitadorService
					.ativar(this.state.layout.identificacao.id)
					.then (data => {
						this.props.template.alterDatatableElement(data);
						this.state.layout = {identificacao: null, sessoes: []};
						this.state.layout.identificacao = {id: data.id,
											   codigo: data.codigo,
											   descricao: data.descricao,
											   tipoDelimitador: data.tipoDelimitador,
											   status: data.status};
						if (data.sessoes != null) {
							for (var index in data.sessoes) {
								var sessao = data.sessoes[index];
								this.state.layout.sessoes.push(sessao);
							}
						}
						this.forceUpdate();
						this.props.template.successMessage('Layout Ativado com Sucesso!');
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
			
			this.props.template.confirmMessage('Inativação', 'Deseja realmente inativar o Layout?', () => {
				LayoutDelimitadorService
					.inativar(this.state.layout.identificacao.id)
					.then (data => {
					this.props.template.alterDatatableElement(data);
						this.state.layout = {identificacao: null, sessoes: []};
						this.state.layout.identificacao = {id: data.id,
											   codigo: data.codigo,
											   descricao: data.descricao,
											   tipoDelimitador: data.tipoDelimitador,
											   status: data.status};
						if (data.sessoes != null) {
							for (var index in data.sessoes) {
								var sessao = data.sessoes[index];
								this.state.layout.sessoes.push(sessao);
							}
						}
						this.forceUpdate();
						this.props.template.successMessage('Layout Inativado com Sucesso!');
					}).catch (error => {
						this.props.template.errorMessage(error);
					})
			})
			return;
		}
	}

	generateRows () {

		var sessoes = this.state.layout.sessoes.map((sessao, index) => {

			var campos = sessao.campos.map((campo, indexCampo) => {

				var dominio = campo.dominio != null ? campo.dominio.codigo : '';
				return (<tr key={indexCampo}>
							<td>{dominio}</td>
							<td>{campo.descricao}</td>
							<td>{campo.pattern}</td>
						</tr>)

			});

			return (<div className="panel panel-default" key={index}>
						<div className="panel-heading">
							<a data-toggle="collapse" data-parent="#accordionDelimitadorDetalhe" href={"#accordionDelimitadorDetalhe"+index} aria-expanded="true" className="collapsed">
								{sessao.nome + ' - '}  {sessao.codigo == null ? '' : sessao.codigo}
							</a>								
						</div>
						<div id={"accordionDelimitadorDetalhe"+index} className="panel-collapse collapse in" aria-expanded="true">
							<div className="panel-body">
								<div>
									<table id="cabecalho" className="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th>Campo</th>
												<th>Descrição</th>
												<th>Formato</th>
											</tr>
										</thead>
										<tbody>
											{campos}
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>)
		});

		return (<div className="row">
					<div className="col-lg-12">
						<div className="panel-group" id="accordionDelimitadorDetalhe">
							{sessoes}
						</div>
					</div>
				</div>);
	}

	clear () {
		this.setState({layout: {identificacao: null, sessoes: []}});
	}

	render(){

		var codigo = '';
		var descricao = '';
		var tipoDelimitador = {codigo: '', nome: ''};
		var status = {codigo: '', nome: ''};
		var rows = '';

		var identificacao = this.state.layout.identificacao;
		if (identificacao && identificacao != null) {
			codigo = identificacao.codigo ? identificacao.codigo : '';
			descricao = identificacao.descricao ? identificacao.descricao : '';
			tipoDelimitador = identificacao.tipoDelimitador ? identificacao.tipoDelimitador : {codigo: '', nome: ''};
			status = identificacao.status ? identificacao.status : {codigo: '', nome: ''};
		}

		if(this.state.layout.sessoes != null) {
			rows = this.generateRows();
		}

		return (
			<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Layout Delimitador - Detalhe
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
								<table id="cabecalho" className="table table-bordered">
									<thead>
										<tr>
											<th>Código <div className="fa fa-key" style={{color: '#c3ae09'}}></div></th>
											<th>Descrição</th>
											<th>Tipo Delimitador</th>
											<th>Status</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>{codigo}</td>
											<td>{descricao}</td>
											<td>{tipoDelimitador.nome}</td>
											<td>{status.nome}</td>
										</tr>
									</tbody>
								</table>

								{rows}

								<div className="sessaoBotoes">
									<button type="button" className={status != null && status.codigo == 'ITV' ? 'displayButton' + ' btn btn-social btn-success': 'notDisplayButton'} id="btnAtivar" onClick={this.ativar()}><i className="fa fa-check"></i>Ativar</button>
									&nbsp;&nbsp;
									<button type="button" className={status != null && status.codigo == 'ATV' ? 'displayButton' + ' btn btn-social btn-danger' : 'notDisplayButton'} id="btnInativar" onClick={this.inativar()}><i className="glyphicon glyphicon-ban-circle"></i>Inativar</button>
								</div>
							</div>
						</div>
					</div>	
				</div>
			</div>
		);
	}
}