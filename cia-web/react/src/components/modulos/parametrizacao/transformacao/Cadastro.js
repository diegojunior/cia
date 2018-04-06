import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../ui/form/FormValidator';
import TextInput from '../../../ui/input/TextInput';
import SelectInput from '../../../ui/input/SelectInput';
import TransformacaoMultiplesInputs from './TransformacaoMultiplesInputs';
import ButtonCrud from '../../../ui/button/ButtonCrud';
import TransformacaoService from '../../../../shared/services/Transformacao';
import LayoutService from '../../../../shared/services/Layout';
import {constants} from '../../../../shared/util/Constants';
import {sort} from '../../../../shared/util/Utils';
import './Style.css';

export default class Cadastro extends Component {
	
	constructor(props) {
		super(props);

		this.state = {
			transformacao: {
				id: null, 
				tipoLayout: '', 
				layout: null, 
				sessao: null, 
				campo: null, 
				tipoTransformacao: null,
				item: {
					itemFixo: {id: null, itensDePara: []},
					itemEquivalencia: {id: null, sistema: null, remetente: null, tipoEquivalencia: null}
				}
			},
			transformacaoSelecionada: {
				id: null, 
				tipoLayout: '', 
				layout: null, 
				sessao: null, 
				campo: null, 
				tipoTransformacao: null,
				item: {
					itemFixo: {id: null, itensDePara: []},
					itemEquivalencia: {id: null, sistema: null, remetente: null, tipoEquivalencia: null}
				}			
			},	
			tiposLayouts: [],
			tiposTransformacoes: [],
			sistemas: [],

			layouts: [],
			sessoes: [],
			campos: [],

			remetentes: [],
			tiposEquivalencias: [],

			showTipoTransformacaoFixo: false,
			showTipoTransformacaoEquivalencia: false,

			showErrors: false
			
		};

		this.incluir = this.incluir.bind(this);
		this.limpar = this.limpar.bind(this);
		
		this.changeTipoLayout= this.changeTipoLayout.bind(this);
		this.changeLayout = this.changeLayout.bind(this);
		this.changeSessao = this.changeSessao.bind(this);
		this.changeCampo = this.changeCampo.bind(this);
		this.changeTipoTransformacao = this.changeTipoTransformacao.bind(this);
		this.changeSistema = this.changeSistema.bind(this);
		this.changeRemetente = this.changeRemetente.bind(this);
		this.changeTipoEquivalencia = this.changeTipoEquivalencia.bind(this);
		
		this.refreshItens = this.refreshItens.bind(this);

		this.getLayout = this.getLayout.bind(this);
		this.getSessao = this.getSessao.bind(this);
		this.getCampo = this.getCampo.bind(this);
		this.getRemetente = this.getRemetente.bind(this);
		this.getTipoEquivalencia = this.getTipoEquivalencia.bind(this);

		this.showPesquisa = this.showPesquisa.bind(this);

		this.isValid = this.isValid.bind(this);

		this.getTransformacaoClear = this.getTransformacaoClear.bind(this);

	}

	componentWillMount () {
		this.state.tiposLayouts = constants.getTiposLayouts();
		this.state.tiposTransformacoes = constants.getTiposTransformacoes();
		this.state.sistemas = constants.getSistemasEnum();
	}

	componentWillReceiveProps (nextProps) {
		var elemento = nextProps.template.state.elementoSelecionado;
		this.state.layouts = [];
		this.state.sessoes = [];
		this.state.campos = [];
		
		if (elemento && elemento.id && elemento.id != '') {

			this.refs.formValidator.clearValues();

			this.showTipoTransformacao(elemento.tipoTransformacao.codigo);

			this.state.layouts = [elemento.layout];
			this.state.sessoes = [elemento.sessao];
			this.state.campos = [{id: elemento.campo.id, codigo: elemento.campo.codigo}];			
			
			this.state.transformacaoSelecionada = elemento;
			this.state.transformacaoSelecionada.sessao = elemento.sessao;
			this.state.transformacaoSelecionada.campo = {id: elemento.campo.id, codigo: elemento.campo.codigo};

			if (this.state.showTipoTransformacaoEquivalencia) {
				this.state.remetentes = [elemento.item.itemEquivalencia.remetente];
				this.state.tiposEquivalencias = [elemento.item.itemEquivalencia.tipoEquivalencia];
			}

			this.setState({transformacao: elemento});

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
		let transformacaoClear = {
			id: null, 
			tipoLayout: '', 
			layout: null, 
			sessao: null, 
			campo: null, 
			tipoTransformacao: null,
			item: {
				itemFixo: {id: null, itensDePara: []},
				itemEquivalencia: {id: null, sistema: null, remetente: null, tipoEquivalencia: null}
			}
		};


		let newState = update(this.state, {transformacao: {$set: transformacaoClear}, 
										   transformacaoSelecionada: {$set: transformacaoClear}, 
										   showErrors: {$set: false},
										   showTipoTransformacaoFixo: {$set: false},
										   showTipoTransformacaoEquivalencia: {$set: false}});
		this.setState(newState);
	}

	incluir () {
		
		return (event) => {
			event.preventDefault();

			this.refreshItens(this.refs.addRemoveMultiplesInputs.getFields());

			this.setState({showErrors: true});

			if (this.state.showTipoTransformacaoFixo) {
				this.refs.formValidator.removeError('sistema');
				this.refs.formValidator.removeError('remetente');
				this.refs.formValidator.removeError('tipoEquivalencia');
			} else {
				this.refs.addRemoveMultiplesInputs.clearAllErrors();
			}

			if(this.isValid()) {
				
				TransformacaoService
					.incluir(this.state.transformacao)
						.then (data => {							
							this.props.template.successMessage('Transformação Incluída com Sucesso.');
							this.props.template.addDatatableElement(data);
							this.props.template.clear();
						}).catch(error => {							
							this.props.template.errorMessage(error);
						});
			}
		}
	}

	getTransformacaoClear () {
		return {
				id: null, 
				tipoLayout: '', 
				layout: null, 
				sessao: null, 
				campo: null, 
				tipoTransformacao: null,

				item: {
						itemFixo: {id: null, itensDePara: []},
						itemEquivalencia: {id: null, sistema: null, remetente: null, tipoEquivalencia: null}
				}
			};
	}

	isValid() {
		var isItensDeParaNaoPreenchidos = this.state.showTipoTransformacaoFixo && 
			this.state.transformacao.item.itemFixo.itensDePara.length == 0;

		if (isItensDeParaNaoPreenchidos) {
			this.props.template.warningMessage("Campos 'De' -> 'Para' são obrigatórios.");
			return false;
		}

		return this.refs.formValidator.isValid();
	}

	changeTipoLayout (value) {
		let tipoLayout = constants.getTipoLayoutPorCodigo(value);

		this.state.sessoes = [];
		this.state.campos = [];
		this.state.layouts = [];

		this.refs.layout.clear();
		this.refs.sessaoLayout.clear();
		this.refs.campoLayout.clear();

		if (value != '') {
			let tipoLayoutConstant = constants.getConstantTipoLayoutPorCodigo(value);
			LayoutService.getBy(tipoLayoutConstant)
				.then(data => {
					sort(data, 'codigo');
					this.state.layouts = data;
					let newState = update(this.state, {transformacao: {tipoLayout: {$set: tipoLayout}}});

					this.setState(newState);
				}).catch(error => {
					this.props.template.errorMessage(error);

					let newState = update(this.state, {transformacao: {tipoLayout: {$set: tipoLayout}}});
					this.setState(newState);
				});

		} else {
			let newState = update(this.state, {transformacao: {tipoLayout: {$set: tipoLayout}}});
			this.setState(newState);
		}
	}
	
	changeLayout (value) {

		if (value != '') {
			var layout = this.getLayout(value);
			this.state.sessoes = layout.sessoes;
			this.state.campos = [];

			this.refs.sessaoLayout.clear();
			this.refs.campoLayout.clear();

			let newState = update(this.state, {transformacao: {layout: {$set: layout}}});
			this.setState(newState);

		} else {
			this.state.sessoes = [];
			this.state.campos = [];

			this.refs.sessaoLayout.clear();
			this.refs.campoLayout.clear();

			let newState = update(this.state, {transformacao: {layout: {$set: null}}});
			this.setState(newState);
		}

	}
	
	changeSessao (value) {

		this.refs.campoLayout.clear();

		if (value != '') {
			var sessao = this.getSessao(value);
			sort(sessao.campos, 'codigo');
			this.state.campos = sessao.campos;
			console.log('depois: ', this.state.campos);
			let newState = update(this.state, {transformacao: {sessao: {$set: sessao}}});
			this.setState(newState);

		} else {

			this.state.campos = [];
			let newState = update(this.state, {transformacao: {sessao: {$set: null}}});
			this.setState(newState);
		}
	}

	changeCampo (value) {

		var campo = this.getCampo(value);

		let newState = update(this.state, {transformacao: {campo: {$set: campo}}});
		this.setState(newState);
	}

	changeTipoTransformacao (value) {
		
		this.showTipoTransformacao(value);

		var tipoTransformacao = constants.getTipoTransformacaoPorCodigo(value);

		let newState = update(this.state, {transformacao: {tipoTransformacao: {$set: tipoTransformacao}}});
		newState.transformacaoSelecionada.item.itemFixo = {id: null, itensDePara: []};
		newState.transformacaoSelecionada.item.itemEquivalencia = {id: null, sistema: null, remetente: null, tipoEquivalencia: null};

		newState.transformacao.item.itemFixo = {id: null, itensDePara: []};
		newState.transformacao.item.itemEquivalencia = {id: null, sistema: null, remetente: null, tipoEquivalencia: null};

		this.refs.sistema.forceClear();
		this.refs.remetente.forceClear();
		this.refs.tipoEquivalencia.forceClear();

		this.refs.formValidator.addRequiredError ('sistema', 'Sistema');
		this.refs.formValidator.addRequiredError ('remetente', 'Remetente');
		this.refs.formValidator.addRequiredError ('tipoEquivalencia', 'Tipo de Equivalência');
		
		this.refs.addRemoveMultiplesInputs.clearAllErrors();
		this.refs.addRemoveMultiplesInputs.limpar();
		this.refs.formValidator.addRequiredError('de0', 'De');
		this.refs.formValidator.addRequiredError('para0', 'Para');

		this.setState(newState);
	}

	showTipoTransformacao(value) {
		if (value == 'FIX') {
			this.state.showTipoTransformacaoFixo = true;
			this.state.showTipoTransformacaoEquivalencia = false;
		} else if (value == 'EQU') {
			this.state.showTipoTransformacaoFixo = false;
			this.state.showTipoTransformacaoEquivalencia = true;
		} else {
			this.state.showTipoTransformacaoFixo = false;
			this.state.showTipoTransformacaoEquivalencia = false;
		}
	}

	changeSistema (value) {
		if (value != '' && value != null)  {
			var sistema = constants.getSistemasPorCodigo(value);

			let constantSistema = constants.getConstantSistemasPorCodigo(sistema.codigo);

			TransformacaoService.loadRemetenteAndTiposEquivalencias(constantSistema)
				.then(response => {

					this.state.remetentes = response.remetentes;
					this.state.tiposEquivalencias = response.tiposEquivalencias;

					let newState = update(this.state, {transformacao: {item: {itemEquivalencia: {sistema: {$set: sistema}}}}});

					newState.transformacao.item.itemEquivalencia.remetente = '';
					newState.transformacao.item.itemEquivalencia.tipoEquivalencia = '';
					newState.transformacaoSelecionada.item.itemEquivalencia.remetente = '';
					newState.transformacaoSelecionada.item.itemEquivalencia.tipoEquivalencia = '';

					this.refs.remetente.clear();
					this.refs.tipoEquivalencia.clear();

					this.setState(newState);

				}).catch(error => {

					this.props.template.errorMessage(error);

					let newState = update(this.state, {transformacao: {item: {itemEquivalencia: {sistema: {$set: sistema}}}}});
					newState.transformacao.item.itemEquivalencia.remetente = '';
					newState.transformacao.item.itemEquivalencia.tipoEquivalencia = '';
					newState.transformacaoSelecionada.item.itemEquivalencia.remetente = '';
					newState.transformacaoSelecionada.item.itemEquivalencia.tipoEquivalencia = '';

					this.refs.remetente.clear();
					this.refs.tipoEquivalencia.clear();
					this.setState(newState);

				});

		} else {

			let newState = update(this.state, {transformacao: {item: {itemEquivalencia: {sistema: {$set: ''}}}}});
			newState.remetentes = [];
			newState.tiposEquivalencias = [];
			newState.transformacao.item.itemEquivalencia.remetente = '';
			newState.transformacao.item.itemEquivalencia.tipoEquivalencia = '';
			newState.transformacaoSelecionada.item.itemEquivalencia.remetente = '';
			newState.transformacaoSelecionada.item.itemEquivalencia.tipoEquivalencia = '';

			this.refs.remetente.clear();
			this.refs.tipoEquivalencia.clear();
			this.setState(newState);
			
		}
	}

	changeRemetente (value) {
		if (value != '' && value != null)  {

			var remetente = this.getRemetente(value);

			let newState = update(this.state, {transformacao: {item: {itemEquivalencia: {remetente: {$set: remetente}}}}});
			this.setState(newState);

		} else {

			let newState = update(this.state, {transformacao: {item: {itemEquivalencia: {remetente: {$set: {}}}}}});
			this.setState(newState);

		}
	}

	changeTipoEquivalencia(value) {
		if (value != '' && value != null)  {
		
			var tipoEquivalencia = this.getTipoEquivalencia(value);

			let newState = update(this.state, {transformacao: {item: {itemEquivalencia: {tipoEquivalencia: {$set: tipoEquivalencia}}}}});
			this.setState(newState);

		} else {

			let newState = update(this.state, {transformacao: {item: {itemEquivalencia: {tipoEquivalencia: {$set: ''}}}}});
			this.setState(newState);
			
		}
	}

	refreshItens (rows) {

		var fields = rows;

		var campos = [];

		for (var indexField in fields) {
			var field = fields[indexField];

			if (field['de'+indexField] == '' || field['para'+indexField] == '') {
				campos = [];
				break;
			}

			var campo = {id: field['id'], 
					de: field['de'+indexField], 
					para: field['para'+indexField]};

			campos.push(campo);
			
		}

		if (this.state.showTipoTransformacaoEquivalencia) {
			this.state.transformacao.item.itemFixo = null;
		} else {
			this.state.transformacao.item.itemFixo.itensDePara = campos;
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

	getSessao(value) {
		for (var index in this.state.sessoes) {
			var sessao = this.state.sessoes[index];
			if (sessao.codigo == value) {
				return sessao;
			}
		}
		return {};
	}

	getSessoesFormatadas(sessoes) {
		var sessoesFormatadas = [];
		for (var index in sessoes) {
			var sessao = sessoes[index];
			sessoesFormatadas.push({id: sessao.id, codigo: sessao.codigo, campos: sessao.campos});
		}
		return sessoesFormatadas;
	}

	getCampo(value) {

		for (var index in this.state.sessoes) {
			var sessao = this.state.sessoes[index];
			for (var i in sessao.campos) {
				var campo = sessao.campos[i];
				if (campo.codigo == value) {
					return campo;
				}
			}
		}
		return {};
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

	getTipoEquivalencia(value) {
		
		for (var index in this.state.tiposEquivalencias) {
			var tipoEquivalencia = this.state.tiposEquivalencias[index];
			if (tipoEquivalencia.codigo == value) {
				return tipoEquivalencia;
			}
		}
		return {};
	}

	showInsertButtonStyle () {
		return this.state.transformacao.id == null || this.state.transformacao.id == '';
	}
	
	showUpdateButtonStyle () {
		return this.state.transformacao.id != null && this.state.transformacao.id != '';
	}

	headersInputMultiples() {
		return (
			<tr>
				<th id='campoDeIdHeader' key='deKey' style='width: 20%'>
					<div className='formRequired'>
						<label className="control-label">De&nbsp;<div className="fa fa-key" style={{color: '#c3ae09'}}></div></label>
					</div>
				</th>
				<th id='campoParaIdHeader' key='paraKey' style='width: 20%'>
					<div className='formRequired'>
						<label className="control-label">Para&nbsp;<div className="fa fa-key" style={{color: '#c3ae09'}}></div></label>
					</div>
				</th>
			</tr>
			);
	}

    render () {
    	
        return (

    		<div className="cia-animation-fadein">
				<div className="row">
					<div className="col-lg-12">
						<div className="page-header">
							<Link to="/cia/home" className="fa fa-home"></Link> - Parametrização - Transformação - Cadastro
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
										id="tipoLayout"
										placeholder="Selecione..."
										name={'tipoLayout'}
										label="Tipo Layout"
										options={this.state.tiposLayouts}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeTipoLayout} 
										selectedValue={this.state.transformacaoSelecionada.tipoLayout} 
										required={true}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="layout"
										ref="layout"
										placeholder="Selecione..."
										name={'layout'}
										label="Layout"
										options={this.state.layouts}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeLayout} 
										selectedValue={this.state.transformacaoSelecionada.layout} 
										required={true}
										chave={true}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="sessao"
										ref="sessaoLayout"
										placeholder="Selecione..."
										name={'sessao'}
										label="Sessão"
										options={this.state.sessoes}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeSessao} 
										selectedValue={this.state.transformacaoSelecionada.sessao} 
										required={true}
										chave={true}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="campo"
										ref="campoLayout"
										placeholder="Selecione..."
										name={'campo'}
										label="Campo"
										options={this.state.campos}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeCampo} 
										selectedValue={this.state.transformacaoSelecionada.campo}
										required={true}
										chave={true}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="tipoTransformacao"
										placeholder="Selecione..."
										name={'tipoTransformacao'}
										label="Tipo Transformação"
										options={this.state.tiposTransformacoes}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeTipoTransformacao}
										selectedValue={this.state.transformacaoSelecionada.tipoTransformacao} 
										required={true}
										readOnly={this.showUpdateButtonStyle()}
										showError={this.state.showErrors} />

									<TransformacaoMultiplesInputs 
										ref="addRemoveMultiplesInputs"
										fields={this.state.fieldsConfig}
										valueChange={this.refreshItens} 
										selectedValue={this.state.transformacaoSelecionada.item.itemFixo.itensDePara}
										showError={this.state.showErrors} 
										readOnly={this.showUpdateButtonStyle()}
										show={this.state.showTipoTransformacaoFixo} />

									<SelectInput 
										id="sistema"
										ref="sistema"
										placeholder="Selecione..."
										name={'sistema'}
										label="Sistema"
										options={this.state.sistemas}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeSistema}
										selectedValue={this.state.transformacaoSelecionada.item.itemEquivalencia.sistema} 
										required={this.state.showTipoTransformacaoEquivalencia}
										showError={this.state.showErrors} 
										readOnly={this.showUpdateButtonStyle()}
										show={this.state.showTipoTransformacaoEquivalencia} />

									<SelectInput 
										id="remetente"
										ref="remetente"
										placeholder="Selecione..."
										name={'remetente'}
										label="Remetente"
										options={this.state.remetentes}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeRemetente}
										selectedValue={this.state.transformacaoSelecionada.item.itemEquivalencia.remetente} 
										required={this.state.showTipoTransformacaoEquivalencia}
										showError={this.state.showErrors} 
										readOnly={this.showUpdateButtonStyle()}
										show={this.state.showTipoTransformacaoEquivalencia}/>

									<SelectInput 
										id="tipoEquivalencia"
										ref="tipoEquivalencia"
										placeholder="Selecione..."
										name={'tipoEquivalencia'}
										label="Tipo de Equivalência"
										options={this.state.tiposEquivalencias}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeTipoEquivalencia}
										selectedValue={this.state.transformacaoSelecionada.item.itemEquivalencia.tipoEquivalencia} 
										required={this.state.showTipoTransformacaoEquivalencia}
										showError={this.state.showErrors} 
										readOnly={this.showUpdateButtonStyle()}
										show={this.state.showTipoTransformacaoEquivalencia} />

									<ButtonCrud 
										showIncluir={this.showInsertButtonStyle()} 
										showAlterar={false}
										showConsultar={false}
										disabledAlterar={this.state.showTipoTransformacaoEquivalencia}
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