import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import moment from 'moment';
import Datatable from '../../../ui/datatable/Datatable';

export default class NotificacaoImportacaoDetalhe extends Component {
	
	constructor(props) {
		super(props);

		this.state = {
			importacaoComNotificacaoSelecionada: {notificacoes: [], importacao: {dataImportacao: '', sistema: '', tipoLayout: '', codigoLayout: '', layout: {tipoLayout: ''}}}
		};

		this.showMensagem = this.showMensagem.bind(this);

	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.template.state.elementoSelecionado && 
			nextProps.template.state.elementoSelecionado.importacao && 
			nextProps.template.state.elementoSelecionado.importacao.id) {
			
			this.setState({importacaoComNotificacaoSelecionada: nextProps.template.state.elementoSelecionado});
		}
    }

    showPesquisa () {
    	return (event) => {

    		event.preventDefault();
	    	
	    	this.props.template.clear();
	        
	        this.props.template.showPesquisa();

    	}
    }

	showMensagem (msg) {
		return (event) => {
			this.props.template.showNotificacao(msg);
		}
	}

	generateData () {

		let notificacoes = this.state.importacaoComNotificacaoSelecionada.notificacoes.map((notificacao, index) => {
			return (
				<tr key={index}>
					<td>{notificacao.status.nome}</td>
					<td>{moment(notificacao.data).format('DD/MM/YYYY HH:mm:ss')}</td>
					<td><button id={"buttonShow"+index} type="button" className="fa fa-pencil-square-o" onClick={this.showMensagem(notificacao.mensagem)}/></td>
				</tr>
			);
		});

		return (

			<div className="panel panel-default" key={this.state.importacaoComNotificacaoSelecionada.importacao.id}>
					<div className="panel-heading">
						<a data-toggle="collapse" data-parent="#accordion" href={"#"+this.state.importacaoComNotificacaoSelecionada.importacao.id} aria-expanded="true">
							{this.state.importacaoComNotificacaoSelecionada.importacao.layout.codigo}
						</a>								
					</div>
					<div id={this.state.importacaoComNotificacaoSelecionada.importacao.id} className="panel-collapse collapse in" aria-expanded="true">
						<div className="panel-body">
							<div>
								<table id="cabecalho" className="table table-bordered">
									<thead>
										<tr>
											<th>Status</th>
											<th>Data</th>
											<th>Mensagem</th>
										</tr>
									</thead>
									<tbody>
										{notificacoes}
									</tbody>	
								</table>
							</div>
						</div>
					</div>
				</div>
		);
	}

	

    render () {
    	
    	let data = this.generateData();

    	return (

    		<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Notificação da Importação - Detalhe
    					</div>
    				</div>
    				
					<div className="col-lg-12">
						<div className="navigationAlign">
							<button type="button" className="btn btn-social btn-twitter" id="renderPesquisaNotificacaoImportacao" onClick={this.showPesquisa()}><i className="fa fa-long-arrow-left"></i>Voltar</button>
						</div>
						
						<div className="panel panel-primary" id="cadPanelNotificacaoImportacao">
							<div className="panel-heading">
								Detalhe
							</div>
							<div className="panel-body">
								<div>
									<table id="cabecalhoNotificacaoImportacao" className="table table-bordered">
										<thead>
											<tr>
												<th>Data</th>
												<th>Sistema</th>
												<th>Tipo Layout</th>
												<th>Layout</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>{moment(this.state.importacaoComNotificacaoSelecionada.importacao.dataImportacao).format('DD/MM/YYYY')}</td>
												<td>{this.state.importacaoComNotificacaoSelecionada.importacao.sistema != null ? this.state.importacaoComNotificacaoSelecionada.importacao.sistema.nome : ''}</td>
												<td>{this.state.importacaoComNotificacaoSelecionada.importacao.layout.tipoLayout.nome}</td>
												<td>{this.state.importacaoComNotificacaoSelecionada.importacao.layout.codigo}</td>
											</tr>
										</tbody>
									</table>
								</div>

								{data}

							</div>
							
						</div>
					</div>
    			</div>
    		</div>
			
        );
    }
}