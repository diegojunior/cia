import React, { Component } from 'react';
import './Style.css';

let colors;
let columnsSize = 0;
let currentRowIndex;
export default class Datatable extends Component {

	constructor (props) {
		super(props);
		currentRowIndex = this.props.rowIndex;
	}

	componentDidMount () {
		this.create();
		this.startActions();
	}

	componentDidUpdate (prevProps, prevState) {
		this.startActions();
	}

	componentWillUpdate (nextProps, nextState) {
		currentRowIndex = nextProps.rowIndex;
	}

	startActions(){		
		var self = this;
		this.load();
		this.select(self);
		this.selectEventOnClick(self);
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

	selectEventOnClick (self, reload) {

		if(reload) {
			$('table[id="'+self.props.id+'"] tbody tr').off('click');
		}

		$('table[id="'+self.props.id+'"] tbody tr').on('click', function () {

			if($(this).hasClass('selected')) {
				$('table[id="'+self.props.id+'"] tbody').find('tr.selected').removeClass('selected');
				currentRowIndex = -1;
			} else {
				$('table[id="'+self.props.id+'"] tbody').find('tr.selected').removeClass('selected');
				$(this).addClass('selected');
				currentRowIndex = $(this).find('td input[type="checkbox"]').attr('value');
			}
			self.changeLineIndex(currentRowIndex, self);
		});
		
	}

	select (self) {
        $('table[id="'+self.props.id+'"] tbody').find('tr.selected').removeClass('selected');
        
		if (currentRowIndex >= 0) {
			$('table[id="'+self.props.id+'"] tbody tr').each(
				function(){
					var index = $(this).find('td input[type="checkbox"]').attr('value');
					
					if(currentRowIndex == index) {
						$('table[id="'+self.props.id+'"] tbody').find('tr.selected').removeClass('selected');
						$(this).addClass('selected');
						self.changeLineIndex(currentRowIndex, self);						
						return;
					}
				}
			);
		}
    }

	changeLineIndex(index, self) {
		if(self.props.changeLineIndex) {
			self.props.changeLineIndex(index, self);
		}
	}
	
	create () {
		$('#'+this.props.id)
		 .DataTable({retrieve: true, responsive: true, searching: false, 
			         lengthChange: false, sort: false, scrollX: false, order: [], 
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
		var json = [];
		colors = [];
		var indexColors = 0;
		
		for(var index = 0; index < this.props.data.length; index++){
			var elemento = this.props.data[index];
			
			var values = [];
			var indexValue = 0;

			values[indexValue] = elemento.perfil;
			indexValue++;
			colors[indexColors] = "otherLeftCell";
			indexColors++;

			values[indexValue] = elemento.statusCarga.nome;
			indexValue++;
			colors[indexColors] = this.getClassCarga(elemento.statusCarga.codigo);
			indexColors++;

			values[indexValue] = elemento.statusImportacao.nome;
			indexValue++;
			colors[indexColors] = this.getClassImportacao(elemento.statusImportacao.codigo);
			indexColors++;

			values[indexValue] = elemento.statusConciliacao.nome;
			indexValue++;
			colors[indexColors] = this.getClassConciliacao(elemento.statusConciliacao.codigo);;
			indexColors++;

			if (index == 0) {
				columnsSize = indexColors;
			}
			json[index] = values;
		}

		$('#'+this.props.id).DataTable().clear();
		$('#'+this.props.id).DataTable().rows.add(json).draw();

		//aplica o css em cada célula do datatable. 
		this.aplicaCores();
	}

	getClassCarga (codigo) {
		return "otherCell";
	}

	getClassImportacao (codigo) {
		return "otherCell";
	}

	getClassConciliacao (codigo) {
		if (codigo == 'AN') {
			return "andamentoCell";
		}
		if (codigo == 'DI') {
			return "divergenteCell";
		}
		if (codigo == 'OK') {
			return "concluidoCell";
		}
		if (codigo == 'ER') {
			return "erroCell";
		}
		if (codigo == 'GR') {
			return "gravadoCell";
		}
		if (codigo == 'GD') {
			return "gravadoDivergenciaCell";
		}
		if (codigo == 'NE') {
			return "naoExecutadoCell";
		}
		return "otherCell";
	}

	aplicaCores() {
		if(colors.length > 0) {
			var page = $('#'+this.props.id).DataTable().page.info().page;
			var index = $('#'+this.props.id).DataTable().page.info().length * columnsSize * page;
			var id = this.props.id;
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
	
    render () {
		var header = ["Perfil", "Carga", "Importação", "Conciliação"]
        return (<div className="cia-animation-fadein">			
				<table className="table table-striped table-bordered table-hover" id={this.props.id}>
					<thead>
						<tr>
							{header.map (function (title, idx) {
								return <th key={idx}>{title}</th>
							})}
						</tr>
					</thead>
				</table>	
				</div>		
        );
    }
}