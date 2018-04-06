import React, { Component } from 'react';
import Datatable from '../../../ui/datatable/Datatable';
import CrudComponent from '../../../../shared/infra/CrudComponent';
import Pesquisa from './Pesquisa';
import Detalhe from './Detalhe';
import NotificacaoCargaService from '../../../../shared/services/NotificacaoCarga'
import {constants} from '../../../../shared/util/Constants';

export default class Template extends CrudComponent {
	
	constructor(props) {
	    super(props);
		this.consultar = this.consultar.bind(this);
	}
	
	consultar (data, sistema, servico) {

		NotificacaoCargaService
			.search(data, sistema, servico)
				.then (data => {
					if (data.length == 0) {
						this.infoMessage('Nenhuma Notificação encontrada!');
					}
					this.setState({
						list: data
					});
				}).catch (error => {
					this.errorMessage(error);	
				});
		
	}

	// overrride
	changeLineIndex (index) {        
        let elemento = this.selecionar(index);
        this.setState({
            elementoSelecionado: elemento, 
            renderDatatable: false,
            renderPesquisa: false,
            renderCadastro: false,
			renderDetalhe: true,
			showDatatable: false,
            clear: true});
    }
	
    render () {	

		var displayDatatable = {'display': this.state.showDatatable == undefined || this.state.showDatatable ? '' : 'none'};

        return (
			<div className="cia-animation-fadein">
				<div style={this.state.showHidePesquisa}>
					<Pesquisa template={this} />
				</div>

				<div style={this.state.showHideDetalhe}>
					<Detalhe template={this} />
				</div>

				<div style={displayDatatable} className="panel panel-listagem" id="listPanel">
					<div className="panel-heading">
						Listagem
					</div>
					<div className="panel-body">
						<div className="dataTable_wrapper">

							<Datatable
								id="idDatatableNotificacaoCarga"
								header={["Data", "Sistema", "Status"]} 
								data={this.state.list}
								metaData={["carga.data", "carga.sistema.nome", "carga.status.nome"]}
								disableCheckboxColumn={false} 
								render={this.state.renderDatatable}
								checkElement={this.checkElement}
								changeLineIndex={this.changeLineIndex} 
								rowIndex={this.state.rowIndex}
								show={this.state.showDatatable}/>
						</div>

					</div>
				</div>

				<CrudComponent />

			</div>
		);
    }
}