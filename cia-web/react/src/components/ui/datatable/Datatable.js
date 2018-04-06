import React, { Component } from 'react';
import {format} from '../../../shared/util/Utils';

let currentRowIndex;
export default class Datatable extends Component {

	constructor (props) {
	    super(props);
		currentRowIndex = this.props.rowIndex;
		
		this.checkAll = this.checkAll.bind(this);
	}

	componentDidMount () {
		this.create();
		this.startActions();
	}

	shouldComponentUpdate (nextProps, nextState) {		
		if (nextProps.render) {			
			return true;
		}
		return false;
		
	}

	componentWillUpdate (nextProps, nextState) {
		currentRowIndex = nextProps.rowIndex;
	}

	componentDidUpdate (prevProps, prevState){
		this.startActions();
	}

	startActions () {
		var self = this;
		this.load();		

		this.select(self);
		this.selectEventOnClick(self);
		this.checkAll(self);

		this.onChangeLength(self);
		this.onChangeSearch(self);
		this.onChangePaginate(self);
		
		this.stopPropagationClickCheckbox(self);
	}

	onChangeLength (self) {		

		$('select[name="'+self.props.id+'_length"]').on('change', function () {			
				
			self.selectEventOnClick(self, 'reload');
			self.select(self);
			self.checkAll(self, 'reload');
			self.stopPropagationClickCheckbox(self, 'reload');
			
		}.bind(this));
	}

	onChangeSearch (self) {
		
		$('input[type="search"]').on('change', function () {			
				
			self.selectEventOnClick(self, 'reload');
			self.select(self);
			self.checkAll(self, 'reload');
			self.stopPropagationClickCheckbox(self, 'reload');
		
		}.bind(this));
	}

	onChangePaginate (self) {
		
		$('div[id="'+self.props.id+'_paginate"]').on('click', function (event) {			
				
			self.selectEventOnClick(self, 'reload');
			self.select(self);
			self.checkAll(self, 'reload');
			self.stopPropagationClickCheckbox(self, 'reload');
			
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

	stopPropagationClickCheckbox (self, reload) {
		if (reload) {
			$('table[id="'+self.props.id+'"] tbody tr td input').off('click');
		}
		$('table[id="'+self.props.id+'"] tbody tr td input').on('click', function (event) {   			
			var index = $(this).attr('value');
			if ( $(this).hasClass('checkboxSelecionado') ) {
				$(this).removeClass('checkboxSelecionado');
				self.checkElement (index, false, self);
			} else {
				$(this).addClass('checkboxSelecionado');
				self.checkElement (index, true, self);
			}
			event.stopPropagation();
		});
	}

	checkElement (index, checked, self) {
		if (self.props.checkElement) {
			self.props.checkElement(index, checked);
		}
	}

	checkAll (self, reload) {
		if (reload) {
			$('#clickAll').off('click');
		}
		var dataTable = $('#'+self.props.id).DataTable();
		$('#checkAll').click(function () {
			$(':checkbox', dataTable.rows().nodes()).prop('checked', this.checked);
			if (self.props.checkAll) {
				self.props.checkAll(this.checked);
			}
		});
	}

	clearCheckAll() {
		$('#checkAll').prop('checked',false);
	}
	
	create () {
		var isLengthChange = this.props.lengthChange == undefined || this.props.lengthChange;
		var isSearching = this.props.searching == undefined || this.props.searching;

		$('#'+this.props.id)
		 .DataTable({retrieve: true, responsive: true, searching: isSearching, lengthChange: isLengthChange, order: [], 
					 columnDefs: [ {targets: 'no-sort', orderable: false} ],	      
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
		$('#'+this.props.id).DataTable().clear().draw();
		if (this.props.data != null && this.props.data.length > 0) {
			var json = [];
			var attributes = this.props.metaData.toString().split(",");
			for(var index = 0; index < this.props.data.length; index++){
				var values = [];
				var element = this.props.data[index];
				for(var attributeIndex in attributes){
					if(attributeIndex == 0){
						if(this.props.disableCheckboxColumn) {
							values.push('<input type="checkbox" value="'+index+'" id="'+element.id+'" disabled />');
						} else {
							values.push('<input type="checkbox" value="'+index+'" id="'+element.id+'" />');
						}
					}
					var arrayAttributes = attributes[attributeIndex].split('.');
					var value = element[arrayAttributes[0]];
					if (value != null){
						for(var i = 1; i < arrayAttributes.length; i++){
							value = value[arrayAttributes[i]];
						}
						values.push(format(value));
					}else {
						values.push('');
					}
				}
				json[index] = values;
			}
			
			$('#'+this.props.id).DataTable().rows.add(json).draw();

			this.applyDefaultOrder();
		}
	}

	applyDefaultOrder () {
		var arrayOrder = [];
		for (var index in this.props.header) {
			arrayOrder.push ([Number(index) + 1, 'asc']);
		}
		$('#'+this.props.id).DataTable().order(arrayOrder).draw();
	}
	
    render () {
		var display = {'display': this.props.show == undefined || this.props.show ? '' : 'none'};
		var checkAllElement = this.props.checkAll != undefined ? <input type="checkbox" id="checkAll" /> : '';
        return (
			<div style={display}>
				<table className="table table-striped table-bordered table-hover" id={this.props.id}>
					<thead>
						<tr>
							<th className="no-sort">{checkAllElement}</th>
							{this.props.header.map (function (title, idx) {
								return <th key={idx}>{title}</th>
							})}
						</tr>
					</thead>
				</table>
			</div>
        );
    }
}