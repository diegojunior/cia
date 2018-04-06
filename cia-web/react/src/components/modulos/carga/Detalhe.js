import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import moment from 'moment';
import TextInput from '../../ui/input/TextInput';
import Button from '../../ui/button/Button';
import CargaService from '../../../shared/services/Carga';
import {constants} from '../../../shared/util/Constants';
import './Style.css';

export default class CargaDetalhe extends Component {

	constructor(props) {
		super(props);

		this.state = {
				cargaSelecionada: {id: null, data: '', sistema: '', status: '', lotes: []}
		};

		this.showPesquisa = this.showPesquisa.bind(this);
		this.showExecucao = this.showExecucao.bind(this);
	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.template.state.elementoSelecionado && 
			
			nextProps.template.state.elementoSelecionado.id && 
			nextProps.template.state.elementoSelecionado.id != '') {

			this.setState({cargaSelecionada: nextProps.template.state.elementoSelecionado});
		}
	}
	
    showPesquisa () {
    	return (event) => {

    		event.preventDefault();
	    	
	    	this.props.template.clear();
			
	        this.props.template.showPesquisa();
    	}
	}
	
	showExecucao () {
		return (event) => {
			this.props.template.showCadastro(false);
		}
	}

	generateRows () {

		var lotes = this.state.cargaSelecionada.lotes.map((lote, indexLote) => {
			return (<div className="panel panel-info" key={indexLote}>
						<div className="panel-heading">
							{lote.servico.servico.nome + ' - ' + lote.status.nome}
						</div>
					</div>)
		});

		return (<div className="row">
					<div className="col-lg-12">
						<div className="panel-group">
							{lotes}
						</div>
					</div>
				</div>);
	}

    render(){
		var rows = this.generateRows();
        return (
        	<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Carga - Detalhe
    					</div>
    				</div>

					<div className="col-lg-12">
						<div className="navigationAlign">
							<button id="renderPesquisa" type="button" className="btn btn-social btn-twitter" onClick={this.showPesquisa()}><i className="fa fa-long-arrow-left"></i>Voltar</button>
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
												<th>Status</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>{moment(this.state.cargaSelecionada.data).format('DD/MM/YYYY')}</td>
												<td>{this.state.cargaSelecionada.sistema.nome}</td>
												<td>{this.state.cargaSelecionada.status.nome}</td>
											</tr>
										</tbody>
									</table>
								</div>

								{rows}

								<div>
									<Button
										className="btn btn-social btn-default"
										id="btnLimpar"
										onClick={this.showExecucao()}
										classFigure="fa fa-flash"
										label="Nova Execução" />
								</div>		
							</div>

						</div>
					</div>
    			</div>
    		</div>
        );
    }
}