import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import moment from 'moment';
import Datatable from '../../../ui/datatable/Datatable';

export default class NotificacaoConciliacaoDetalhe extends Component {
	
	constructor(props) {
		super(props);

		this.state = {
			conciliacaoComNotificacaoSelecionada: {notificacaoLote: [], conciliacao: {data: '', perfil: {identificacao: {codigo: ''}}, status: ''}}
		};

		this.showMensagem = this.showMensagem.bind(this);

	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.template.state.elementoSelecionado && 
			nextProps.template.state.elementoSelecionado.conciliacao && 
			nextProps.template.state.elementoSelecionado.conciliacao.id) {
			
			this.setState({conciliacaoComNotificacaoSelecionada: nextProps.template.state.elementoSelecionado});
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

	generateRows () {

			return this.state.conciliacaoComNotificacaoSelecionada.notificacaoLote.map((notificacaoLoteConciliacao, indexLote) => {

				var notificacoes = notificacaoLoteConciliacao.notificacoes.map((notificacao, index) => {
					return	(
						<tr key={index}>
							<td>{notificacao.status.nome}</td>
							<td>{moment(notificacao.data).format('DD/MM/YYYY HH:mm:ss')}</td>
							<td><button id={"buttonShow"+index} type="button" className="fa fa-pencil-square-o" onClick={this.showMensagem(notificacao.mensagem)}/></td>
						</tr>); 
							
				});		

				return (<div className="panel panel-default" key={indexLote}>
							<div className="panel-heading">
								<a data-toggle="collapse" data-parent="#accordion" href={"#"+indexLote} aria-expanded="true">
									{notificacaoLoteConciliacao.lote.configuracaoPerfil.identificacao} - {notificacaoLoteConciliacao.lote.configuracaoPerfil.servico}

								</a>								
							</div>
							<div id={indexLote} className="panel-collapse collapse in" aria-expanded="true">
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
			});
	}

	

    render () {
    	
    	let rows = this.generateRows();

    	return (

    		<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Notificação da Conciliacao - Detalhe
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
												<th>Perfil</th>
												<th>Status</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>{moment(this.state.conciliacaoComNotificacaoSelecionada.conciliacao.data).format('DD/MM/YYYY')}</td>
												<td>{this.state.conciliacaoComNotificacaoSelecionada.conciliacao.perfil.identificacao.codigo}</td>
												<td>{this.state.conciliacaoComNotificacaoSelecionada.conciliacao.status.nome}</td>
											</tr>
										</tbody>
									</table>
								</div>

								{rows}

							</div>
							
						</div>
					</div>
    			</div>
    		</div>
			
        );
    }
}