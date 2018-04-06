import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../../ui/form/FormValidator';
import TextInput from '../../../../ui/input/TextInput';
import SelectInput from '../../../../ui/input/SelectInput';
import ButtonCrud from '../../../../ui/button/ButtonCrud';
import ValidacaoService from '../../../../../shared/services/ValidacaoArquivo';
import LayoutService from '../../../../../shared/services/Layout';
import {constants} from '../../../../../shared/util/Constants';
import {sort} from '../../../../../shared/util/Utils';
import './Style.css';

export default class Cadastro extends Component {
	
	constructor(props) {
		super(props);

		this.state = {
				validacao: {id: null, campoValidacao: '', tipoLayout: '', layout: null, localValidacao: '', posicaoInicial: null, posicaoFinal: null, formato: '', sessaoLayout: null, campoLayout: null},
				validacaoSelecionada: {id: null, campoValidacao: '', tipoLayout: '', layout: null, localValidacao: '', posicaoInicial: null, posicaoFinal: null, formato: '', sessaoLayout: null, campoLayout: null},

				tiposLayouts: [],
				layouts: [],
				camposValidacoes: [],
				locaisValidacoes: [],
				sessoesLayout: [],
				camposLayout: [],

				showValidacaoExterna: false,
				showValidacaoInterna: false,

				showErrors: false
				
			};

		this.incluir = this.incluir.bind(this);
		this.alterar = this.alterar.bind(this);
		this.limpar = this.limpar.bind(this);

		this.isValido = this.isValido.bind(this);

		this.changeTipoLayout = this.changeTipoLayout.bind(this);
		this.changeLayout = this.changeLayout.bind(this);
		this.changeCampoValidacao = this.changeCampoValidacao.bind(this);
		this.changeLocalValidacao = this.changeLocalValidacao.bind(this);
		this.changeSessaoLayout = this.changeSessaoLayout.bind(this);
		this.changeCampoLayout = this.changeCampoLayout.bind(this);
		this.changePosicaoInicial = this.changePosicaoInicial.bind(this);
		this.changePosicaoFinal = this.changePosicaoFinal.bind(this);
		this.changeFormato = this.changeFormato.bind(this);
		
		this.showPesquisa = this.showPesquisa.bind(this);

		this.showPorLocalValidacao = this.showPorLocalValidacao.bind(this);

	}

	componentWillMount () {
		this.state.tiposLayouts = constants.getTiposLayouts();
		this.state.camposValidacoes = constants.getCamposValidacoes();
		this.state.locaisValidacoes = constants.getLocaisValidacoes();		
	}

	componentWillReceiveProps (nextProps) {

		var elemento = nextProps.template.state.elementoSelecionado;
		this.state.layouts = [];

		if (elemento && elemento.id && elemento.id != '') {

			this.refs.formValidator.clearValues();

			this.state.layouts = [elemento.layout];

			this.showPorLocalValidacao(elemento.localValidacao.codigo);

			if (this.state.showValidacaoInterna) {
				this.state.sessoesLayout = elemento.layout.sessoes;
				this.state.camposLayout = elemento.sessaoLayout.campos;
			}

			this.setState({validacao: elemento, 
						   validacaoSelecionada: elemento});

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

		let validacaoClear = {id: null, campoValidacao: '', tipoLayout: '', layout: null, localValidacao: '', posicaoInicial: null, posicaoFinal: null, formato: '', sessaoLayout: null, campoLayout: null};

		this.state.showValidacaoExterna = false;
		this.state.showValidacaoInterna = false;

		let newState = update(this.state, {validacao: {$set: validacaoClear}, validacaoSelecionada: {$set: validacaoClear}, showErrors: {$set: false}});
		this.setState(newState);
	}

	incluir () {
		
		return (event) => {
			event.preventDefault();

			this.setState({showErrors: true});

			if (this.state.showValidacaoExterna) {
				this.refs.formValidator.removeError('sessaoLayout');
				this.refs.formValidator.removeError('campoLayout');
			} else {
				this.refs.formValidator.removeError('posicaoInicial');
				this.refs.formValidator.removeError('posicaoFinal');
				this.refs.formValidator.removeError('formato');
			}

			if (this.isValido()) {
				
				ValidacaoService
					.incluir(this.state.validacao)
						.then (data => {
							this.props.template.successMessage('Validação Incluída com Sucesso.');
							this.props.template.addDatatableElement(data);
							this.props.template.clear();
						}).catch(error => {
							this.props.template.errorMessage(error);
						});
			}
		}
	}

	alterar () {

		return (event) => {

			event.preventDefault();

			let validacaoClear = {id: null, campoValidacao: '', tipoLayout: '', layout: null, localValidacao: '', posicaoInicial: null, posicaoFinal: null, formato: '', sessaoLayout: null, campoLayout: null};

			this.setState({validacaoSelecionada: validacaoClear, showErrors: true});

			if (this.state.showValidacaoExterna) {
				this.refs.formValidator.removeError('sessaoLayout');
				this.refs.formValidator.removeError('campoLayout');
			} else {
				this.refs.formValidator.removeError('posicaoInicial');
				this.refs.formValidator.removeError('posicaoFinal');
				this.refs.formValidator.removeError('formato');
			}

			if (this.isValido()) {
				ValidacaoService
					.alterar(this.state.validacao)
						.then(data => {
							this.props.template.successMessage('Validação Alterada com Sucesso.');
							this.props.template.alterDatatableElement(data);
							this.props.template.clear();
							this.clear();	
						}).catch(error => {
							this.props.template.errorMessage(error);	
						});
			}
		}
	}

	isValido () {

		if (this.state.showValidacaoExterna) {
			if (Number(this.state.validacao.posicaoInicial) >= Number(this.state.validacao.posicaoFinal)) {
				this.props.template.errorMessage("O Campo 'Posição Inicial' deve ser menor que o Campo 'Posição Final'");
				return false;
			}
		}

		return this.refs.formValidator.isValid();
	}

	changeCampoValidacao (value) {
		
		if (value != '') {
			var campoValidacao = constants.getCampoValidacaoPorCodigo(value);
			let newState = update(this.state, {validacao: {campoValidacao: {$set: campoValidacao}}});
			this.setState(newState);
	
		} else {
			let newState = update(this.state, {validacao: {campoValidacao: {$set: ''}}});
			this.setState(newState);
		}
	}

	changeTipoLayout (value) {
		var tipoLayout = constants.getTipoLayoutPorCodigo(value);

		if (value != '') {
			let tipoLayoutConstant = constants.getConstantTipoLayoutPorCodigo(value);
			LayoutService.getBy(tipoLayoutConstant)
				.then(data => {
					this.state.layouts = data;
					sort(this.state.layouts, "codigo");
					let newState = update(this.state, {validacao: {tipoLayout: {$set: tipoLayout}}});

					this.setState(newState);
				}).catch(error => {
					
					this.props.template.errorMessage(error);

					this.state.layouts = [];
					let newState = update(this.state, {validacao: {tipoLayout: {$set: tipoLayout}}});
					this.setState(newState);
				});

		} else {
			this.state.layouts = [];
			let newState = update(this.state, {validacao: {tipoLayout: {$set: tipoLayout}}});
			this.setState(newState);
		}
	}

	changeLayout (value) {
		if (value != '') {
			var layout = this.getLayout(value);
			this.state.sessoesLayout = layout.sessoes;
			sort(this.state.sessoesLayout, "codigo")
			let newState = update(this.state, {validacao: {layout: {$set: layout}}});
			this.setState(newState);
			
		} else {
			this.state.sessoesLayout = [];
			this.state.camposLayout = [];

			let newState = update(this.state, {validacao: {layout: {$set: null}}});
			this.setState(newState);
		}
	}

	changeLocalValidacao (value) {
		this.showPorLocalValidacao(value);

		var localValidacao = constants.getLocalValidacaoPorCodigo(value);

		this.refs.posicaoInicial.forceClear();
		this.refs.posicaoFinal.forceClear();
		this.refs.formato.forceClear();
		this.refs.sessaoLayout.forceClear();
		this.refs.campoLayout.forceClear();
	
		this.refs.formValidator.addRequiredError('posicaoInicial', 'Posição Inicial');
		this.refs.formValidator.addRequiredError('posicaoFinal', 'Posição Final');
		this.refs.formValidator.addRequiredError('formato', 'Formato');
		this.refs.formValidator.addRequiredError('sessaoLayout', 'Sessão do Layout');
		this.refs.formValidator.addRequiredError('campoLayout', 'Campo do Layout');
		
		let newState = update(this.state, {validacao: {localValidacao: {$set: localValidacao}}});
		this.setState(newState);
	}

	changeSessaoLayout (value) {
		if (value != '') {
			var sessao = this.getSessaoLayout(value);
			this.state.camposLayout = sessao.campos;
			sort(this.state.camposLayout, "codigo")
			this.refs.campoLayout.clear();
			
			let newState = update(this.state, {validacao: {sessaoLayout: {$set: sessao}}});
			this.setState(newState);
			
		} else {
			this.state.camposLayout = [];

			let newState = update(this.state, {validacao: {sessaoLayout: {$set: null}}});
			this.setState(newState);
		}
	}

	changeCampoLayout (value) {
		if (value != '') {
			var campo = this.getCampoLayout(value);
			
			let newState = update(this.state, {validacao: {campoLayout: {$set: campo}}});
			this.setState(newState);
			
		} else {

			let newState = update(this.state, {validacao: {campoLayout: {$set: null}}});
			this.setState(newState);
		}
	}

	changePosicaoInicial (value) {
		let newState = update(this.state, {validacao: {posicaoInicial: {$set: value}}});
		this.setState(newState);
	}

	changePosicaoFinal (value) {
		let newState = update(this.state, {validacao: {posicaoFinal: {$set: value}}});
		this.setState(newState);
	}

	changeFormato (value) {
		let newState = update(this.state, {validacao: {formato: {$set: value}}});
		this.setState(newState);
	}

	showPorLocalValidacao(value) {
		if (value == 'EXT') {
			this.state.showValidacaoExterna = true;
			this.state.showValidacaoInterna = false;
		} else if (value == 'INT') {
			this.state.showValidacaoExterna = false;
			this.state.showValidacaoInterna = true;
		} else {
			this.state.showValidacaoExterna = false;
			this.state.showValidacaoInterna = false;
		}
	}

	getLayout(value) {
		for (var index in this.state.layouts) {
			var layout = this.state.layouts[index];
			if (layout.codigo == value) {
				return layout;
			}
		}
		return {};
	}

	getSessaoLayout(value) {

		for (var indexLayout in this.state.layouts) {
			var layout = this.state.layouts[indexLayout];
			if (this.state.validacao.layout) {
				if (this.state.validacao.layout.codigo == layout.codigo) {
					for (var indexSessao in layout.sessoes) {
						var sessao = layout.sessoes[indexSessao];
						if (sessao.codigo == value) {
							return sessao;
						}
					}
				}
			}
			
		}
		
		return {};
	}

	getCampoLayout(value) {

		for (var indexLayout in this.state.layouts) {
			var layout = this.state.layouts[indexLayout];
			if (this.state.validacao.layout) {
				if (this.state.validacao.layout.codigo == layout.codigo) {
					for (var indexSessao in layout.sessoes) {
						var sessao = layout.sessoes[indexSessao];
						if (this.state.validacao.sessaoLayout) {
							if (this.state.validacao.sessaoLayout.codigo == sessao.codigo) {
								for (var indexCampo in sessao.campos) {
									var campo = sessao.campos[indexCampo];
									if (campo.codigo == value) {
										return campo;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return {};
	}

	showInsertButtonStyle () {
		return this.state.validacao.id == null || this.state.validacao.id == '';
	}
	
	showUpdateButtonStyle () {
		return this.state.validacao.id != null && this.state.validacao.id != '';
	}

    render () {
    	
        return (

    		<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Validação de Arquivo - Cadastro
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

									<SelectInput
										id="campoValidacao"
										placeholder="Selecione..."
										name={'campoValidacao'}
										label="Campo de Validação"
										options={this.state.camposValidacoes}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeCampoValidacao}
										selectedValue={this.state.validacaoSelecionada.campoValidacao}
										required={true}
										chave={true}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="tipoLayout"
										placeholder="Selecione..."
										name={'tipoLayout'}
										label="Tipo de Layout"
										options={this.state.tiposLayouts}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeTipoLayout} 
										selectedValue={this.state.validacaoSelecionada.tipoLayout} 
										required={true}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="layout"
										placeholder="Selecione..."
										name={'layout'}
										label="Layout"
										options={this.state.layouts}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeLayout} 
										selectedValue={this.state.validacaoSelecionada.layout} 
										required={true}
										chave={true}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors}/>
											
									<SelectInput 
										id="localValidacao"
										placeholder="Selecione..."
										name={'localValidacao'}
										label="Local de Validação"
										options={this.state.locaisValidacoes}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeLocalValidacao} 
										selectedValue={this.state.validacaoSelecionada.localValidacao} 
										required={true}
										chave={true}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="sessaoLayout"
										ref="sessaoLayout"
										placeholder="Selecione..."
										name={'sessaoLayout'}
										label="Sessão do Layout"
										options={this.state.sessoesLayout}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeSessaoLayout} 
										selectedValue={this.state.validacaoSelecionada.sessaoLayout} 
										required={this.state.showValidacaoInterna}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors}
										show={this.state.showValidacaoInterna} />

									<SelectInput 
										id="campoLayout"
										ref="campoLayout"
										placeholder="Selecione..."
										name={'campoLayout'}
										label="Campo do Layout"
										options={this.state.camposLayout}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeCampoLayout} 
										selectedValue={this.state.validacaoSelecionada.campoLayout}
										required={this.state.showValidacaoInterna}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors}
										show={this.state.showValidacaoInterna} />

									<TextInput 
										id="posicaoInicial"
										label="Posição Inicial"
										ref="posicaoInicial"
										placeholder="Somente Números Inteiros"
										type="text"
										maxLength="4"
										selectedValue={this.state.validacaoSelecionada.posicaoInicial} 
										integerOnly={this.state.showValidacaoExterna}
										required={this.state.showValidacaoExterna}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changePosicaoInicial} 
										showError={this.state.showErrors}
										show={this.state.showValidacaoExterna} />
									
									<TextInput 
										id="posicaoFinal"
										label="Posição Final"
										ref="posicaoFinal"
										placeholder="Somente Números Inteiros"
										type="text"
										maxLength="4"
										selectedValue={this.state.validacaoSelecionada.posicaoFinal} 
										integerOnly={this.state.showValidacaoExterna}
										required={this.state.showValidacaoExterna}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changePosicaoFinal}
										showError={this.state.showErrors}
										show={this.state.showValidacaoExterna} />

									<TextInput 
										id="formato"
										label="Formato"
										ref="formato"
										type="text"
										maxLength="20"
										required={this.state.showValidacaoExterna}
										selectedValue={this.state.validacaoSelecionada.formato} 
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeFormato}
										showError={this.state.showErrors}
										show={this.state.showValidacaoExterna} />

									<ButtonCrud 
										showIncluir={this.showInsertButtonStyle()} 
										showAlterar={this.showUpdateButtonStyle() && this.state.showValidacaoExterna}
										showConsultar={false}
										label="Novo"
										incluir={this.incluir()} 
										alterar={this.alterar()} 
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