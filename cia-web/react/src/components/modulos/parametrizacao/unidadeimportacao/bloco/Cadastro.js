import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../../ui/form/FormValidator';
import TextInput from '../../../../ui/input/TextInput';
import SelectInput from '../../../../ui/input/SelectInput';
import AddRemoveMultiplesInputs from './UnidadeBlocoMultiplosInputs';
import ButtonCrud from '../../../../ui/button/ButtonCrud';
import UnidadeService from '../../../../../shared/services/UnidadeImportacaoBloco';
import LayoutService from '../../../../../shared/services/Layout';
import {constants} from '../../../../../shared/util/Constants';
import {sort} from '../../../../../shared/util/Utils';
import './Style.css';

export default class Cadastro extends Component {
	
	constructor(props) {
		super(props);

		this.state = {
			unidade: {id: null, codigo: '', descricao: '',  layout: {}, sessaoAbertura: {}, sessaoFechamento: {}, linhasBloco: []},
			unidadeSelecionado: {id: null, codigo: '', descricao: '',  layout: {}, sessaoAbertura: {}, sessaoFechamento: {}, linhasBloco: []},
			tiposLayout: constants.getTiposLayouts(),
			layouts: [],
			sessoes: [],
			campos: [],
			camposSelecionados: [],
			possuiDuplicidade: false,
			linhasBloco: [],
			fields: [],
			showErrors: false
		};

		this.incluir = this.incluir.bind(this);
		this.limpar = this.limpar.bind(this);
		this.isValid = this.isValid.bind(this);
		
		this.changeCodigo = this.changeCodigo.bind(this);
		this.changeDescricao = this.changeDescricao.bind(this);
		this.changeLayout = this.changeLayout.bind(this);
		this.changeSessaoAbertura = this.changeSessaoAbertura.bind(this);
		this.changeSessaoFechamento = this.changeSessaoFechamento.bind(this);

		this.carregaSessoes = this.carregaSessoes.bind(this);
		this.carregaCampos = this.carregaCampos.bind(this);
		
		this.showPesquisa = this.showPesquisa.bind(this);

		this.refresh = this.refresh.bind(this);
		this.checkDuplicity = this.checkDuplicity.bind(this);

	}

	componentWillMount () {
		this.loadLayouts();

		this.state.fields= [
			{
				key:'sessao', 
				label:'Sessão',
				placeholder: "Selecione...",
				options: this.carregaSessoes,
				required: true
			},
			{
				key:'campo', 
				label:'Campo',
				placeholder: "Selecione...",
				options: this.carregaCampos,
				required: true
			}
		]

		/*this.state.fieldsConfig = [
			{	key:'sessao', 
				label:'Sessão', 
				chave: true,
				required: true, 
				type: 'select',
				placeholder: "Selecione...", 
				initialValue: {id: null, codigo: ''}, 
				options: this.carregaSessoes
			},
			{	key:'campo', 
				label:'Campo',
				chave: true, 
				required: true, 
				type: 'select',
				initialValue: {id: null, codigo: ''}, 
				placeholder: "Selecione...", 
				options: this.carregaCampos
			}			
		];*/
	}

	componentDidMount () {
		this.refs.tipoLayout.setDefaultValue(this.state.tiposLayout[0].codigo);
	}

	carregaSessoes () {
		return this.state.sessoes;
	}

	carregaCampos (index) {
		if (this.state.sessoes != null && this.state.sessoes.length > 0) { 
			return this.getCamposSessaoSelecionada (index);
		}
		return [];
	}

	getCamposSessaoSelecionada (index) {
		var rows = this.refs.addRemoveMultiplesInputs.getFields();
		var codigoSessao = rows[index]['sessao'+index].codigo;

		for (var indexSessao in this.state.sessoes) {
			var sessao = this.state.sessoes[indexSessao];
			if (sessao.codigo == codigoSessao) {
				return sessao.campos;
			}
		}
		return [];
	}

	componentWillReceiveProps (nextProps) {

		if (nextProps.template.state.elementoSelecionado && 
			nextProps.template.state.elementoSelecionado.id && 
			nextProps.template.state.elementoSelecionado.id != '') {

			this.refs.formValidator.clearValues();

			this.state.sessoes = nextProps.template.state.elementoSelecionado.layout.sessoes;
			this.state.camposSelecionados = [];
			for (var index in nextProps.template.state.elementoSelecionado.linhasBloco) {
				var linha = nextProps.template.state.elementoSelecionado.linhasBloco[index];
				for (var indexCampo in linha.campos) {
					var campo = linha.campos[indexCampo];
					this.state.camposSelecionados.push({sessao: linha.sessao, campo: campo});
				}
			}

			let newState = update(this.state, {
				unidade: {$set: nextProps.template.state.elementoSelecionado},
				unidadeSelecionado: {$set: nextProps.template.state.elementoSelecionado}
			});

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

		let unidadeClear = {id: null, codigo: '', descricao: '',  layout: {}, sessaoAbertura: {}, sessaoFechamento: {}, linhasBloco: []};
		this.state.camposSelecionados = [];
		let newState = update(this.state, {
									unidade: {$set: unidadeClear}, 
									unidadeSelecionado: {$set: unidadeClear}, 
									showErrors: {$set: false}
								});
		this.setState(newState);
	}

	incluir () {
		
		return (event) => {
			event.preventDefault();

			this.setState({showErrors: true});
			if(this.isValid()) {
				UnidadeService
					.incluir (this.state.unidade)
					.then (data => {
						this.props.template.successMessage('Unidade de importação incluída com sucesso.');
						this.props.template.addDatatableElement(data);
						this.props.template.clear();
						this.clear();
					}).catch (error => {
						this.props.template.warningMessage(error);
					});
			}
		}
	}

	isValid () {
		return this.refs.formValidator.isValid() && !this.state.possuiDuplicidade;
	}

	changeCodigo (value) {
		let newState = update(this.state, {unidade: {codigo: {$set: value}}});
		this.setState(newState);
	}

	changeDescricao (value) {
		let newState = update(this.state, {unidade: {descricao: {$set: value}}});
		this.setState(newState);
	}

	changeLayout (codigoLayout) {
		this.state.unidade.sessaoAbertura = {};
		this.state.unidade.sessaoFechamento = {};
		this.refs.sessaoAbertura.forceClear();
		this.refs.sessaoFechamento.forceClear();
		this.refs.addRemoveMultiplesInputs.clearAllErrors();
		this.refs.addRemoveMultiplesInputs.limpar();
		this.refs.formValidator.addRequiredError('sessao0', 'Sessão');
		this.refs.formValidator.addRequiredError('campo0', 'Campo');
		if (codigoLayout == null) {
			let newState = update(this.state, {unidade: {layout: {$set: null}}});
			this.setState(newState);
		} else {
			for (var index in this.state.layouts) {
				let layoutSelecionado = this.state.layouts[index];			
				if(codigoLayout == layoutSelecionado.codigo) {
					this.state.sessoes = layoutSelecionado.sessoes;
					let newState = update(this.state, {unidade: {layout: {$set: layoutSelecionado}}});
					this.setState(newState);
					break;
				}
			}
		}
	}

	changeSessaoAbertura (value) {
		if (value != null && value != '') {
			for (var index in this.state.sessoes) {
				var sessao = this.state.sessoes[index];
				if (value == sessao.codigo) {
					var isMesmaSessaoFechamento = 
					this.isMesmaSessaoAberturaEFechamento(sessao, this.state.unidade.sessaoFechamento);
					if (isMesmaSessaoFechamento) {
						this.props.template.warningMessage('A Abertura do Bloco deve ser Diferente do Fechamento do Bloco');
						let newState = update(this.state, {unidade: {sessaoAbertura: {$set: {}}}});
						this.setState(newState);
						return;
					}
					let newState = update(this.state, {unidade: {sessaoAbertura: {$set: sessao}}});
					this.setState(newState);
					return;
				}
			}
		} else {
			let newState = update(this.state, {unidade: {sessaoAbertura: {$set: {}}}});
			this.setState(newState);
		}
	}

	changeSessaoFechamento (value) {
		if (value != null && value != '') {
			for (var index in this.state.sessoes) {
				var sessao = this.state.sessoes[index];
				if (value == sessao.codigo) {
					var isMesmaSessaoAbertura = 
						this.isMesmaSessaoAberturaEFechamento(this.state.unidade.sessaoAbertura, sessao);
					if (isMesmaSessaoAbertura) {
						this.props.template.warningMessage('O Fechamento do Bloco deve ser Diferente da Abertura do Bloco');
						let newState = update(this.state, {unidade: {sessaoFechamento: {$set: {}}}});
						this.setState(newState);
						return;
					}
					let newState = update(this.state, {unidade: {sessaoFechamento: {$set: sessao}}});
					this.setState(newState);
					return;
				}
			}
		} else {
			let newState = update(this.state, {unidade: {sessaoFechamento: {$set: {}}}});
			this.setState(newState);
		}
	}

	isMesmaSessaoAberturaEFechamento (sessaoAbertura, sessaoFechamento) {
		return sessaoAbertura != null && sessaoFechamento != null && 
		sessaoAbertura.codigo == sessaoFechamento.codigo;
	}

	loadLayouts () {
		let constantTipoLayout = constants.getConstantTipoLayoutPorCodigo('DEL');
		LayoutService
			.getBy(constantTipoLayout)
			.then (data => {
				sort(data, 'codigo');
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

	showIncluir () {
		return this.state.unidadeSelecionado.id == null || this.state.unidadeSelecionado.id == '';
	}

	showAlterar () {
		return this.state.unidadeSelecionado.id != null && this.state.unidadeSelecionado.id != '';
	}

	refresh (rows) {
		var fields = rows;
		var linhasBloco = [];

		for (var indexField in fields) {
			var field = fields[indexField];

			var sessao = field['sessao'+indexField] ? field['sessao'+indexField].codigo : '';
			var campo = field['campo'+indexField] ? field['campo'+indexField].codigo : '';

			var linhaBloco = {sessao: this.getSessao(sessao), 
						      campo: this.getCampo(campo)};

			if (!this.isExisteDuplicidade(linhasBloco, linhaBloco)) {
				linhasBloco.push(linhaBloco);
			}
		}
	
		this.state.unidade.linhasBloco = this.formataLinhas(linhasBloco);
	}

	formataLinhas (linhas) {
		var linhasBloco = [];

		for (var indexLinha in linhas) {
			var linha = linhas[indexLinha];
			if (linhasBloco.length == 0) {
				linhasBloco.push({sessao: linha.sessao, campos: new Array(linha.campo)});
			} else {
				var indexSessaoExistente = this.sessaoExistente(linhasBloco, linha.sessao);
				if (indexSessaoExistente != null) {
					linhasBloco[indexSessaoExistente].campos.push(linha.campo);
				} else {
					linhasBloco.push({sessao: linha.sessao, campos: new Array(linha.campo)});
				}
			}
		}
		return linhasBloco;
	}

	sessaoExistente (linhas, sessao) {
		for (var index in linhas) {
			var linha = linhas[index];
			if (linha.sessao.codigo == sessao.codigo) {
				return index;
			}
		}
		return null;
	}

	checkDuplicity (stateParam) {
		
		var fields = stateParam.rows;

		var linhasBloco = [];

		for (var indexField in fields) {
			var field = fields[indexField];

			var sessao = field['sessao'+indexField] ? field['sessao'+indexField].codigo : '';
			var campo = field['campo'+indexField] ? field['campo'+indexField].codigo : '';			

			var linhaBloco = {sessa: this.getSessao(sessao),
						      campo: this.getCampo(campo)};

			if (this.isExisteDuplicidade(linhasBloco, linhaBloco)) {
				return this.refs.addRemoveMultiplesInputs.removeRowToState(stateParam);
			} else {
				linhasBloco.push(linhaBloco);
			}
		}

		return stateParam;
	}

	isExisteDuplicidade (linhasBloco, linhaBloco) {
		for (var index in linhasBloco) {
			var linha = linhasBloco[index];
			if (linha.sessao == linhaBloco.sessao &&
				linha.campo == linhaBloco.campo) {
					this.props.template.warningMessage('Chave(s) Duplicada(s).');
					this.state.possuiDuplicidade = true;
					return true;
				}
		}
		this.state.possuiDuplicidade = false;
		return false;
	}

	getSessao (codigo) {
		for (var index in this.state.sessoes) {
			var sessao = this.state.sessoes[index];
			if (sessao.codigo == codigo) {
				return sessao;
			}
		}
		return {};
	}

	getCampo (codigo) {
		for (var index in this.state.sessoes) {
			var sessao = this.state.sessoes[index];
			for (var indexCampo in sessao.campos) {
				var campo = sessao.campos[indexCampo];
				if (campo.codigo == codigo) {
					return campo;
				}
			}
		}
		return {};
	}

    render () {
    	return (

    		<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Unidade de Importação por Bloco - Cadastro
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
										id="codigo" 
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
										valueChange={this.changeCodigo} />

									<TextInput
										id="descricao" 
										type="text"
										label="Descrição"
										maxLength="60"
										className="col-sm-5 col-md-5 col-lg-5"
										required={false}
										readOnly={this.state.unidadeSelecionado.id != null}
										showError={this.state.showErrors}
										selectedValue={this.state.unidadeSelecionado.descricao}
										valueChange={this.changeDescricao} />

									<SelectInput
										ref="tipoLayout"
										id="tipoLayout"
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Tipo de Layout"
										name={'tipoLayout'}
										options={this.state.tiposLayout}
										required={true}
										readOnly={true} 
										showError={this.state.showErrors} />

									<SelectInput
										ref="layout"
										id="layout" 
										chave={true}
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

									<SelectInput
										ref="sessaoAbertura"
										id="sessaoAbertura" 
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Abertura do Bloco"
										name={'sessaoAbertura'}
										options={this.state.sessoes}
										selectedValue={this.state.unidadeSelecionado.sessaoAbertura}
										valueChange={this.changeSessaoAbertura}
										required={true}
										readOnly={this.state.unidadeSelecionado.id != null}
										showError={this.state.showErrors} />

									<SelectInput
										ref="sessaoFechamento"
										id="sessaoFechamento" 
										placeholder="Selecione..."
										className="col-sm-5 col-md-5 col-lg-5"
										label="Fechamento do Bloco"
										name={'sessaoFechamento'}
										options={this.state.sessoes}
										selectedValue={this.state.unidadeSelecionado.sessaoFechamento}
										valueChange={this.changeSessaoFechamento}
										required={true}
										readOnly={this.state.unidadeSelecionado.id != null}
										showError={this.state.showErrors} />

									<AddRemoveMultiplesInputs 
										ref="addRemoveMultiplesInputs"
										label="Linhas do Bloco"
										fields={this.state.fields}
										valueChange={this.refresh}
										selectedValue={this.state.camposSelecionados}
										showError={this.state.showErrors} 
										readOnly={this.state.unidadeSelecionado.id != null}
										displayPlusMinus={this.state.unidadeSelecionado.id == null}
										checkRowDuplicity={this.checkDuplicity }/>

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