import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../../ui/form/FormValidator';
import TextInput from '../../../../ui/input/TextInput';
import Button from '../../../../ui/button/Button';
import Datatable from '../../../../ui/datatable/Datatable';
import AddRemoveMultiplesInputs from './MultiplesInputs';
import {constants} from '../../../../../shared/util/Constants';
import './Style.css';

export default class Sessao extends Component {
	
		constructor(props) {
			super(props);
	
			this.state = {
				sessao: {id: null, codigo: null, nome: null, tamanho: null, campos: []},
				sessoes: [],
				camposSelecionados: [],

				sessoesSelecionadas: [],

				renderDatatable: true,
				rowIndex: -1,
				
				showErrors: false,
				renderAdicionarCampos: true
			};
	
			this.changeNomeLinha = this.changeNomeLinha.bind(this);
			this.changeCodigoLinha = this.changeCodigoLinha.bind(this);
			this.changeTamanhoLinha = this.changeTamanhoLinha.bind(this);
	
			this.changeLineIndex = this.changeLineIndex.bind(this);
			this.checkElement = this.checkElement.bind(this);
	
			this.adicionar = this.adicionar.bind(this);
			this.remover = this.remover.bind(this);		
			this.limpar = this.limpar.bind(this);
			this.novo = this.novo.bind(this);
			this.clear = this.clear.bind(this);

			this.carregaCampos = this.carregaCampos.bind(this);
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
				this.state.sessao.tamanho = null;
				this.state.sessao.campos = [];
				this.state.camposSelecionados = [];
				this.refs.formValidator.clearValues();
				this.refs.addRemoveMultiplesInputs.limpar();
				this.setState({showErrors: false, renderDatatable: true});
			} else {
				this.state.sessao.nome = elemento.nome;
				this.state.sessao.codigo = elemento.codigo;
				this.state.sessao.tamanho = elemento.tamanho;
				this.state.sessao.campos = elemento.campos;
				this.state.camposSelecionados = elemento.campos;

				this.refs.nome.state.value = elemento.nome;
				this.refs.codigo.state.value = elemento.codigo;
				this.refs.tamanho.state.value = elemento.tamanho;
				
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
	
		limpar () {		
			this.refs.addRemoveMultiplesInputs.limpar();
			this.refs.formValidator.clearValues();
			this.refs.nome.forceClear();
			this.refs.codigo.forceClear();
			this.refs.tamanho.forceClear();
	
			this.state.sessao.id = null;
			this.state.sessao.nome = null;
			this.state.sessao.codigo = null;
			this.state.sessao.tamanho = null;
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
				this.refs.tamanho.forceClear();
		
				this.state.sessao.id = null;
				this.state.sessao.nome = null;
				this.state.sessao.codigo = null;
				this.state.sessao.tamanho = null;
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

		changeTamanhoLinha (value) {
			this.state.sessao.tamanho = value;
			let newState = update(this.state, {renderDatatable: {$set: true}});
			this.setState(newState);
		}

		adicionar () {
			return (event) => {
				event.preventDefault();

				this.carregaCampos();
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
				
					this.state.sessao = {id: null, codigo: null, nome: null, tamanho: null, campos: []};
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

		carregaCampos () {
			var fields = this.refs.addRemoveMultiplesInputs.getCampos();

			this.state.sessao.campos = fields;
		}
	
	
		isRegrasObedecidas () {
			if (!this.isCodigoLinhaDuplicado() && this.isTamanhoLinhaRespeitado()) {
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

		isTamanhoLinhaRespeitado () {
			var tamanhoInserido = 0;
			for (var index in this.state.sessao.campos) {
				var campo = this.state.sessao.campos[index];
				tamanhoInserido += Number(campo.tamanho);
			}
			if (tamanhoInserido != this.state.sessao.tamanho) {
				this.props.errorMessage('O tamanho total dos campos deve ser igual ao Tamanho da Linha.');
				return false;
			}
			return true;
		}

		clear () {

			this.refs.addRemoveMultiplesInputs.limpar();
			this.refs.formValidator.clearValues();
			this.refs.nome.forceClear();
			this.refs.codigo.forceClear();
			this.refs.tamanho.forceClear();
	
			this.state.sessao.id = null;
			this.state.sessao.nome = null;
			this.state.sessao.codigo = null;
			this.state.sessao.tamanho = null;
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
											id="nome"
											ref="nome"
											label="Nome"
											type="text"
											maxLength="60"
											className="col-sm-5 col-md-5 col-lg-5" 
											required={true}
											readOnly={this.state.rowIndex != -1}
											valueChange={this.changeNomeLinha}
											showError={this.state.showErrors} />	

										<TextInput 
											id="codigo"
											ref="codigo"
											label="Código"
											type="text"
											className="col-sm-5 col-md-5 col-lg-5" 
											required={true}
											chave={true}
											readOnly={this.state.rowIndex != -1}
											valueChange={this.changeCodigoLinha}
											showError={this.state.showErrors}/>

										<TextInput 
											id="Tamanho"
											ref="tamanho"
											placeholder="Somente números Inteiros"
											label="Tamanho da linha"
											type="text"
											maxLength="4"
											className="col-sm-5 col-md-5 col-lg-5"
											required={true}
											integerOnly={true}
											valueChange={this.changeTamanhoLinha}
											readOnly={this.state.rowIndex != -1}
											showError={this.state.showErrors} />

										<AddRemoveMultiplesInputs 
											ref="addRemoveMultiplesInputs" 
											selectedValue={this.state.camposSelecionados}
											displayPlusMinus={this.state.rowIndex == -1}
											readOnly={this.state.rowIndex != -1}
											showError={this.state.showErrors} />
		
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
											id="dataTableSessaoPosicional"
											header={["Nome", "Código da Linha", "Tamanho"]} 
											data={this.state.sessoes}
											metaData={["nome", "codigo", "tamanho"]}
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