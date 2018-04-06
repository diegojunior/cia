import React, { Component } from 'react';
import update from 'immutability-helper';
import Importacao from '../../../shared/services/Importacao';
import UploadFile from '../../ui/upload/UploadFile';
import SelectInput from '../../ui/input/SelectInput';
import DatePicker from '../../ui/date/DatePicker';
import Moment from 'moment';
import Button from '../../ui/button/Button';
import { Link } from 'react-router';
import {constants} from '../../../shared/util/Constants';
import './Style.css';

export default class ImportacaoDetalhe extends Component {

	constructor (props) {
		super(props);
		this.state = {
			importacaoSelecionado : {
				id: null, 
				dataImportacao: null, 
				sistema: '', 
				remetente: null, 
				agente: null, 
				layout: {codigo: '', tipoLayout: ''}, 
				tipoImportacao: '', 
				arquivo: {fileName: ''}}
		};

		this.showPesquisa = this.showPesquisa.bind(this);
		this.showExecucao = this.showExecucao.bind(this);
	}

	componentWillReceiveProps (nextProps) {

		if (nextProps.template.state.elementoSelecionado && 
			
			nextProps.template.state.elementoSelecionado.id && 
			nextProps.template.state.elementoSelecionado.id != '') {
			
			this.setState({importacaoSelecionado: nextProps.template.state.elementoSelecionado});
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

    render () {

    	return (
			<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Importação - Detalhe
    					</div>
    				</div>

    				<form role="form" name="form">
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
													<th>Tipo Layout</th>
													<th>Layout</th>
													<th>Sistema</th>
													<th>Agente</th>
													<th>Remetente</th>
													<th>Tipo de Importação</th>
													<th>Arquivo</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td>{Moment(this.state.importacaoSelecionado.dataImportacao).format('DD/MM/YYYY')}</td>
													<td>{this.state.importacaoSelecionado.layout.tipoLayout.codigo}</td>
													<td>{this.state.importacaoSelecionado.layout.codigo}</td>
													<td>{this.state.importacaoSelecionado.sistema.nome}</td>
													<td>{this.state.importacaoSelecionado.agente != null ? this.state.importacaoSelecionado.agente.codigo: ''}</td>
													<td>{this.state.importacaoSelecionado.remetente != null ? this.state.importacaoSelecionado.remetente.codigo: ''}</td>
													<td>{this.state.importacaoSelecionado.tipoImportacao.nome}</td>
													<td>{this.state.importacaoSelecionado.arquivo.fileName}</td>
												</tr>
											</tbody>
										</table>
									</div>

									<Button
										className="btn btn-social btn-default"
										id="btnNovo"
										onClick={this.showExecucao()}
										classFigure="glyphicon glyphicon-import"
										label="Nova Importação" />
		    						
		    					</div>
		    				</div>
	    				</div>
    				</form>
    			</div>
    		</div>
				
		);
	}
}