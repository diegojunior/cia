import React, { Component } from 'react';
import update from 'immutability-helper';
import Agente from '../../../shared/services/Agente';
import Layout from '../../../shared/services/Layout';
import Importacao from '../../../shared/services/Importacao';
import Remetente from '../../../shared/services/Remetente';
import Equivalencia from '../../../shared/services/Equivalencia';
import ValidacaoArquivo from '../../../shared/services/ValidacaoArquivo';
import FormValidator from '../../ui/form/FormValidator';
import UploadFile from '../../ui/upload/UploadFile';
import SelectInput from '../../ui/input/SelectInput';
import DatePicker from '../../ui/date/DatePicker';
import Moment from 'moment';
import Button from '../../ui/button/Button';
import { Link } from 'react-router';
import {constants} from '../../../shared/util/Constants';
import {sort} from '../../../shared/util/Utils';
import FileException from '../../ui/upload/FileException';
import './Style.css';

export default class ImportacaoExecucao extends Component {

	constructor (props) {
		super(props);
		this.state = {
			importacao: {
				id: null, dataImportacao: '', dataExecucao: '', dataConclusao: '', 
				sistema: null, remetente: null, agente: null, tipoLayout: {}, layout: {}, 
				tipoImportacao: {}, arquivo: {}, equivalencias: []},
			importacaoSelecionado : {
				id: null, dataImportacao: '', dataExecucao: '', dataConclusao: '', 
				sistema: null, remetente: null, agente: null, tipoLayout: {}, layout: {}, 
				tipoImportacao: {}, arquivo: {}, equivalencias: []},
			sistemas: constants.getSistemasEnum(),
			agentes: [],
			tiposLayout: constants.getTiposLayouts(),
			layouts: [],
			validacoesImportacao: [],
			tiposImportacao: constants.getTipoImportacao(),
			remetentes: [],
			showErrors: false
		};

		this.incluir = this.incluir.bind(this);
		this.clear = this.clear.bind(this);
		this.limpar = this.limpar.bind(this);
		this.showPesquisa = this.showPesquisa.bind(this);
		this.handlerDataImportacao = this.handlerDataImportacao.bind(this);
		this.handlerSistema = this.handlerSistema.bind(this);
		this.handlerRemetente = this.handlerRemetente.bind(this);
		this.handlerAgente = this.handlerAgente.bind(this);
		this.handlerTipoLayout = this.handlerTipoLayout.bind(this);
		this.handlerLayout = this.handlerLayout.bind(this);
		this.handlerTipoImportacao = this.handlerTipoImportacao.bind(this);
		this.handlerUpload = this.handlerUpload.bind(this);
		this.handlerErrorMessage = this.handlerErrorMessage.bind(this);
		this.checkFileTypes = this.checkFileTypes.bind(this);
		this.validateFile = this.validateFile.bind(this);
	}

	componentDidMount () {
		this.refs.tipoImportacao.setDefaultValue(constants.getTipoImportacao()[0].codigo);
	}

	componentWillReceiveProps (nextProps) {

		if (nextProps.template.state.elementoSelecionado && 
			
			nextProps.template.state.elementoSelecionado.id && 
			nextProps.template.state.elementoSelecionado.id != '') {
			this.refs.formValidator.clearValues();
			let constantSistema = null;
			if (nextProps.template.state.elementoSelecionado.sistema != null) {
				constantSistema = constants.getConstantSistemasPorCodigo(nextProps.template.state.elementoSelecionado.sistema.codigo);					
			}
			let tipoLayoutConstant = constants.getConstantTipoLayoutPorCodigo(nextProps.template.state.elementoSelecionado.layout.tipoLayout.codigo);
			Importacao
				.loadAgenteRemetenteLayouts(constantSistema, tipoLayoutConstant)
				.then (response => {
					let newState = update(this.state, {importacao: {$set: nextProps.template.state.elementoSelecionado}, 
												importacaoSelecionado: {$set: nextProps.template.state.elementoSelecionado},
												agentes: {$set: response.agentes},
												remetentes: {$set: response.remetentes},
												layouts: {$set: response.layouts}});
					this.setState(newState);
				}).catch(error => {
					console.log('errorMessage: ', error)
					this.props.template.errorMessage(error);
				});	


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

    showInsertButtonStyle () {
		return this.state.importacao.id == null;
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
		let importacaoClear = {id: null, dataImportacao: '', dataExecucao: '', dataConclusao: '', sistema: null, remetente: null, agente: null, tipoLayout: {}, layout: {}, tipoImportacao: {}, arquivo: {}, equivalencias: []};
		let newState = update(this.state, {importacao: {$set: importacaoClear}, 
			importacaoSelecionado: {$set: importacaoClear}, showErrors: {$set: false},
			agentes: {$set: []}, layouts: {$set: []}});
		this.setState(newState);

	}

	incluir () {
		
		return (event) => {
			event.preventDefault();
			this.setState({showErrors: true});
			if(this.refs.formValidator.isValid()) {
				this.state.importacao.tipoImportacao = constants.getTipoImportacao()[0];
				Importacao
					.incluir(this.state.importacao)
					.then (data => {
						this.props.template.successMessage('Executando a importação com sucesso.');
						this.props.template.addDatatableElement(data);
						this.props.template.clear();
						this.clear();
					}).catch(error => {
						console.log('errorMessage: ', error)
						this.props.template.errorMessage(error);
					});	
			}
		}
	}

	handlerDataImportacao (value) {
		let newState = update(this.state, {importacao: {dataImportacao: {$set: value}, dataExecucao: {$set: new Moment()}}});
		this.setState(newState);
	}

	handlerSistema (value) {
		let sistema = constants.getSistemasPorCodigo(value);
		if (sistema != null) {
			let constantSistema = constants.getConstantSistemasPorCodigo(sistema.codigo);
			Importacao
				.loadAgenteRemetente(constantSistema)
				.then (data => {
					let newState = update(this.state, {importacao: {sistema: {$set: sistema}}, 
						agentes: {$set: data.agentes}, 
						remetentes: {$set: data.remetentes}});
					this.setState(newState);
				}).catch(error => {
					this.props.template.errorMessage(error);
				});	
		} else {
			this.state.agentes = [];
			this.state.remetentes = [];
			this.refs.agente.forceClear();
			this.refs.remetente.forceClear();
			let newState = update(this.state, {importacao: {agente: {$set: null},
				remetente: {$set: null}, 
				sistema: {$set: null}}});
			this.setState(newState);
		}
		
      	
	}

	handlerAgente (value) {
		let agente = this.getAgenteByCodigo(value);
		let newState = update(this.state, {importacao: {agente: {$set: agente}}});
		this.setState(newState);
	}

	handlerRemetente (value) {
		if (value != '' && value != null)  {
			let constantSistema = constants.getConstantSistemasPorCodigo(this.state.importacao.sistema.codigo);
			let remetente = this.getRemetente(value);
			let codigoTipoEquivalenciaCorretora = '23';
			let tipoEquivalencia = constants.getConstantTipoEquivalenciaCodigo(codigoTipoEquivalenciaCorretora);
			Equivalencia
				.getBy(constantSistema, value, tipoEquivalencia)
				.then (data => {
					let newState = update(this.state, {importacao: {remetente: {$set: remetente}, equivalencias: {$set: data}}});
					this.setState(newState);
				}).catch (error => {
					this.props.template.errorMessage(error);
				});

		} else {

			let newState = update(this.state, {importacao: {remetente: {$set: {}}}});
			this.setState(newState);

		}
	}

	handlerTipoLayout (value) {
		let tipoLayout = constants.getTipoLayoutPorCodigo(value);
		let tipoLayoutConstant = constants.getConstantTipoLayoutPorCodigo(value);
		Layout
			.getBy(tipoLayoutConstant)
			.then (data => {
				sort(data, 'codigo');
				let newState = update(this.state, {importacao: {tipoLayout: {$set: tipoLayout}}, layouts: {$set: data}});
				this.setState(newState);
			}).catch(error => {
				this.props.template.errorMessage(error);
			});
	}

	handlerLayout (value) {
		let layout = this.getLayoutByCodigo(value);
		if (layout != null) {
			ValidacaoArquivo
				.getBy(null, layout.codigo, null, null)
				.then (data => {
					this.state.validacoes = data;
					let newState = update(this.state, {importacao: {layout: {$set: layout}}});
					this.setState(newState);
				}).catch(error => {
					this.props.template.errorMessage(error);
				});
		} else {
			let newState = update(this.state, {importacao: {layout: {$set: layout}}});
			this.setState(newState);
		}
		
	}

	handlerTipoImportacao (value) {
		let tipoImportacao = constants.getTipoImportacaoPorCodigo(value);
		let newState = update(this.state, {importacao: {tipoImportacao: {$set: tipoImportacao}}});
		this.setState(newState);
	}

	handlerUpload (file) {
		let newState = update(this.state, {importacao: {arquivo: {$set: file}}});
		this.setState(newState);
	}

	handlerErrorMessage (msg) {
		this.props.template.errorMessage(msg);
	}

	checkFileTypes (type) {
		console.log(type)
		let fileTypes = constants.getFileTypesEnum();
		if (this.state.importacao.tipoLayout != null) {
			let patternFiles = constants.getPatternFileTipoLayoutPorCodigo(this.state.importacao.tipoLayout.codigo);
			for (let item in fileTypes) {
				if (fileTypes[item].codigo == type) {
					let found = patternFiles.find((pattern) => {
						return pattern == type;
					});
					return (found);
				}
			}	
		}
		
		return false;
	}

	validateFile (file) {
		if (file.size == 0) {
			throw new FileException('Arquivo em branco!'); 
		}
		for (let index in this.state.validacoes) {
			let validacao = this.state.validacoes[index];
			let isOk = true;
			if (validacao.localValidacao.codigo == 'EXT') {
				if (validacao.campoValidacao.codigo == 'COR') {
					isOk = this.validateCorretora(file.name, validacao);
				} 
				if (validacao.campoValidacao.codigo == 'DAT') {
					isOk = this.validateData(file.name, validacao);
				}	
				
			}
			if (!isOk) {
				throw new FileException('Corretora e(ou) Data selecionada não identificada. Verifique Validação.');
			}
		}
	}

	validateCorretora (fileName, validacao) {
		if (this.state.importacao.agente) {
			let equivalencia = this.getEquivalenciaBy(this.state.importacao.agente.codigo);
			let codigoCorretoraArquivo = fileName.substring(validacao.posicaoInicial-1, validacao.posicaoFinal);
			if (equivalencia != null) {
				if (equivalencia.codigoInterno.toUpperCase() == codigoCorretoraArquivo.toUpperCase()
					|| equivalencia.codigoExterno.toUpperCase() == codigoCorretoraArquivo.toUpperCase()) {
					return true;
				}
			}
			return false;	
		}
	}

	validateData (fileName, validacao) {
		if (this.state.importacao.dataImportacao) {
			let dataImportacao = Moment(this.state.importacao.dataImportacao, 'YYYY-MM-DDThh:mm:ssZ');
			let dataImportacaoArquivo = fileName.substring(validacao.posicaoInicial-1, validacao.posicaoFinal);
			let formatoData = validacao.formato.toUpperCase();
			let dataFormatada = dataImportacao.format(formatoData);
			if (dataFormatada == dataImportacaoArquivo) {
				return true;
			}
			return false;	
		}
	}

	getEquivalenciaBy (codigo) {
		if (this.state.importacao.equivalencias) {
			for (let index in this.state.importacao.equivalencias) {
				let equivalencia = this.state.importacao.equivalencias[index];
				if (equivalencia.codigoInterno == codigo) {
					return equivalencia;
				}
			}
			return null;
		}
	}

	getAgenteByCodigo (codigo) {
		for (let index in this.state.agentes) {
			if (this.state.agentes[index].codigo == codigo) {
				return this.state.agentes[index];
			}
		}
		return null;
	}

	getLayoutByCodigo (codigo) {
		for (let index in this.state.layouts) {
			if (this.state.layouts[index].codigo == codigo) {
				return this.state.layouts[index];
			}
		}
		return null;
	}

	getRemetente(value) {
		
		for (var index in this.state.remetentes) {
			var remetente = this.state.remetentes[index];
			if (remetente.codigo == value) {
				return remetente;
			}
		}
		return {};

	}

	disabled () {
		return this.state.importacaoSelecionado.id != null
	}

	enabled () {
		return this.state.importacaoSelecionado.id == null
	}

	disabledUpload () {
		if (this.state.importacaoSelecionado.id != null) {
			return true;
		} else if (this.state.importacao.layout == null
			|| 'undefined' === typeof this.state.importacao.layout.codigo) {
			return true;
		} 
		return false;
	}
	
	render () {
		return (
			<div className="cia-animation-fadein">
    			<div className="row">
    				<div className="col-lg-12">
    					<div className="page-header">
    						<Link to="/cia/home" className="fa fa-home"></Link> - Importação - Execução
    					</div>
    				</div>

    				<form role="form" name="form">
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


										<DatePicker
											id="dataImportacaoId"
											label="Data Importação"
											className="col-sm-5 col-md-5 col-lg-5"
											required={true}
											showError={this.state.showErrors}
											readOnly={this.disabled()}
											selectedValue={this.state.importacaoSelecionado.dataImportacao}
											valueChange={this.handlerDataImportacao} />

										<SelectInput
											id="tipoLayoutId" 
											placeholder="Selecione..."
											className="col-sm-5 col-md-5 col-lg-5"
											label="Tipo de Layout"
											name={'tipoLayout'}
											readOnly={this.disabled()}
											options={this.state.tiposLayout}
											selectedValue={this.state.importacaoSelecionado.layout.tipoLayout}
											valueChange={this.handlerTipoLayout}
											required={true}
											showError={this.state.showErrors} />

										<SelectInput
											id="layoutId" 
											placeholder="Selecione..."
											className="col-sm-5 col-md-5 col-lg-5"
											label="Layout"
											name={'layout'}
											readOnly={this.disabled()}
											options={this.state.layouts}
											selectedValue={this.state.importacaoSelecionado.layout}
											valueChange={this.handlerLayout}
											required={true}
											showError={this.state.showErrors} />

										<SelectInput
											id="sistemaId" 
											placeholder="Selecione..."
											className="col-sm-5 col-md-5 col-lg-5"
											label="Sistema"
											name={'sistema'}
											readOnly={this.disabled()}
											options={this.state.sistemas}
											selectedValue={this.state.importacaoSelecionado.sistema}
											valueChange={this.handlerSistema}
											required={false}
											showError={this.state.showErrors} />

										<SelectInput
											id="agenteId"
											ref="agente" 
											placeholder="Selecione..."
											className="col-sm-5 col-md-5 col-lg-5"
											label="Corretora"
											name={'agente'}
											readOnly={this.disabled()}
											options={this.state.agentes}
											selectedValue={this.state.importacaoSelecionado.agente}
											valueChange={this.handlerAgente}
											required={false}
											showError={this.state.showErrors} />

										<SelectInput 
											id="remetente"
											ref="remetente"
											placeholder="Selecione..."
											name={'remetente'}
											label="Remetente"
											options={this.state.remetentes}
											className="col-sm-5 col-md-5 col-lg-5"
											valueChange={this.handlerRemetente}
											selectedValue={this.state.importacaoSelecionado.remetente} 
											required={false}
											showError={this.state.showErrors} 
											readOnly={this.disabled()}/>

										<SelectInput
											id="tipoId" 
											ref="tipoImportacao"
											placeholder="Selecione..."
											className="col-sm-5 col-md-5 col-lg-5"
											label="Tipo de Importação"
											name={'layout'}
											readOnly={true}
											options={this.state.tiposImportacao}
											selectedValue={this.state.importacaoSelecionado.tipoImportacao}
											valueChange={this.handlerTipoImportacao}
											required={true}
											showError={this.state.showErrors} />

										<UploadFile
											id="uploadImportacaoId"
											label="Arquivo"
											className="col-sm-5 col-md-5 col-lg-5"
											required={true}
											readOnly={this.disabledUpload()}
											fileType={this.checkFileTypes}
											handlerErrorMessage={this.handlerErrorMessage}
											selectedValue={this.state.importacaoSelecionado.arquivo.fileName}
											valueChange={this.handlerUpload}
											validateFile={this.validateFile}
											showError={this.state.showErrors}/>	

										<Button
											className="btn btn-social btn-success"
											id="btnImportar"
											show={this.enabled()}
											onClick={this.incluir()}
											classFigure="glyphicon glyphicon-import"
											label="Importar" />
										
										<Button
											className="btn btn-social btn-default"
											id="btnNovo"
											show={!this.enabled()}
											onClick={this.limpar()}
											classFigure="glyphicon glyphicon-import"
											label="Nova Importação" />

									</FormValidator>
		    						
		    					</div>
		    				</div>
	    				</div>
    				</form>
    			</div>
    		</div>
				
		);
	}
}