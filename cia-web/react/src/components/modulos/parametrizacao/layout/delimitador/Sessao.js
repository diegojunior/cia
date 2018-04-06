import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../../ui/form/FormValidator';
import TextInput from '../../../../ui/input/TextInput';
import RadioInput from '../../../../ui/input/RadioInput';
import Button from '../../../../ui/button/Button';
import DominioService from '../../../../../shared/services/Dominio';
import Datatable from '../../../../ui/datatable/Datatable';
import AddRemoveMultiplesInputs from './MultiplesInputs';
import {constants} from '../../../../../shared/util/Constants';
import './Style.css';

export default class Sessao extends Component {
	
		constructor(props) {
			super(props);
	
			this.state = {
				sessao: {id: null, codigo: '', nome: null, campos: [], semSessaoConfigurada: false},
				sessoes: [],
				camposSelecionados: [],

				sessoesSelecionadas: [],
				comOuSemSessao: [],
				showCodigoSessao: true,
				showButtonAddSessao: true,
				readOnlyOptionComOuSemSessao: false,

				fieldsConfig: [],
				dominios: [],
				dominiosRestantes: [],
				
				renderDatatable: true,
				rowIndex: -1,
				
				showErrors: false,
				renderAdicionarCampos: true
			};
	
			this.changeNomeLinha = this.changeNomeLinha.bind(this);
			this.changeCodigoLinha = this.changeCodigoLinha.bind(this);
			this.onChangeComOuSemSessao = this.onChangeComOuSemSessao.bind(this);
				
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

			this.showAddSessao = this.showAddSessao.bind(this);
			this.readOnlyChangeComOuSemSessao = this.readOnlyChangeComOuSemSessao.bind(this);
			this.readOnly = this.readOnly.bind(this);
		}
	
		componentWillMount () {
	
			this.loadDominios();

			this.state.comOuSemSessao = [{codigo: 'Código Sessão', chave: true}, {codigo: 'Sem Sessão'}]
			
			this.state.fieldsConfig = [
				{	key:'dominio', 
					label:'Campo', 
					type: 'select', 
					initialValue: {id: null, codigo: '', tipo: {}}, 
					placeholder: "Selecione...", 
					options: this.getDominios, 
					required: true, 
					chave: true,
					integerOnly: false, 
					readOnly: false,
					fieldReadOnly: "pattern",
					setPrevValue: false,				
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
					setPrevValue: false,
					maxLength: '60',
					styleColumnHeader: {'width': '22%'},
					styleColumn: {'width': '22%'}
					
				},
				{	key:'pattern', 
					label:'Formato', 
					type: 'text',
					initialValue: '', 
					placeholder: "Formato", 
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
				this.state.sessao.codigo = '';
				this.state.showCodigoSessao = true;
				this.state.sessao.semSessaoConfigurada = false;
				this.state.sessao.campos = [];
				this.state.camposSelecionados = [];
				this.refs.formValidator.clearValues();
				this.refs.addRemoveMultiplesInputs.limpar();
				this.setState({showErrors: false, renderDatatable: true});
			} else {
				this.state.sessao.nome = elemento.nome;
				this.state.sessao.codigo = elemento.codigo;
				this.state.sessao.campos = elemento.campos;
				this.state.sessao.semSessaoConfigurada = elemento.semSessaoConfigurada;
				this.state.camposSelecionados = elemento.campos;
				this.state.showCodigoSessao = elemento.codigo != 'SEM SESSÂO' ? true : false;

				this.refs.nome.state.value = elemento.nome;
				this.refs.codigo.state.value = elemento.codigo;
								
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
			this.refs.formValidator.clearValues();
			this.refs.nome.forceClear();
			this.refs.codigo.forceClear();
			this.refs.comOuSemSessao.selecionar(0);
				
			this.state.sessao.id = null;
			this.state.sessao.nome = null;
			this.state.sessao.codigo = '';
			this.state.sessao.semSessaoConfigurada = false;
			this.state.sessao.campos = [];
			this.state.camposSelecionados = [];			

			this.setState({rowIndex: -1, showErrors: false, renderDatatable: true, renderAdicionarCampos: true});
			
		}

		novo () {
			return (event) => {
				event.preventDefault();
				this.refs.addRemoveMultiplesInputs.limpar();
				this.refs.formValidator.clearValues();
				this.refs.nome.forceClear();
				this.refs.codigo.forceClear();
				this.refs.comOuSemSessao.selecionar(0);
						
				this.state.sessao.id = null;
				this.state.sessao.nome = null;
				this.state.sessao.codigo = '';
				this.state.sessao.semSessaoConfigurada = false;
				this.state.sessao.campos = [];
				this.state.camposSelecionados = [];			
	
				this.setState({rowIndex: -1, showErrors: false, renderDatatable: true, renderAdicionarCampos: true});
			}
		}
	
		formatValue (value) {
			return value.toUpperCase();
		}

		changeNomeLinha (value) {
			this.state.sessao.nome = value;
			let newState = update(this.state, {renderDatatable: {$set: true}});
			this.setState(newState);
		}

		changeCodigoLinha(value) {
			this.state.sessao.codigo = value;
			let newState = update(this.state, {renderDatatable: {$set: true}});
			this.setState(newState);
		}

		onChangeComOuSemSessao(value) {
			let flagShowCodigoSessao = false;
			if (value == 0) {
				flagShowCodigoSessao = true;
				this.state.sessao.semSessaoConfigurada = false;
			} else {
				this.state.sessao.codigo = "SEM SESSÂO";
				this.state.sessao.semSessaoConfigurada = true;
			}
			this.setState({showCodigoSessao: flagShowCodigoSessao});
		}

		adicionar () {
			return (event) => {				
				event.preventDefault();

				this.refreshCampos();
				this.setState({showErrors: true});
				if (!this.state.showCodigoSessao) {
					this.refs.formValidator.removeError('codigo');
				}
				var isSessaoValida = this.isSessaoValida();
				if (isSessaoValida) {
					if (this.isRegrasObedecidas()){
						let newSessao = update(this.state.sessao, {sessao: {$set: this.state.sessao}});
						this.state.sessoes.push(newSessao);
						if (!this.state.showCodigoSessao
							&& this.state.sessoes.length == 1) {
							this.state.showButtonAddSessao = false;
						} else {
							this.state.showButtonAddSessao = true;
						}
						this.state.readOnlyOptionComOuSemSessao = true;
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
					this.state.sessao = {id: null, codigo: null, nome: null, semSessaoConfigurada: false, campos: []};
					this.state.camposSelecionados = [];
					if (this.state.sessoes.length == 0) {
						this.state.readOnlyOptionComOuSemSessao = false;
						this.state.showCodigoSessao = true;
					} else {
						this.state.readOnlyOptionComOuSemSessao = true;
					}
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

				var dominio = this.getDominio(field['dominio'+indexField].codigo);
				var formato = field['pattern'+indexField] && field['pattern'+indexField].hasOwnProperty("value") ? 
							field['pattern'+indexField].value : field['pattern'+indexField];
				var campo = {id: field['id'], 
							ordem: ordem++,
							dominio: dominio, 
							descricao: field['descricao'+indexField], 
							pattern: formato};

				if (dominio != null
					&& !this.campoDuplicated(campos, campo)) {
					campos.push(campo);
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
			if (!this.isCodigoLinhaDuplicado()) {
				return true;
			}
			return false;

		}

		isCodigoLinhaDuplicado () {
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
			this.refs.formValidator.clearValues();
			this.refs.nome.forceClear();
			this.refs.codigo.forceClear();
				
			this.state.sessao.id = null;
			this.state.sessao.nome = null;
			this.state.sessao.codigo = null;
			this.state.sessao.semSessaoConfigurada = false;
			this.state.showButtonAddSessao = true;
			this.state.showCodigoSessao = true;
			this.state.readOnlyOptionComOuSemSessao = false;
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

		showAddSessao () {
			if (this.state.sessoes.length == 0) {
				return true;
			}
			if (this.state.showButtonAddSessao
				&& this.state.showCodigoSessao
				&& this.state.rowIndex == -1) {
				return true;
			}
			return false;
		}

		readOnlyChangeComOuSemSessao () {
			return this.state.readOnlyOptionComOuSemSessao;
		}

		readOnly () {
			if (!this.state.showCodigoSessao
				&& this.state.sessoes.length == 1) {
				return true;
			}

			if (this.state.rowIndex != -1) {
				return true;
			}

			return false;
		}
		
		render(){
			return (
				<div className="cia-animation-fadein">
					<div className="row">
						
						<div className="col-lg-12">
							<div className="panel panel-default" id="cadPanel">
								<div className="panel-body">
									<FormValidator ref="formValidator">
										
										<TextInput 
											id="nome"
											ref="nome"
											label="Nome"
											type="text"
											maxLength="60"
											className="col-sm-5 col-md-5 col-lg-5" 
											required={true}
											readOnly={this.readOnly()}
											valueChange={this.changeNomeLinha}
											showError={this.state.showErrors} />

										<RadioInput 
											id="comOuSemSessao"
											ref="comOuSemSessao"
											className="col-sm-5 col-md-5 col-lg-5"
											required={true}
											show="true"
											readOnly={this.state.rowIndex != -1 || this.readOnlyChangeComOuSemSessao()}
											options={this.state.comOuSemSessao}
											onChange={this.onChangeComOuSemSessao} />	

										<TextInput 
											id="codigo"
											ref="codigo"
											labelValidator="Código"
											type="text"
											className="col-sm-5 col-md-5 col-lg-5" 
											required={true}
											chave={this.state.showCodigoSessao}
											readOnly={this.readOnly()}
											valueChange={this.changeCodigoLinha}
											showError={this.state.showErrors}
											show={this.state.showCodigoSessao}/>

										<AddRemoveMultiplesInputs 
											ref="addRemoveMultiplesInputs"
											fieldsConfig={this.state.fieldsConfig}
											valueChange={this.refreshCampos} 
											selectedValue={this.state.camposSelecionados}
											displayPlusMinus={this.state.rowIndex == -1}
											readOnly={this.readOnly()}
											showError={this.state.showErrors}
											checkRowDuplicity={this.checkDuplicity}/>
		
										<Button
											id="adicionar" 
											label="Adicionar"
											className={'btn btn-social btn-primary'} 
											onClick={this.adicionar()}
											show={this.showAddSessao()}
											classFigure={'glyphicon glyphicon-circle-arrow-down'} />

										<Button
											id="novo" 
											label="Adicionar Novo"
											className={'btn btn-social btn-default'} 
											onClick={this.novo()}
											show={this.state.rowIndex != -1 && this.state.showCodigoSessao}
											classFigure={'glyphicon glyphicon-circle-arrow-down'} />
										
										<Datatable 
											id="dataTableSessaoDelimitador"
											header={["Código da Linha", "Nome"]} 
											data={this.state.sessoes}
											metaData={["codigo", "nome"]}
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