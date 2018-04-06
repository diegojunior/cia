import React, { Component } from 'react';
import Datatable from '../../ui/datatable/Datatable';
import CrudComponent from '../../../shared/infra/CrudComponent';
import Execucao from './Execucao';
import Pesquisa from './Pesquisa';
import Detalhe from './Detalhe';
import Importacao from '../../../shared/services/Importacao'
import {constants} from '../../../shared/util/Constants';

export default class Template extends CrudComponent {
	
	constructor(props) {
	    super(props);
		this.consultar = this.consultar.bind(this);
	}
	
	consultar (dataReferencia, sistema, corretora, tipoLayout, layout) {
		return (event) => {
			event.preventDefault();
			Importacao
				.getBy(dataReferencia, sistema, corretora, tipoLayout, layout)
					.then (data => {
						if (data.length == 0) {
							this.infoMessage('Não há importação executada!');
						}
						this.setState({
							list: data
						});
					}).catch (error => {
						this.errorMessage(error);	
					});
		}
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
				
				<div style={this.state.showHideCadastro}>
					<Execucao template={this} />
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
								id="idDatatable"
								header={["Data", "Sistema", "Layout", "Status"]} 
								data={this.state.list}
								metaData={["dataImportacao", "sistema.nome", "layout.codigo", "status.nome"]}
								disableCheckboxColumn={false} 
								render={this.state.renderDatatable}
								checkElement={this.checkElement}
								changeLineIndex={this.changeLineIndex} 
								rowIndex={this.state.rowIndex}/>
						</div>
					</div>
				</div>

				<CrudComponent />

			</div>
		);
    }
}