import React, { Component } from 'react';
import BaseComponent from './BaseComponent';
import update from 'immutability-helper';

export default class CrudComponent extends BaseComponent {
	
	constructor(props) {
	    super(props);
        this.state.renderPesquisa = true;
        this.state.renderCadastro = false;
        this.state.renderDetalhe = false;
        this.state.showDatatable = true;
        this.state.list = [];

        this.state.selectedList = [];
        this.state.renderDatatable = true;
        this.state.elementoSelecionado = {};
        this.state.rowIndex = -1;
        this.state.showHidePesquisa = {'display': 'block'};
        this.state.showHideCadastro = {'display': 'none'};
        this.state.showHideDetalhe = {'display': 'none'};

        this.state.clear = false;
        
        this.showPesquisa = this.showPesquisa.bind(this);
		this.showCadastro = this.showCadastro.bind(this);
        this.showDetalhe = this.showDetalhe.bind(this);
        
        this.changeLineIndex = this.changeLineIndex.bind(this);
        this.selecionar = this.selecionar.bind(this);
        this.checkElement = this.checkElement.bind(this);
        this.addDatatableElement = this.addDatatableElement.bind(this);
        this.alterDatatableElement = this.alterDatatableElement.bind(this);

        this.clear = this.clear.bind(this);
	}

    componentWillUpdate (nextProps, nextState) {
        nextState.showHidePesquisa = {'display': nextState.renderPesquisa ? 'block' : 'none'};
        nextState.showHideCadastro = {'display': nextState.renderCadastro ? 'block' : 'none'};
        nextState.showHideDetalhe = {'display': nextState.renderDetalhe ? 'block' : 'none'};
    }

    showPesquisa () {
		this.setState({elementoSelecionado: {}, rowIndex: -1, renderDatatable: true, showDatatable: true, renderPesquisa: true, renderCadastro: false, renderDetalhe: false});
	}

	showCadastro (displayDatatable) {
        if (displayDatatable == undefined) {
            displayDatatable = true;
        }
		this.setState({renderPesquisa: false, renderCadastro: true, showDatatable: displayDatatable, renderDetalhe: false, selectedList: []});
	}

    showDetalhe () {
		this.setState({renderPesquisa: false, renderCadastro: false, showDatatable: true, renderDetalhe: true, selectedList: []});
	}

    isPesquisaAtiva() {
        return this.state.renderPesquisa && !this.state.renderCadastro && !this.state.renderDetalhe;
    }

    isCadastroAtivo() {
        return !this.state.renderPesquisa && this.state.renderCadastro && !this.state.renderDetalhe;
    }

    isDetalheAtivo() {
        return !this.state.renderPesquisa && !this.state.renderCadastro && this.state.renderDetalhe;
    }

    changeLineIndex (index) {        
        let elemento = this.selecionar(index);        
        this.setState({
            elementoSelecionado: elemento, 
            renderDatatable: false,
            renderPesquisa: false,
            renderCadastro: true,
            renderDetalhe: false,
            clear: true});
    }

    selecionar (index) {
        
        for (var indexElement in this.state.list) {

            if(indexElement == index) {
                return this.state.list[index];
            }
        }

        return {};
    }

	checkElement (index, checked) {
        if (checked) {
            for (var i in this.state.list) {
                if(i == index) {
                    this.state.selectedList.push(this.state.list[i]);
                    break;
                }
            }

        } else {
            var selectedElement = this.state.list[index];
            for (var i in this.state.selectedList) {
                var element = this.state.selectedList[i];
                if(selectedElement.id == element.id) {
                    this.state.selectedList.splice(i, 1);
                    break;
                }
            }
        }
    }

    addDatatableElement(element) {
        let newList = update(this.state.list, {list: {$set: this.state.list}});
        newList.push(element);
        this.setState({list: newList, renderDatatable: true});
    }

    alterDatatableElement(newElement) {

        let newList = update(this.state.list, {list: {$set: this.state.list}});
         for (var index in newList) {
             var oldElement = newList[index];
         
            if (oldElement.id == newElement.id) {
                
                newList.splice(index, 1, newElement);

                this.setState({list: newList, renderDatatable: true});

                break;
             }
        }
    }

    deleteDatatableElements(selectedList) {

        let newList = update(this.state.list, {list: {$set: this.state.list}});
        var indexesRemoved = [];

        for (var newIndex in selectedList) {
            var newElement = selectedList[newIndex];

            for (var index in newList) {
            
                var oldElement = newList[index];
            
                if (oldElement.id == newElement.id) {
                    
                    newList.splice(index, 1);
                    indexesRemoved.push(newIndex);
                    break;
                }
            } 
        }

        for (var i in indexesRemoved) {
            var index = indexesRemoved[i];
            selectedList.splice(index, 1);
        }

        this.setState({list: newList, renderDatatable: true});
    }
    
    clear () {
		this.setState({elementoSelecionado: {}, rowIndex: -1, renderDatatable: true, selectedList: [], clear: true});
    }
	
    render () {
        return (<BaseComponent />);
    }
}