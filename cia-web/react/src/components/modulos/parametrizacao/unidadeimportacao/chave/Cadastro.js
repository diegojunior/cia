import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../../ui/form/FormValidator';
import TextInput from '../../../../ui/input/TextInput';
import SelectInput from '../../../../ui/input/SelectInput';
import AddRemoveMultiplesInputs from '../../../../ui/input/dynamic/AddRemoveMultiplesInputs';
import ButtonCrud from '../../../../ui/button/ButtonCrud';
import Picklist from '../../../../ui/picklist/Picklist';
import UnidadeService from '../../../../../shared/services/UnidadeImportacaoChave';
import LayoutService from '../../../../../shared/services/Layout';
import {constants} from '../../../../../shared/util/Constants';
import {sort} from '../../../../../shared/util/Utils';
import {getValueByProperty, objectAsSame, copyObject}  from '../../../../../shared/util/Utils';
import './Style.css';

export default class Cadastro extends Component {
	
	constructor(props) {
		super(props);

		this.state = {
			unidade: {
				id: null, codigo: '', descricao: '', 
				tipoLayout: '', layout: {}, sessoes: [], 
				camposUnidadeImportacao: [], chavesUnidadeImportacao: []},
			unidadeSelecionado: {
				id: null, codigo: '', descricao: '', 
				tipoLayout: '', layout: {}, sessoes: [], 
				camposUnidadeImportacao: [], chavesUnidadeImportacao: []},
			tiposLayout: constants.getTiposLayouts(),
			layouts: [],
			sessoes: [],
			chaves: [],
			campos: [],
			showErrors: false
		};

		this.incluir = this.incluir.bind(this);
		this.limpar = this.limpar.bind(this);
		
		this.changeCodigoFieldChanged = this.changeCodigoFieldChanged.bind(this);
		this.changeDescricaoFieldChanged = this.changeDescricaoFieldChanged.bind(this);
		this.changeTipoLayout = this.changeTipoLayout.bind(this);
		this.changeLayout = this.changeLayout.bind(this);
		this.changeSessoes = this.changeSessoes.bind(this);
		this.changeChaves = this.changeChaves.bind(this);
		this.changeCampos = this.changeCampos.bind(this);
		
		this.showPesquisa = this.showPesquisa.bind(this);
	}

	componentWillReceiveProps (nextProps) {
		
		if (nextProps.template.state.elementoSelecionado && 
			nextProps.template.state.elementoSelecionado.id && 
			nextProps.template.state.elementoSelecionado.id != '') {
			
			this.refs.formValidator.clearValues();
		
			this.loadLayouts(nextProps.template.state.elementoSelecionado.layout.tipoLayout);

			this.state.sessoes = nextProps.template.state.elementoSelecionado.layout.sessoes;

			this.state.chaves = this.getChavesPor(nextProps.template.state.elementoSelecionado.sessoes);

			let campos = this.getCamposPor(nextProps.template.state.elementoSelecionado.sessoes, this.state.chaves);

			this.state.campos = this.gerarCampos(this.state.sessoes, campos)

			let newState = update(this.state, {
				unidade: {$set: nextProps.template.state.elementoSelecionado},
				unidadeSelecionado: {$set: nextProps.template.state.elementoSelecionado},
				renderDatatable: {$set: true}});

			this.setState(newState);

		} else if (nextProps.template.state.clear){
			this.clear();
		}
    }

    showPesquisa () {
    	return (event) => {

    		event.preventDefault();
	    	
	    	this.props.template.clear();
			
			this.clear ();
	        
	        this.props.template.showPesquisa();

    	}
    }

    limpar () {		
		
		return (event) => {
			event.preventDefault();

			this.props.template.clear();

			this.clear();
		}
	}

	clear () {

		this.refs.formValidator.clearValues();

		let unidadeClear = {
			id: null, codigo: '', descricao: '', 
			tipoLayout: '', layout: {}, sessoes: [], 
			chavesUnidadeImportacao: [], camposUnidadeImportacao: []};

		this.state.chaves = [];
		this.state.campos = [];		
		let newState = update(this.state, {
									unidade: {$set: unidadeClear}, 
									layouts: {$set: []},
									sessoes: {$set: []},
									unidadeSelecionado: {$set: unidadeClear}, 
									showErrors: {$set: false}
								});
		this.setState(newState);
	}

	incluir () {
		
		return (event) => {
			event.preventDefault();

			this.setState({showErrors: true});

			if(this.refs.formValidator.isValid()) {
				UnidadeService
					.incluir (this.state.unidade)
					.then (data => {
						this.props.template.successMessage('Unidade de importação incluída com sucesso.');
						this.props.template.addDatatableElement(data);
						this.props.template.clear();
						this.clear();
					}).catch (error => {
						this.props.template.errorMessage(error);
					});
			}
		}
	}

	changeCodigoFieldChanged (value) {
		let newState = update(this.state, {unidade: {codigo: {$set: value}}});
		this.setState(newState);
	}

	changeDescricaoFieldChanged (value) {
		let newState = update(this.state, {unidade: {descricao: {$set: value}}});
		this.setState(newState);
	}

	changeTipoLayout (value) {
		this.refs.layoutRef.clear()
		this.refs.sessoesRef.clear();
		this.refs.chavesRef.clear();
		this.refs.camposRef.clear();
		let tipoLayout = constants.getTipoLayoutPorCodigo(value);
		if (tipoLayout == null) {
			let unidadeClear = {
				tipoLayout: '', layout: null, sessoes: [], 
				camposUnidadeImportacao: [], chavesUnidadeImportacao: []};
			this.state.layouts = [];
			this.state.sessoes = [];
			this.state.chaves = [];
			this.state.campos = [];
			let newState = update(this.state, {unidade: {$set: unidadeClear}});
			this.setState(newState);
		} else {
			let constantTipoLayout = constants.getConstantTipoLayoutPorCodigo(value);
			LayoutService
				.getBy(constantTipoLayout)
				.then (data => {
					sort(data, 'codigo');
					this.state.layouts = data;
					this.state.sessoes = [];
					this.state.chaves = [];
					this.state.campos = [];
					let newState = update(this.state, {
						unidade: {
							tipoLayout: {$set: tipoLayout}, 
							sessoes: {$set: []},
							chavesUnidadeImportacao: {$set: []},
							camposUnidadeImportacao: {$set: []}}});
					
					this.setState(newState);
				}).catch (error => {
					this.props.template.errorMessage(error);
				});
		}
		
	}

	changeLayout (codigoLayout) {
		this.refs.sessoesRef.clear();
		this.refs.chavesRef.clear();
		this.refs.camposRef.clear();
		
		if (codigoLayout == null) {
			let unidadeClear = {
				layout: null, sessoes: [], 
				camposUnidadeImportacao: [], chavesUnidadeImportacao: []};
			this.state.sessoes = [];
			this.state.chaves = [];
			this.state.campos = [];
			let newState = update(this.state, {unidade: {$set: unidadeClear}});
			this.setState(newState);

		} else {	
			for (var index in this.state.layouts) {
				let layoutSelecionado = this.state.layouts[index];			
				if(codigoLayout == layoutSelecionado.codigo) {
					this.state.sessoes = layoutSelecionado.sessoes;
					this.state.chaves = [];
					this.state.campos = [];
					let newState = update(this.state, {unidade: {layout: {$set: layoutSelecionado}}});
					this.setState(newState);
					break;
				}
			}	
		}
		
	}

	changeSessoes (options) {
		let sessoes = this.state.sessoes.slice();
		let chaves = this.getChavesPor(options);
		let campos = this.getCamposPor(options, chaves);
		let camposUnidadeImportacao = this.gerarCampos(sessoes, campos)
		if (!objectAsSame(this.state.chaves, chaves, 'sessoes', 'campo.dominio.id')) {
			this.refs.chavesRef.clear();
			this.state.chaves = chaves;
		}

		if (!objectAsSame(this.state.campos, camposUnidadeImportacao, 'campo.dominio.id')) {
			this.refs.camposRef.clear();
			this.state.campos = camposUnidadeImportacao;
		}
		var newState = update(this.state, {
			unidade: {
				sessoes: {$set: options}}});
		this.setState(newState);
	}

	changeChaves (options) {
		let sessoesSelecionadas = [];
		if (options.length > 0) {
			sessoesSelecionadas = this.state.unidade.sessoes.slice();
		}
		
		this.state.unidade.camposUnidadeImportacao = [];
		let newState = update(this.state, {unidade: {chavesUnidadeImportacao: {$set: options}}});
		this.setState(newState);
	}

	changeCampos (optionsCampos) {
		let newState = update(this.state, {unidade: {camposUnidadeImportacao: {$set: optionsCampos}}});
		this.setState(newState);
	}

	getChavesPor (options) {
		let chaves = [];
		let sessoesChaves = [];
		let optionsCopy = options.slice();
		if (optionsCopy.length > 0)  {
			chaves = optionsCopy[0].campos.slice();
			sessoesChaves.push(optionsCopy[0]);
			if (optionsCopy.length > 1) {
				let newOptions = optionsCopy.slice(1);
				newOptions.forEach(option => {
					let copyOfChaves = chaves.slice();
					copyOfChaves.forEach((campoChave) => {
						if (!this.foundSearchTerm(option.campos, campoChave.dominio.id, 'dominio.id')) {
							this.removeItem(chaves, campoChave.dominio.id, 'dominio.id');
						} else {
							if (!this.foundSearchTerm(sessoesChaves, option.codigo, 'codigo')) {
								sessoesChaves.push(option);
							}
						}
					});
				});	
			}
		}
		return this.gerarChaves(sessoesChaves, chaves);
	}

	getCamposPor (optionsSessoes, camposChaves) {
		let campos = [];
		let options = optionsSessoes.slice();
		if (camposChaves.length == 0) return campos;
		options.forEach(option => {
			option.campos.forEach(campo => {
				if (!this.foundSearchTerm(camposChaves, campo.dominio.id, 'campo.dominio.id')
					&& !this.foundSearchTerm(campos, campo.dominio.id, 'campo.dominio.id')) {
					let campoUnidade = {id: null, campo: campo, sessao: option};
					campos.push(campoUnidade);
				}
			});
		});

		return campos;
	}

	foundSearchTerm (object, searchTerm, property) {
		for (var index=0, length = object.length; index < length; index++) {
			let valor = getValueByProperty(object[index], property);
			if (valor === searchTerm) {
				return true;
			}
		}
		
		return false;
	}

	removeItem (items, searchTerm, property) {
		for (var index=0, length = items.length; index < length; index++) {
			let valor = getValueByProperty(items[index], property);
			if (valor === searchTerm) {
				items.splice(index, 1);
				break;
			}
		}
	}

	getSessoesNaoSelecionadas (sessoesSelecionadas) {
		let sessoesNaoSelecionadas = [];
		let sessoes = this.state.sessoes.slice();
		sessoes.forEach(sessao => {
			if (!this.foundSearchTerm(sessoesSelecionadas, sessao.id, 'id')) {
				sessoesNaoSelecionadas.push(sessao);
			}
		})
		return sessoesNaoSelecionadas;
	}

	gerarChaves (sessoesChaves, campos) {
		let chavesUnidadeImportacao = [];
		campos.forEach(campo => {
			let chaveUnidadeImportacao = {id: null, campo: copyObject(campo), sessoes: sessoesChaves};
			chavesUnidadeImportacao.push(chaveUnidadeImportacao);
		});
		return chavesUnidadeImportacao;
	}

	gerarCampos (sessoesChaves, campos) {
		let camposUnidadeImportacao = [];
		campos.forEach(itemCampo => {
			sessoesChaves.forEach(sessao => {
				if (this.foundSearchTerm(sessao.campos, itemCampo.campo.dominio.id, 'dominio.id')) {
					let campoUnidade = {id: null, campo: copyObject(itemCampo.campo), sessao: sessao};
					if (!this.foundSearchTerm(camposUnidadeImportacao, campoUnidade.campo.id, 'campo.id')) {
						camposUnidadeImportacao.push(campoUnidade);
					}
				}
			});
			
		});
		return camposUnidadeImportacao;
	}

	loadLayouts (tipoLayout) {
		let constantTipoLayout = constants.getConstantTipoLayoutPorCodigo(tipoLayout.codigo);
		LayoutService
			.getBy(constantTipoLayout)
			.then (data => {
				this.state.layouts = data;
				let newState = update(this.state, {layouts: {$set: data}});
				this.setState(newState);
			}).catch (error => {
				this.props.template.errorMessage(error);
			});
	}

	formatValue (value) {
		return value.toUpperCase();
	}

	showInsertButtonStyle () {
		return true;
	}
	
	showUpdateButtonStyle () {
		return false;
	}

	showIncluir () {
		return this.state.unidadeSelecionado.id == null || this.state.unidadeSelecionado.id == '';
	}

	showAlterar () {
		return this.state.unidadeSelecionado.id != null && this.state.unidadeSelecionado.id != '';
	}

    render () {
    	return (

    		<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Unidade de Importação por Chave - Cadastro
						</div>
					</div>
					<div className="col-lg-12">
						<div className="navigationAlign">
							<button type="button" className="btn btn-social btn-twitter" id="renderPesquisa" onClick={this.showPesquisa()}><i className="fa fa-long-arrow-left"></i>Voltar</button>
						</div>

						<div className="panel panel-primary" id="cadPanel">
							<div className="panel-heading">
								Cadastro
							</div>
							<div className="panel-body">

									<FormValidator ref="formValidator">

										<TextInput
											id="codigoId" 
											type="text"
											label="Código"
											maxLength="30"
											className="col-sm-5 col-md-5 col-lg-5"
											required={true}
											chave={true}
											formatValue={this.formatValue}
											readOnly={this.state.unidadeSelecionado.id != null}
											showError={this.state.showErrors}
											selectedValue={this.state.unidadeSelecionado.codigo}
											valueChange={this.changeCodigoFieldChanged} />

										<TextInput
											id="descricaoId" 
											type="text"
											label="Descrição"
											maxLength="60"
											className="col-sm-5 col-md-5 col-lg-5"
											required={false}
											readOnly={this.state.unidadeSelecionado.id != null}
											showError={this.state.showErrors}
											selectedValue={this.state.unidadeSelecionado.descricao}
											valueChange={this.changeDescricaoFieldChanged} />

										<SelectInput
											id="tipoLayoutId" 
											placeholder="Selecione..."
											className="col-sm-5 col-md-5 col-lg-5"
											label="Tipo de Layout"
											name={'tipoLayout'}
											options={this.state.tiposLayout}
											selectedValue={this.state.unidadeSelecionado.tipoLayout}
											valueChange={this.changeTipoLayout}
											required={true}
											readOnly={this.state.unidadeSelecionado.id != null}
											showError={this.state.showErrors} />

										<SelectInput
											ref="layoutRef"
											id="layoutId" 
											placeholder="Selecione..."
											className="col-sm-5 col-md-5 col-lg-5"
											label="Layout"
											name={'layout'}
											options={this.state.layouts}
											selectedValue={this.state.unidadeSelecionado.layout}
											valueChange={this.changeLayout}
											required={true}
											readOnly={this.state.unidadeSelecionado.id != null}
											showError={this.state.showErrors} />

										<Picklist
											ref="sessoesRef"
											id="idSessoes"
											label="Código Identificador" 
											className="col-sm-5 col-md-5 col-lg-5"
											options={this.state.sessoes} 
											required={true}
											selectedValue={this.state.unidadeSelecionado.sessoes} 
											textProp="codigo"
											valueProp="id"
											propertyToSort="codigo"
											readOnly={this.state.unidadeSelecionado.id != null}
											onChange={this.changeSessoes}
											showError={this.state.showErrors} />

										<Picklist
											ref="chavesRef" 
											id="idChaves"
											label="Campos Chaves" 
											className="col-sm-5 col-md-5 col-lg-5"
											options={this.state.chaves} 
											required={true}
											selectedValue={this.state.unidadeSelecionado.chavesUnidadeImportacao} 
											textProp="campo.codigo"
											valueProp="campo.dominio.id"
											propertyToSort="campo.codigo"
											propToCheck="sessoes"
											readOnly={this.state.unidadeSelecionado.id != null}
											onChange={this.changeChaves}
											showError={this.state.showErrors}/>

										<Picklist
											ref="camposRef" 
											id="idCampos"
											label="Demais Campos" 
											className="col-sm-5 col-md-5 col-lg-5"
											options={this.state.campos} 
											required={true}
											selectedValue={this.state.unidadeSelecionado.camposUnidadeImportacao} 
											textProp="campo.codigo"
											valueProp="campo.dominio.id"
											propertyToSort="campo.codigo"
											readOnly={this.state.unidadeSelecionado.id != null}
											onChange={this.changeCampos}
											showError={this.state.showErrors}/>											
										
										<ButtonCrud 
											showIncluir={this.showIncluir()} 
											showAlterar={false}
											showConsultar={false}
											label="Novo"
											incluir={this.incluir()} 
											clear={this.limpar()} />

									</FormValidator>
									
							</div>
						</div>
					</div>
				</div>
			</div>
			
        );
    }
}