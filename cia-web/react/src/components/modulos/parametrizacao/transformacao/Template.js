import React, { Component } from 'react';
import Datatable from '../../../ui/datatable/Datatable';
import CrudComponent from '../../../../shared/infra/CrudComponent';
import Pesquisa from './Pesquisa';
import Cadastro from './Cadastro';
import TransformacaoService from '../../../../shared/services/Transformacao'
import {constants} from '../../../../shared/util/Constants';

export default class Template extends CrudComponent {
	
	constructor(props) {
	    super(props);
		this.consultar = this.consultar.bind(this);
		this.remover = this.remover.bind(this);
	}
	
	consultar (tipoLayout, layout, sessao, campo, tipoTransformacao) {

		TransformacaoService
			.getBy(tipoLayout, layout, sessao, campo, tipoTransformacao)
				.then (data => {
					if (data.length == 0) {
						this.infoMessage('Nenhuma Transformação Encontrada!');
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
				TransformacaoService
					.excluir(this.state.selectedList)
					.then (response => {
						this.successMessage('Itens Removidos com Sucesso!');
						this.deleteDatatableElements(this.state.selectedList);
						this.clear();
					}).catch (error => {
						this.errorMessage(error);
					});
			});
			
		}
	}
	
    render () {	

        return (
			<div className="cia-animation-fadein">
				<div style={this.state.showHidePesquisa}>
					<Pesquisa template={this} />
				</div>
				
				<div style={this.state.showHideCadastro}>
					<Cadastro template={this} />
				</div>

				<div className="panel panel-listagem" id="listPanel">
					<div className="panel-heading">
						Listagem
					</div>
					<div className="panel-body">
						<div className="dataTable_wrapper">

							<Datatable
								id="idDatatable"
								header={["Tipo de Layout", "Layout", "Sessão", "Campo", "Tipo de Transformação"]} 
								data={this.state.list}
								metaData={["tipoLayout.nome", "layout.codigo", "sessao.nome", "campo.codigo", "tipoTransformacao.nome"]}
								disableCheckboxColumn={false} 
								render={this.state.renderDatatable}
								checkElement={this.checkElement}
								changeLineIndex={this.changeLineIndex} 
								rowIndex={this.state.rowIndex}/>
						</div>

						<button id="buttonRemover" type="submit" className="btn btn-social btn-danger" onClick={this.remover()}><i className="fa fa-trash-o"></i>Remover</button>

					</div>
				</div>

				<CrudComponent />

			</div>
		);
    }
}