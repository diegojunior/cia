import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../ui/form/FormValidator';
import TextInput from '../../../ui/input/TextInput';
import RadioInput from '../../../ui/input/RadioInput';
import SelectInput from '../../../ui/input/SelectInput';
import ConfiguracaoServicoService from '../../../../shared/services/ConfiguracaoServico';
import UnidadeImportacaoChaveService from '../../../../shared/services/UnidadeImportacaoChave';
import UnidadeImportacaoBlocoService from '../../../../shared/services/UnidadeImportacaoBloco';
import Datatable from '../../../ui/datatable/Datatable';
import AddRemoveMultiplesInputs from './PerfilAddRemoveMultiplesInputs';
import {constants} from '../../../../shared/util/Constants';
import {sort} from '../../../../shared/util/Utils';
import './Style.css';

export default class ConfiguracaoConciliacao extends Component {

	constructor(props) {
		super(props);

		this.state = {
			configuracao: {localizacaoCampos: null, identificacao: null, servico: null, consolidarDados: false, configuracoesCampos: []},

			layoutSelecionado: null,
			sistemaSelecionado: null,
			camposSelecionados: [],
			localizacaoCampos: [],
			
			sessoes: [],
			unidades: [],
			servicos: [],
			consolidarOptions: constants.getSimOuNao(),
			
			camposCarga: [],
			camposImportacao: [],
			camposEquivalencia: [],

			fieldsConfig: [],

			showErrors: false
		};

		this.changeLocalizacaoCampos = this.changeLocalizacaoCampos.bind(this);
		this.changeSessao = this.changeSessao.bind(this);
		this.changeUnidade = this.changeUnidade.bind(this);
		this.changeServico = this.changeServico.bind(this);
		this.changeConsolidarDados = this.changeConsolidarDados.bind(this);

		this.refresh = this.refresh.bind(this);

		this.carregaServicos = this.carregaServicos.bind(this);
		this.carregaSessoes = this.carregaSessoes.bind(this);
		this.carregaUnidades = this.carregaUnidades.bind(this);

		this.carregaCamposCarga = this.carregaCamposCarga.bind(this);
		this.carregaCamposImportacao = this.carregaCamposImportacao.bind(this);
		this.carregaCamposEquivalencia = this.carregaCamposEquivalencia.bind(this);

		this.getCamposSessao = this.getCamposSessao.bind(this);
		this.getCamposUnidade = this.getCamposUnidade.bind(this);
		this.getCamposServico = this.getCamposServico.bind(this);
		this.getNomeSessao = this.getNomeSessao.bind(this);
		this.getCamposFormatados = this.getCamposFormatados.bind(this);

		this.clear = this.clear.bind(this);
		this.isValid = this.isValid.bind(this);

		this.checkDuplicity = this.checkDuplicity.bind(this);

		this.getCamposServicoByServicoAndCodigoCampo = this.getCamposServicoByServicoAndCodigoCampo.bind(this);
	}

	componentWillMount () {

		this.state.localizacaoCampos = constants.getLocalizacoesCampos();
		
		this.state.fieldsConfig = [
			{	key:'chave', 
				label:'Chave', 
				type: 'checkbox',
				uncheckInputs: ['conciliavel', 'informativo']
			},
			{	key:'conciliavel', 
				label:'Conciliável', 
				type: 'checkbox',
				uncheckInputs: ['chave', 'informativo']
			},
			{	key:'informativo', 
				label:'Informativo', 
				type: 'checkbox',
				uncheckInputs: ['chave', 'conciliavel']
			},
			{	key:'campoCarga', 
				label:'Campo da Carga', 
				type: 'select',
				initialValue: {id: null, codigo: ''}, 
				placeholder: "Selecione...", 
				options: this.carregaCamposCarga
			},
			{	key:'campoImportacao', 
				label:'Campo da Importação', 
				type: 'select',
				initialValue: {id: null, codigo: ''}, 
				placeholder: "Selecione...", 
				options: this.carregaCamposImportacao
			},
			{	key:'campoEquivalente',
				label:'Campo Equivalente',
				type: 'select',
				initialValue: {id: null, codigo: ''}, 
				placeholder: "Selecione...", 
				options: this.carregaCamposEquivalencia
			}
		];
	}

	componentWillReceiveProps(nextProps) {

		if (nextProps.identificacao != null) {
			this.state.configuracao.localizacaoCampos = this.state.configuracao.localizacaoCampos == null ? constants.getLocalizacoesCampos()[0] : this.state.configuracao.localizacaoCampos;

			if (this.state.layoutSelecionado != null &&
				this.state.layoutSelecionado.id != nextProps.identificacao.layout.id) {
				this.clear();
			} else if (this.state.sistemaSelecionado != null &&
					this.state.sistemaSelecionado.codigo != nextProps.identificacao.sistema.codigo) {
				this.clear();
			}

			if (nextProps.identificacao.sistema != null) {
				this.state.sistemaSelecionado = nextProps.identificacao.sistema;
				this.carregaServicos(nextProps.identificacao.sistema);
			}

			if (nextProps.identificacao.layout != null) {
				this.state.layoutSelecionado = nextProps.identificacao.layout;
				this.carregaSessoes(nextProps.identificacao.layout);
				this.carregaUnidades(nextProps.identificacao.layout);
			}
		}
	}

	showErrors (isShowErrors) {
		this.setState({showErrors: isShowErrors});
	}

	isValidForm() {
		this.refresh();
		if (this.state.configuracao.configuracoesCampos.length == 0) {
			this.props.warningMessage('É necessário configurar os campos da Conciliação!');
			return false;
		}
		return this.isValid();
	}

	isValid(){
		
		if (this.refs.formValidator.isValid()) {

			var fields = this.refs.addRemoveMultiplesInputs.state.rows;
			var campos = [];
			var possuiConciliavel = false;
			var possuiChave = false;
			for (var indexField in fields) {
				var field = fields[indexField];
	
				var chave = field['chave'+indexField] ? true : false;
				var conciliavel = field['conciliavel'+indexField] ? true : false;
				var informativo = field['informativo'+indexField] ? true : false;
				
				var campoCarga = field['campoCarga'+indexField] ? field['campoCarga'+indexField].codigo : '';
				var campoImportacao = field['campoImportacao'+indexField] ? field['campoImportacao'+indexField].codigo : '';
				var campoEquivalente = field['campoEquivalente'+indexField] ? field['campoEquivalente'+indexField].codigo : '';

				if (!chave && !conciliavel && !informativo){
					this.props.warningMessage("É obrigatório selecionar uma opção entre 'Chave', 'Conciliável' e 'Informativo'.");
					return false;
				}
				if ((chave && (conciliavel || informativo)) ||
					(conciliavel && (chave || informativo))) {
					this.props.warningMessage("Somente uma opção entre 'Chave', 'Conciliável' e 'Informativo' deve ser selecionada!");
					return false;
				}
				if (chave && (campoCarga == '' || campoImportacao == '')) {
					this.props.warningMessage("Para determinar uma configuração de Chave, o 'Campo de Carga' e 'Campo de Importação' são obrigatórios!");
					return false;
				}
				if (conciliavel && (campoCarga == '' || campoImportacao == '')) {
					this.props.warningMessage("Para determinar uma configuração de campo Conciliável, o 'Campo de Carga' e 'Campo de Importação' são obrigatórios!");
					return false;
				}
				if (informativo && (campoCarga == '' || campoImportacao != '' || campoEquivalente != '')) {
					this.props.warningMessage("Para determinar uma configuração de campo Informatívo, somente o 'Campo de Carga' deve ser preenchido!");
					return false;
				}
				if (chave) {
					possuiChave = true;
				} else if (conciliavel) {
					possuiConciliavel = true;
				}
			}

			if (!possuiChave) {
				this.props.warningMessage("Nenhum Campo Chave Configurado!");
				return false;
			}
			if (!possuiConciliavel) {
				this.props.warningMessage("Nenhum Campo Conciliável Configurado!");
				return false;
			}
			return true;
		}
		return false;
	}


	clear () {

		this.state.camposImportacao = [];
		this.state.camposCarga = [];
		this.state.camposEquivalencia = [];

		this.state.configuracao = {localizacaoCampos: null, identificacao: null, servico: null, consolidarDados: false, configuracoesCampos: []};
		this.state.configuracao.localizacaoCampos = constants.getLocalizacoesCampos()[0];
		this.refs.localizacaoCampos.selecionar(0);
		
		this.refs.sessoes.clear();
		this.refs.unidades.clear();
		this.refs.servicos.clear();

		this.state.layoutSelecionado = null;
		this.state.sistemaSelecionado = null;
		this.state.camposSelecionados = [];
		this.state.unidades.length = 0;
		this.state.sessoes.length = 0;
		this.refs.addRemoveMultiplesInputs.limpar();

		var newState = update(this.state, {showErrors: {$set: false}});
		this.setState(newState);

	}

	carregaServicos (sistema) {
		let constantSistema = constants.getConstantSistemasPorCodigo(sistema.codigo);

		if (this.state.servicos.length == 0) {

			ConfiguracaoServicoService.getBy(constantSistema)
				.then (data => {
					
					var newState = update(this.state, {servicos: {$set: data}});
					this.setState(newState);

				}).catch (error => {
					this.props.errorMessage("Nao foi possivel carregar os Serviços");
				});
			}
	}

	carregaSessoes (layout) {
		var newState = update(this.state, {sessoes: {$set: layout.sessoes}});
		this.setState(newState);
	}

	carregaUnidades (layout) {
		if (layout != null && this.state.unidades.length == 0) {
			if (layout.tipoLayout.codigo == 'DEL') {
				UnidadeImportacaoBlocoService.getByLayout(layout.id)
					.then (data => {
						
						var newState = update(this.state, {unidades: {$set: data}});
						this.setState(newState);

					}).catch (error => {
						this.props.errorMessage("Nao foi possivel carregar as Parametrizações de Unidade");
					});
			} else {
				UnidadeImportacaoChaveService.getByLayout(layout.id)
				.then (data => {
					
					var newState = update(this.state, {unidades: {$set: data}});
					this.setState(newState);

				}).catch (error => {
					this.props.errorMessage("Nao foi possivel carregar as Parametrizações de Unidade");
				});
			}
		}
	}

	carregaCamposCarga() {
		return this.state.camposCarga;
	}

	carregaCamposImportacao() {
		return this.state.camposImportacao;
	}

	carregaCamposEquivalencia() {
		return this.state.camposEquivalencia;
	}

	changeLocalizacaoCampos (value) {
		this.state.camposImportacao = [];
		this.state.camposCarga = [];
		this.state.camposEquivalencia = [];

		this.state.configuracao.identificacao = null;
		this.state.configuracao.servico = null;
		this.state.camposSelecionados = [];

		this.refs.sessoes.clear();
		this.refs.unidades.clear();
		this.refs.servicos.clear();
		
		this.refs.addRemoveMultiplesInputs.limpar();

		var newState = update(this.state, {configuracao: {localizacaoCampos: {$set: constants.getLocalizacoesCampos()[value]}}});
		this.setState(newState);
	}

	changeSessao (value) {
		
		this.state.camposCarga = [];
		this.state.camposEquivalencia = [];
		
		this.state.configuracao.servico = null;
		this.state.camposSelecionados = [];
		
		this.refs.unidades.clear();
		this.refs.formValidator.removeError('unidades');

		this.refs.servicos.clear();
		
		this.refs.addRemoveMultiplesInputs.limpar();

		this.state.camposImportacao = this.getCamposSessao(value);

		var newState = update(this.state, {configuracao: {identificacao: {$set: value}}});
		this.setState(newState);
	}

	changeUnidade (value) {
		
		this.state.camposCarga = [];
		this.state.camposEquivalencia = [];
		
		this.state.configuracao.servico = null;
		this.state.camposSelecionados = [];

		this.refs.sessoes.clear();
		this.refs.formValidator.removeError('sessoes');

		this.refs.servicos.clear();
		
		this.refs.addRemoveMultiplesInputs.limpar();

		this.state.camposImportacao =  this.getCamposUnidade(value);

		var newState = update(this.state, {configuracao: {identificacao: {$set: value}}});
		this.setState(newState);
	}

	changeServico (value) {

		this.state.camposSelecionados = [];
		
		this.refs.addRemoveMultiplesInputs.limpar();
		
		this.state.camposCarga = this.getCamposServico(value);
		this.state.camposEquivalencia = this.getCamposServico(value);

		var newState = update(this.state, {configuracao: {servico: {$set: value}}});
		this.setState(newState);
	}

	changeConsolidarDados (value) {
		let consolidar = '';
		if (value) {
			consolidar = constants.getSimOuNaoCodigo(value).codigo;
		}
		let newState = update(this.state, {configuracao: {consolidarDados: {$set: consolidar}}});
		this.setState(newState);
	}

	refresh () {
		var fields = this.refs.addRemoveMultiplesInputs.getFields();

		var campos = [];

		for (var indexField in fields) {
			var field = fields[indexField];

			var chave = field['chave'+indexField] ? true : false;
			var conciliavel = field['conciliavel'+indexField] ? true : false;
			var informativo = field['informativo'+indexField] ? true : false;
			var campoCarga = field['campoCarga'+indexField] ? this.getCamposServicoByServicoAndCodigoCampo(field['campoCarga'+indexField].codigo) : '';
			var campoImportacao = field['campoImportacao'+indexField] ? field['campoImportacao'+indexField].codigo : '';
			var campoEquivalente = field['campoEquivalente'+indexField] ? this.getCamposServicoByServicoAndCodigoCampo(field['campoEquivalente'+indexField].codigo) : '';

			var campo = {chave: chave, 
						 conciliavel: conciliavel,
						 informativo: informativo,
						 campoCarga: campoCarga,
						 campoImportacao: campoImportacao,
						 campoEquivalente: campoEquivalente,
						 ordem: indexField};

			if (!this.isExisteDuplicidade(campos, campo)){
				campos.push(campo);
			}

		}
	
		this.state.configuracao.configuracoesCampos = campos;

	}

	isValid(){
		
		if (this.refs.formValidator.isValid()) {

			var fields = this.refs.addRemoveMultiplesInputs.state.rows;
			var campos = [];
			var possuiConciliavel = false;
			var possuiChave = false;
			for (var indexField in fields) {
				var field = fields[indexField];
	
				var chave = field['chave'+indexField] ? true : false;
				var conciliavel = field['conciliavel'+indexField] ? true : false;
				var informativo = field['informativo'+indexField] ? true : false;
				
				var campoCarga = field['campoCarga'+indexField] ? field['campoCarga'+indexField].codigo : '';
				var campoImportacao = field['campoImportacao'+indexField] ? field['campoImportacao'+indexField].codigo : '';
				var campoEquivalente = field['campoEquivalente'+indexField] ? field['campoEquivalente'+indexField].codigo : '';

				if (!chave && !conciliavel && !informativo){
					this.props.warningMessage("É obrigatório selecionar uma opção entre 'Chave', 'Conciliável' e 'Informativo'.");
					return false;
				}
				if ((chave && (conciliavel || informativo)) ||
					(conciliavel && (chave || informativo))) {
					this.props.warningMessage("Somente uma opção entre 'Chave', 'Conciliável' e 'Informativo' deve ser selecionada!");
					return false;
				}
				if (chave && (campoCarga == '' || campoImportacao == '')) {
					this.props.warningMessage("Para determinar uma configuração de Chave, o 'Campo de Carga' e 'Campo de Importação' são obrigatórios!");
					return false;
				}
				if (conciliavel && (campoCarga == '' || campoImportacao == '')) {
					this.props.warningMessage("Para determinar uma configuração de campo Conciliável, o 'Campo de Carga' e 'Campo de Importação' são obrigatórios!");
					return false;
				}
				if (informativo && (campoCarga == '' || campoImportacao != '' || campoEquivalente != '')) {
					this.props.warningMessage("Para determinar uma configuração de campo Informatívo, somente o 'Campo de Carga' deve ser preenchido!");
					return false;
				}
				if (chave) {
					possuiChave = true;
				} else if (conciliavel) {
					possuiConciliavel = true;
				}
			}

			if (!possuiChave) {
				this.props.warningMessage("Nenhum Campo Chave Configurado!");
				return false;
			}
			if (!possuiConciliavel) {
				this.props.warningMessage("Nenhum Campo Conciliável Configurado!");
				return false;
			}
			return true;
		}
		return false;
	}

	checkDuplicity (stateParam) {
		
		var fields = stateParam.rows;

		var campos = [];

		for (var indexField in fields) {
			var field = fields[indexField];

			var chave = field['chave'+indexField] ? true : false;
			var conciliavel = field['conciliavel'+indexField] ? true : false;
			var informativo = field['informativo'+indexField] ? true : false;
			
			var campoCarga = field['campoCarga'+indexField] ? this.getCamposServicoByServicoAndCodigoCampo(field['campoCarga'+indexField].codigo) : '';
			var campoImportacao = field['campoImportacao'+indexField] ? field['campoImportacao'+indexField].codigo : '';
			var campoEquivalente = field['campoEquivalente'+indexField] ? this.getCamposServicoByServicoAndCodigoCampo(field['campoEquivalente'+indexField].codigo) : '';
			
			var campo = {chave: chave, 
						conciliavel: conciliavel,
						informativo: informativo,
						campoCarga: campoCarga,
						campoImportacao: campoImportacao,
						campoEquivalente: campoEquivalente,
						ordem: indexField};
						
			if (this.isExisteDuplicidade(campos, campo)) {
				return this.refs.addRemoveMultiplesInputs.removeRow(stateParam);
			} else {
				campos.push(campo);
			}
		}

		return stateParam;
	}

	isExisteDuplicidade (campos, novoCampo) {
		if (this.chaveDuplicada(campos, novoCampo)) {
			this.props.errorMessage('Configuração de Campo Chave Duplicada');
			return true;
		} else if (this.conciliavelDuplicado(campos, novoCampo)) {
			this.props.errorMessage('Configuração de Campo Conciliável Duplicada');
			return true;
		} else if (this.informativoDuplicado(campos, novoCampo)) {
			this.props.errorMessage('Configuração de Campo Informativo Duplicada');
			return true;
		}
		return false;
	}

	chaveDuplicada (campos, novoCampo) {
		for (let index in campos) {
			let campo = campos[index];
			if (campo.chave == true &&
				campo.chave == novoCampo.chave &&
			    campo.campoCarga.codigo == novoCampo.campoCarga.codigo &&
				campo.campoImportacao == novoCampo.campoImportacao) {
				return true;
			}
		}
		return false;
	}

	conciliavelDuplicado (campos, novoCampo) {
		for (let index in campos) {
			let campo = campos[index];
			if (campo.conciliavel == true && 
				campo.conciliavel == novoCampo.conciliavel &&
			    campo.campoCarga.codigo == novoCampo.campoCarga.codigo &&
				campo.campoImportacao == novoCampo.campoImportacao) {
				return true;
			}
		}
		return false;
	}

	informativoDuplicado (campos, novoCampo) {
		for (let index in campos) {
			let campo = campos[index];
			if (campo.informativo == true &&
				campo.informativo == novoCampo.informativo &&
			    campo.campoCarga.codigo == novoCampo.campoCarga.codigo) {
				return true;
			}
		}
		return false;
	}

	getNomeSessao(codigo) {
		for (var index in this.state.sessoes) {
			var sessao = this.state.sessoes[index];
			if (sessao.codigo == codigo) {
				return sessao.nome;
			}
		}
		return null;
	}

	getCamposSessao(value) {
		for (var index in this.state.sessoes) {
			var sessao = this.state.sessoes[index];
			if (sessao.codigo == value) {
				sort(sessao.campos, 'codigo')
				return sessao.campos;
			}
		}
		return [];
	}

	getCamposUnidade(value) {
		for (var index in this.state.unidades) {
			var unidade = this.state.unidades[index];
			if (unidade.codigo == value) {
				sort(unidade.campos, 'codigo')
				return unidade.campos;
			}
		}
		return [];
	}

	getCamposServico(value) {
		for (var index in this.state.servicos) {
			var servico = this.state.servicos[index];
			if (servico.codigo == value) {
				sort(servico.campos, 'label')
				return servico.campos;
			}
		}
		return [];
	}

	getCamposServicoByServicoAndCodigoCampo(codigoCampo) {
		for (var indexCampo in this.state.camposCarga) {
			var campo = this.state.camposCarga[indexCampo];
			if (campo.codigo == codigoCampo) {
				return campo;
			}
		}
		return {};
	}

	getCamposFormatados (campos) {
		var camposFormatados = [];
		for (var index in campos) {
			var campo = campos[index];
			var campoFormatado = {chave: campo.chave, 
								  conciliavel: campo.conciliavel,
								  informativo: campo.informativo,
								  campoCarga: {codigo: campo.campoCarga.codigo},
								  campoImportacao: {codigo: campo.campoImportacao},
								  campoEquivalente: {codigo: campo.campoEquivalente.codigo}};
			camposFormatados.push(campoFormatado);
		}
		return camposFormatados;
	}

	render(){
	    
		return (
			<div className="cia-animation-fadein">
    			<div className="row">
    				
					<div className="col-lg-12">
						<div className="panel panel-default" id="cadPanel">
							<div className="panel-body">
								<FormValidator ref="formValidator">

									<RadioInput
										id="localizacaoCampos"
										ref="localizacaoCampos"
										name={'localizacaoCampos'}
										required={true}
										options={this.state.localizacaoCampos}
										className="col-sm-5 col-md-5 col-lg-5"
										onChange={this.changeLocalizacaoCampos}
										showError={this.state.showErrors} />

									<SelectInput 
										id="sessoes"
										ref="sessoes"
										labelValidator="Sessão do Layout"
										placeholder="Selecione..."
										name={'sessoes'}
										required={this.state.configuracao.localizacaoCampos != null && this.state.configuracao.localizacaoCampos.codigo == 'SES'}
										options={this.state.sessoes}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeSessao}
										show={this.state.configuracao.localizacaoCampos != null && this.state.configuracao.localizacaoCampos.codigo == 'SES'}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="unidades"
										ref="unidades"
										labelValidator="Parametrização de Unidade"
										placeholder="Selecione..."
										name={'unidades'}
										required={this.state.configuracao.localizacaoCampos != null && this.state.configuracao.localizacaoCampos.codigo == 'UNI'}
										options={this.state.unidades}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeUnidade}
										show={this.state.configuracao.localizacaoCampos != null && this.state.configuracao.localizacaoCampos.codigo == 'UNI'}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="servicos"
										ref="servicos"
										placeholder="Selecione..."
										name={'servicos'}
										label="Serviço"
										required={true}
										options={this.state.servicos}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeServico}
										showError={this.state.showErrors}/>

									<SelectInput 
										id="consolidaDadosId"
										ref="consolidar"
										placeholder="Selecione..."
										name={'consolidar'}
										label="Consolidar Dados"
										required={true}
										options={this.state.consolidarOptions}
										className="col-sm-5 col-md-5 col-lg-5"
										valueChange={this.changeConsolidarDados}
										showError={this.state.showErrors}/>	

									<AddRemoveMultiplesInputs 
										ref="addRemoveMultiplesInputs"
										label="Configuração"
										fieldsConfig={this.state.fieldsConfig}
										valueChange={this.refresh}
										selectedValue={this.state.camposSelecionados}
										className="control-group limitsAddRemoveMultiplesInputs"
										headerClassName="control-group header-table"
										showError={this.state.showErrors} 
										checkRowDuplicity={this.checkDuplicity }/>

								</FormValidator>
							</div>
						</div>
					</div>
    			</div>
    		</div>
		);
	}
}