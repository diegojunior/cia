import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../../ui/form/FormValidator';
import TextInput from '../../../../ui/input/TextInput';
import Button from '../../../../ui/button/Button';
import DominioService from '../../../../../shared/services/Dominio';
import Datatable from '../../../../ui/datatable/Datatable';
import AddRemoveMultiplesInputs from '../../../../ui/input/dynamic/AddRemoveMultiplesInputs';
import {constants} from '../../../../../shared/util/Constants';
import './Style.css';

export default class Sessao extends Component {
	
		constructor(props) {
			super(props);
	
			this.state = {
				sessao: {id: null, codigo: null, nome: null, campos: []},
				sessoes: [],
				camposSelecionados: [],

				sessoesSelecionadas: [],

				fieldsConfig: [],
				dominios: [],
				dominiosRestantes: [],
				
				renderDatatable: true,
				rowIndex: -1,
				
				showErrors: false,
				renderAdicionarCampos: true
			};
	
			this.changeNomeFragmento = this.changeNomeFragmento.bind(this);
			this.changeTagFragmento = this.changeTagFragmento.bind(this);
	
			this.changeLineIndex = this.changeLineIndex.bind(this);
			this.checkElement = this.checkElement.bind(this);
			
			this.getDominios = this.getDominios.bind(this);
	
			this.adicionar = this.adicionar.bind(this);
			this.remover = this.remover.bind(this);		
			this.limpar = this.limpar.bind(this);
			this.novo = this.novo.bind(this);
			this.clear = this.clear.bind(this);

			this.refreshCampos = this.refreshCampos.bind(this);
	
			this.checkDuplicity = this.checkDuplicity.bind(this);
		}
	
		componentWillMount () {
	
			this.loadDominios();
			
			this.state.fieldsConfig = [
				{	key:'dominio', 
					label:'Campo', 
					type: 'select', 
					initialValue: {id: null, codigo: '', tipo: {}}, 
					placeholder: "Selecione...", 
					options: this.getDominios, 
					required: true, 
					chave: true,
					fieldReadOnly: "pattern",
					integerOnly: false, 
					readOnly: false,				
					styleColumnHeader: {'width': '20%'},
					styleColumn: {'width': '20%'}
	
				},
				{	key:'descricao', 
					label:'Descrição', 
					type: 'text', 
					initialValue: '', 
					placeholder: "",
					required: false, 
					integerOnly: false, 
					readOnly: false,
					maxLength: '60',				
					styleColumnHeader: {'width': '20%'},
					styleColumn: {'width': '20%'}
				},
				{	key:'tag', 
					label:'Tag', 
					type: 'text', 
					initialValue: '', 
					placeholder: "Tag do Elemento",
					required: true, 
					integerOnly: false, 
					readOnly: false,
					maxLength: '500',				
					styleColumnHeader: {'width': '50%'},
					styleColumn: {'width': '50%'}
				},
				{	key:'pattern', 
					label:'Formato', 
					type: 'text',
					initialValue: '', 
					placeholder: "", 
					required: false, 
					integerOnly: false, 
					readOnly: false,
					maxLength: '30',				
					styleColumnHeader: {'width': '10%'},
					styleColumn: {'width': '8.92%'}
				}
			];
		}

		changeLineIndex (index, self) {
			
			let elemento = {};
	
			this.state.rowIndex = index;
			
			for (var indexElement in this.state.sessoes) {
				
				if(indexElement == index) {
					elemento = this.state.sessoes[index];
					break;
				}
			}
			
			if (this.state.rowIndex == -1) {
				this.state.sessao.nome = null;
				this.state.sessao.codigo = null;
				this.state.sessao.campos = [];
				this.state.camposSelecionados = [];
				this.refs.formValidator.clearValues();
				this.refs.addRemoveMultiplesInputs.limpar();
				this.setState({showErrors: false, renderDatatable: true});
			} else {
				this.state.sessao.nome = elemento.nome;
				this.state.sessao.codigo = elemento.codigo;
				this.state.sessao.campos = elemento.campos;
				this.state.camposSelecionados = elemento.campos;

				this.refs.nomeFragmento.state.value = elemento.nome;
				this.refs.tagFragmento.state.value = elemento.codigo;
				
				this.setState({showErrors: false, renderAdicionarCampos: false, renderDatatable: false});
			}
		}

		checkElement (index, checked) {
			if (checked) {
				this.state.sessoesSelecionadas.push(index);
			} else {
				for (var i in this.state.sessoesSelecionadas) {
					var element = this.state.sessoesSelecionadas[i];
				
					if(index == element) {
						this.state.sessoesSelecionadas.splice(i, 1);
						break;
					}
				}
			}
		}
		
		loadDominios () {
			DominioService.getAll()
				.then (data => {
					if (data.length > 0) {
						this.state.dominiosRestantes = data;
						var newState = update(this.state, {dominios: {$push: data}});
						this.setState(newState);
					}
				});
		}
	
		getDominios() {
			return this.state.dominiosRestantes;
		}
	
		limpar () {		
			this.refs.addRemoveMultiplesInputs.limpar();
			this.refs.nomeFragmento.forceClear();
			this.refs.tagFragmento.forceClear();
	
			this.state.sessao.nome = null;
			this.state.sessao.codigo = null;
			this.state.sessao.campos = [];
			this.state.camposSelecionados = [];
	
			this.setState({rowIndex: -1, showErrors: false, renderDatatable: true, renderAdicionarCampos: true});
			
		}

		novo () {
			return (event) => {
				event.preventDefault();
				this.refs.addRemoveMultiplesInputs.limpar();
				this.refs.nomeFragmento.forceClear();
				this.refs.tagFragmento.forceClear();
		
				this.state.sessao.nome = null;
				this.state.sessao.codigo = null;
				this.state.sessao.campos = [];
				this.state.camposSelecionados = [];
		
				this.setState({rowIndex: -1, showErrors: false, renderDatatable: true, renderAdicionarCampos: true});
			}
		}
	
		formatValue (value) {
			return value.toUpperCase();
		}

		changeNomeFragmento (value) {
			if (value != '') {
				this.state.sessao.nome = value;
			} else {
				this.state.sessao.nome = null;
			}
			this.setState({renderDatatable: true});
		}
	
		changeTagFragmento (value) {
			if (value != '') {
				this.state.sessao.codigo = value;
			} else {
				this.state.sessao.codigo = null;
			}
			this.setState({renderDatatable: true});
		}

		adicionar () {
			return (event) => {				
				event.preventDefault();

				this.refreshCampos();
				this.setState({showErrors: true});
	
				var isSessaoValida = this.isSessaoValida();
				if (isSessaoValida) {
	
					if (this.isRegrasObedecidas()){
						let newSessao = update(this.state.sessao, {sessao: {$set: this.state.sessao}});
						this.state.sessoes.push(newSessao);
						this.props.infoMessage('Sessão Adicionada.');
						this.limpar();
					}
				}
			}
		}

		isSessaoValida(){
			return this.refs.formValidator.isValid() && this.refs.addRemoveMultiplesInputs.isValid();
		}
	
		remover () {
			return (event) => {
				event.preventDefault();

				if (this.state.sessoesSelecionadas.length <= 0) {
					this.props.infoMessage('Nenhum elemento selecionado!');
					return;
				}
	
				this.props.confirmMessage('Exclusão', 'Deseja remover o(s) registro(s) selecionado(s)?', () => {
					this.deleteDatatableElements(this.state.sessoesSelecionadas);
				
					this.state.sessao = {id: null, codigo: null, nome: null, campos: []};			
					this.state.camposSelecionados = [];
					this.state.rowIndex = -1;
					this.refs.formValidator.clearValues();
					this.refs.addRemoveMultiplesInputs.limpar();
					
					this.setState({showErrors: false, renderDatatable: true});	
				});
			}
		}

		deleteDatatableElements(selectedList) {
			var indexesRemoved = [];
	
			for (var newIndex in selectedList) {
				var newElement = selectedList[newIndex];
				for (var index in this.state.sessoes) {
					if (index == newElement) {
						this.state.sessoes.splice(index, 1);
						indexesRemoved.push(newIndex);
						break;
					}
				} 
			}
	
			for (var i in indexesRemoved) {
				var index = indexesRemoved[i];
				selectedList.splice(index, 1);
			}
	
			if (indexesRemoved.length > 0) {
				this.props.infoMessage('Sessões Removidas!');
			}
		}
	
		refreshCampos () {
			var fields = this.refs.addRemoveMultiplesInputs.getFields();
	
			var campos = [];
			var ordem = 0;
			for (var indexField in fields) {
				var field = fields[indexField];
				if (field['dominio'+indexField]) {
					var dominio = this.getDominio(field['dominio'+indexField].codigo);
					var formato = field['pattern'+indexField] && field['pattern'+indexField].hasOwnProperty("value") ? 
						field['pattern'+indexField].value : field['pattern'+indexField];
					var campo = {id: field['id'], 
								ordem: ordem++,
								dominio: dominio, 
								descricao: field['descricao'+indexField], 
								tag: field['tag'+indexField], 
								pattern: formato};
					if (dominio != null
						&& !this.campoDuplicated(campos, campo)) {
						campos.push(campo);
					}
				}	
			}
			this.state.sessao.campos = campos;
		}
	
		checkDuplicity (stateParam) {
			var fields = stateParam.rows;
	
			var campos = [];
			for (var indexField in fields) {
				var field = fields[indexField];
				var dominio = this.getDominio(field['dominio'+indexField].codigo);
				var formato = field['pattern'+indexField] && field['pattern'+indexField].hasOwnProperty("value") ? 
						field['pattern'+indexField].value : field['pattern'+indexField];
				var campo = {id: field['id'], 
							 dominio: dominio, 
							 descricao: field['descricao'+indexField], 
							 tag: field['tag'+indexField], 
							 pattern: formato
							};
				if (dominio != null
					&& this.campoDuplicated(campos, campo)) {
					this.props.errorMessage('Chave(s) Duplicada(s).' );
					return this.refs.addRemoveMultiplesInputs.removeRow(stateParam);
				} else {
					campos.push(campo);
				}
			}
			return stateParam;
		}
	
		campoDuplicated (campos, campo) {
			for (let index in campos) {
				let campoAdded = campos[index];
				if (campoAdded.id == campo.id
					&& campoAdded.dominio.codigo == campo.dominio.codigo) {
					return true;
				}
			}
			return false;
		}
	
		getDominio (codigo) {
			for (var index in this.state.dominios) {
				var dominio = this.state.dominios[index];			
				if(codigo == dominio.codigo) {
					return dominio;
				}
			}
			return null;
		}
	
		isRegrasObedecidas () {
			return !this.isTagFragmentoDuplicado();
		}
	
		isTagFragmentoDuplicado () {
			for (var index in this.state.sessoes) {
	
				var sessaoExistente = this.state.sessoes[index];
	
				if(index != this.state.rowIndex && 
					this.state.sessao.codigo == sessaoExistente.codigo) {
					this.props.errorMessage('Chave(s) Duplicada(s).');
					return true;
				}
			}
			return false;
		}
	
		clear () {
			this.refs.addRemoveMultiplesInputs.limpar();
			this.refs.nomeFragmento.forceClear();
			this.refs.tagFragmento.forceClear();
	
			this.state.sessao.nome = null;
			this.state.sessao.codigo = null;
			this.state.sessao.campos = [];
			this.state.camposSelecionados = [];			
			this.state.sessoes = [];
	
			this.setState({rowIndex: -1, showErrors: false, renderDatatable: true, renderAdicionarCampos: true});
		}

		//FUNCTIONS PARA WIZARD
		showErrors (isShowErrors) {
			//this.setState({showErrors: isShowErrors});
		}

		isValidForm() {
			if (this.state.sessoes.length == 0) {
				this.props.warningMessage('Nenhuma sessão adicionada!');
				return false;
			}
			this.setState({showErrors: false});
			return true;
		}
		//FIM FUNCTIONS PARA WIZARD
		
		render(){
		
			return (
				<div className="cia-animation-fadein">
					<div className="row">
						
						<div className="col-lg-12">
							<div className="panel panel-default" id="cadPanel">
								<div className="panel-body">
									<FormValidator ref="formValidator">
										
										<TextInput 
											id="nomeFragmento"
											ref="nomeFragmento"
											label="Nome"
											type="text"
											maxLength="60"
											className="col-sm-5 col-md-5 col-lg-5" 
											required={true}
											readOnly={this.state.rowIndex != -1}
											valueChange={this.changeNomeFragmento}
											showError={this.state.showErrors} />	

										<TextInput 
											id="tagFragmento"
											ref="tagFragmento"
											label="Tag do Fragmento"
											type="text"
											className="col-sm-5 col-md-5 col-lg-5" 
											required={true}
											chave={true}
											readOnly={this.state.rowIndex != -1}
											valueChange={this.changeTagFragmento}
											showError={this.state.showErrors}/>
												
										<AddRemoveMultiplesInputs 
											ref="addRemoveMultiplesInputs"
											fieldsConfig={this.state.fieldsConfig}
											valueChange={this.refreshCampos} 
											selectedValue={this.state.camposSelecionados}
											displayPlusMinus={this.state.rowIndex == -1}
											readOnly={this.state.rowIndex != -1}
											showError={this.state.showErrors}
											checkRowDuplicity={this.checkDuplicity}/>
		
										<Button
											id="adicionar" 
											label="Adicionar"
											className={'btn btn-social btn-primary'} 
											onClick={this.adicionar()}
											show={this.state.rowIndex == -1}
											classFigure={'glyphicon glyphicon-circle-arrow-down'} />

										<Button
											id="novo" 
											label="Adicionar Novo"
											className={'btn btn-social btn-default'} 
											onClick={this.novo()}
											show={this.state.rowIndex != -1}
											classFigure={'glyphicon glyphicon-circle-arrow-down'} />
										
										<Datatable 
											id="dataTableSessaoXml"
											header={["Nome", "Tag do Fragmento"]} 
											data={this.state.sessoes}
											metaData={["nome", "codigo"]}
											disableCheckboxColumn={false} 
											lengthChange={false}
											searching={false}
											render={this.state.renderDatatable}
											checkElement={this.checkElement}
											changeLineIndex={this.changeLineIndex} 
											rowIndex={this.state.rowIndex} />

										<Button
											id="remover" 
											label="Remover"
											className={'btn btn-social btn-danger'} 
											onClick={this.remover()}
											classFigure={'fa fa-trash-o'} />

									</FormValidator>
								</div>
							</div>
						</div>
    				</div>
    			</div>
		);
	}
}