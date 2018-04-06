import React, { Component } from 'react';
import Datatable from '../../../../ui/datatable/Datatable';
import CrudComponent from '../../../../../shared/infra/CrudComponent';
import LayoutPosicionalService from '../../../../../shared/services/LayoutPosicional';
import {constants} from '../../../../../shared/util/Constants';
import Pesquisa from './Pesquisa';
import Cadastro from './Cadastro';
import Detalhe from './Detalhe';

export default class Template extends CrudComponent {

	constructor(props) {
		super(props);

		this.consultar = this.consultar.bind(this);
		this.remover = this.remover.bind(this);
		this.clear = this.clear.bind(this);
	}
	
	consultar (codigo, descricao, codigoIdentificador, status) {
		LayoutPosicionalService.getBy(codigo, descricao, codigoIdentificador, status)
			.then (data => {
					if (data.length == 0) {
						this.infoMessage('Nenhum Layout Posicional Encontrado!');
					}
					this.setState({
						list: data
					});
				}).catch (error => {
					this.errorMessage(error);
				});
	}

	remover () {
		return (event) => {
			event.preventDefault();

			if (this.state.selectedList.length <= 0) {
				this.infoMessage('Nenhum Elemento Selecionado!');
				return;
			}

			this.confirmMessage('Exclusão', 'Deseja remover o(s) registro(s) selecionado(s)?', () => {
				LayoutPosicionalService.remover(this.state.selectedList)
					.then (data => {
						this.successMessage('Itens Removidos com Sucesso!');
						this.deleteDatatableElements(this.state.selectedList);
						this.clear();
					}).catch (error => {
						this.errorMessage(error);
					});
			});			
		}
	}

	// override
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
					<Cadastro template={this} />
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

							<Datatable id="datatableLayoutPosicional"
										header={["Código", "Descrição", "Status"]} 
										data={this.state.list}
										metaData={["codigo", "descricao", "status.nome"]}
										disableCheckboxColumn={false} 
										render={this.state.renderDatatable}
										checkElement={this.checkElement}
										changeLineIndex={this.changeLineIndex} 
										rowIndex={this.state.rowIndex}
										show={this.state.showDatatable} />
							</div>

							<button id="buttonRemover" type="submit" className="btn btn-social btn-danger" onClick={this.remover()}><i className="fa fa-trash-o"></i>Remover</button>

					</div>
				</div>

				<CrudComponent />

			</div>
		);
    }
}