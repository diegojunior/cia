import React, { Component } from 'react';
import {format} from '../../../../shared/util/Utils';
import './Style.css';

let values = [];
let indexValue = 0;
let colors;
let indexColors = 0;
let columnsSize = 0;
export default class Datatable extends Component {

	constructor (props) {
		super(props);
	}

	componentDidMount () {
		this.create();
		this.startActions();
	}

	componentDidUpdate (prevProps, prevState) {
		this.startActions();
	}

	startActions(){		
		var self = this;
		this.load();
		this.onChangeLength(self);
		this.onChangePaginate(self);
	}

	onChangeLength (self) {
		$('select[name="'+self.props.id+'_length"]').on('change', function () {
			self.aplicaCores();
		}.bind(this));
	}

	onChangePaginate (self) {
		$('div[id="'+self.props.id+'_paginate"]').on('click', function (event) {			
			self.aplicaCores();		
		}.bind(this));
	}
	
	create () {
		$('#'+this.props.id)
		 .DataTable({retrieve: true, responsive: true, searching: false, 
					 lengthChange: true, sort: false, scrollX: true,
					 language: {emptyTable: "Não há registros", 
						 		info: "Exibindo _START_ até _END_ de _TOTAL_ registros", 
						 		infoEmpty: "", 
						        infoFiltered: "(filtrado a partir de _MAX_ registros totais)", 
						        lengthMenu: "Exibir _MENU_ Registros", 
						        loadingRecords: "Carregando...", 
						        processing: "Processando...", 
						        search: "Busca Rápida:", 
						        zeroRecords: "Nenhum registro encontrato", 
						        paginate: {first: "Primeiro", last: "Último", next: "Próximo", previous: "Anterior"}}
					 }).clear()
					   .draw();
	}
	
	load () {
		var unidades = this.props.data;
		var json = [];
		colors = [];
		indexColors = 0;
		for(var index = 0; index < unidades.length; index++){
			var unidade = unidades[index];
			values = [];
			indexValue = 0;

			this.adicionaChaves(unidade);
			this.adicionaConciliaveis(unidade);
			this.adicionaInformativos(unidade);
			this.adicionaColunasFixas(unidade);

			if (index == 0) {
				columnsSize = indexColors;
			}
			json[index] = values;
		}

		$('#'+this.props.id).DataTable().clear();
		$('#'+this.props.id).DataTable().rows.add(json).draw();

		this.aplicaCores();
	}

	adicionaChaves (unidade) {
		for (var indexHeaderChave in this.props.headerChave) {
			var campoHeader = this.props.headerChave[indexHeaderChave];
			for(var campoChaveIndex = 0; campoChaveIndex < unidade.camposChave.length; campoChaveIndex++){
				var campoChave = unidade.camposChave[campoChaveIndex];
				if (campoChave.nome == campoHeader.nome) {
					values[indexValue] = format(campoChave.valor);
					indexValue++;
					
					colors[indexColors] = "chaveCell";
					indexColors++;
					break;
				} else if (campoHeader.isEquivalente && campoChave.campoEquivalente == campoHeader.nome) {
					values[indexValue] = format(campoChave.valorEquivalente);
					indexValue++;
					colors[indexColors] = "chaveCell";
					indexColors++;
					break;
				}
			}
		}
	}

	adicionaConciliaveis(unidade) {
		for (var indexHeaderConciliaveis in this.props.headerConciliaveis) {
			var campoHeader = this.props.headerConciliaveis[indexHeaderConciliaveis];
			for(var campoIndex = 0; campoIndex < unidade.camposConciliaveis.length; campoIndex++){
				var campo = unidade.camposConciliaveis[campoIndex];
				if (campo.nome == campoHeader.nome) {
					if (campo.campoEquivalente && campo.campoEquivalente != '') {
						values[indexValue] = format(campo.valorEquivalente);
						indexValue++;
						if(campo.status.codigo == "OK"){
							colors[indexColors] = "okCell";
						}else if(campo.status.codigo == "DI"){
							colors[indexColors] = "divergenteCell";
						}else if(campo.status.codigo == "SC"){
							colors[indexColors] = "somenteCargaCell";
						}else if (campo.status.codigo = "SI"){
							colors[indexColors] = "somenteImportacaoCell";
						}
						indexColors++;
					}
					values[indexValue] = format(campo.valorCarga);
					indexValue++;
					values[indexValue] = format(campo.valorImportacao);
					indexValue++;
					values[indexValue] = format(campo.valorConciliacao);
					indexValue++;
					if(campo.status.codigo == "OK"){
						colors[indexColors] = "okCell";
						indexColors++;
						colors[indexColors] = "okCell";
						indexColors++
						colors[indexColors] = "okCell";
					}else if(campo.status.codigo == "DI"){
						colors[indexColors] = "divergenteCell";
						indexColors++;
						colors[indexColors] = "divergenteCell";
						indexColors++;
						colors[indexColors] = "divergenteCell";
					}else if(campo.status.codigo == "SC"){
						colors[indexColors] = "somenteCargaCell";
						indexColors++;
						colors[indexColors] = "somenteCargaCell";
						indexColors++;
						colors[indexColors] = "somenteCargaCell";
					}else if (campo.status.codigo = "SI"){
						colors[indexColors] = "somenteImportacaoCell";
						indexColors++;
						colors[indexColors] = "somenteImportacaoCell";
						indexColors++;
						colors[indexColors] = "somenteImportacaoCell";
					}
					indexColors++;
					break;
				}
			}
		}
	}

	adicionaInformativos(unidade) {
		for (var indexHeaderInformativos in this.props.headerInformativos) {
			var campoHeader = this.props.headerInformativos[indexHeaderInformativos];
			for(var campoInformativoIndex = 0; campoInformativoIndex < unidade.camposInformativos.length; campoInformativoIndex++){
				var campoInformativo = unidade.camposInformativos[campoInformativoIndex];
				if (campoHeader == campoInformativo.nome) {
					values[indexValue] = format(campoInformativo.valor);
					indexValue++;
					colors[indexColors] = "informativoCell";
					indexColors++;
					break;
				}
			}
		}
	}

	adicionaColunasFixas(unidade) {
		values[indexValue] = unidade.status.nome;
		indexValue++;
		colors[indexColors] = "otherCell";
		indexColors++;
		values[indexValue] = '<button id="buttonJustificativa" type="button" class="fa fa-pencil-square-o" />';
		indexValue++;
		colors[indexColors] = "otherCell";
		indexColors++;
	}

	aplicaCores() {
		if(colors.length > 0) {
			var page = $('#'+this.props.id).DataTable().page.info().page;
			var index = $('#'+this.props.id).DataTable().page.info().length * columnsSize * page;
			$('#'+this.props.id).find('tr').each(function () {
				$(this).find('td').each(function () {
					$(this).addClass(colors[index]);
					index++;
				});
			});
		}
	}

	draw(id) {
		$('#'+id).DataTable().draw();
	}

	destroy () {
		$('#'+this.props.id).DataTable().destroy();
	}
	
    render () {

		let headerFixedColumns = ["Carga", "Importação", "Conciliação"];
		let otherColumns = ["Status", "Justificativa"];
		
        return (
			
			<table className="table table-striped table-bordered widthDatatable" id={this.props.id}>
				<thead>
					<tr>
						{this.props.headerChave.map (function (campo, idx) {
							return <th rowSpan="2" className="columnStyle" key={idx}>{campo.nome}</th>
						})}
						{this.props.headerConciliaveis.map (function (campo, idx) {
							if (campo.campoEquivalente && campo.campoEquivalente != '') {
								return <th colSpan="4" className="columnStyle" key={idx}>{campo.nome}</th>
							} else {
								return <th colSpan="3" className="columnStyle" key={idx}>{campo.nome}</th>
							}
						})}
						{this.props.headerInformativos.map (function (title, idx) {
							return <th rowSpan="2" className="columnStyle" key={idx}>{title}</th>
						})}
						{otherColumns.map (function (title, idx) {
							return <th rowSpan="2" className="columnStyle" key={idx}>{title}</th>
						})}
					</tr>
					<tr>
						{this.props.headerConciliaveis.map (function (campo, index) {
							var columns = [];
							if (campo.campoEquivalente && campo.campoEquivalente != '') {
								columns.push({nome: campo.campoEquivalente, aplicaCSS: true});
							}
							for (var index in headerFixedColumns){
								columns.push(headerFixedColumns[index]);
							}
							return columns.map(function (column, idx) {
								if(column.aplicaCSS) {
									return <th className="columnStyle equivalenciaHeaderColumn" key={idx}>{column.nome}</th> 
								}
								return <th className="columnStyle" key={idx}>{column}</th>
							});
						})}
					</tr>
				</thead>
			</table>
        );
    }
}